package com.sti.sticanteen.data.network.repository

import android.util.Log
import com.sti.sticanteen.data.local.dao.TagDao
import com.sti.sticanteen.data.local.entity.relations.cross_refs.ProductTagCrossRef
import com.sti.sticanteen.data.mapper.mapToDomain
import com.sti.sticanteen.data.mapper.mapToEntity
import com.sti.sticanteen.data.network.api.CanteenApi
import com.sti.sticanteen.data.network.entity.Product
import com.sti.sticanteen.data.network.entity.Tag
import com.sti.sticanteen.domain.repository.TagRepository
import com.sti.sticanteen.utils.Constants.networkError
import com.sti.sticanteen.utils.Constants.serverError
import com.sti.sticanteen.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class TagRepositoryImpl @Inject constructor(
    private val canteenApi: CanteenApi,
    private val tagDao: TagDao
) : TagRepository {

    companion object {
        const val TAG = "TAG_REPOSITORY"
    }


    override suspend fun getTags(): Flow<Resource<List<Tag>>> = flow {
        emit(Resource.Loading())

        val getCachedTags = tagDao.getTags().map { tags -> tags.mapToDomain() }
        if (getCachedTags.isNotEmpty()) {
            Log.i(TAG, "emittedTagCachedValues: $getCachedTags")
            emit(Resource.Loading(data = getCachedTags))
        }

        try {
            val getNetworkCachedTags = canteenApi.getTags()
            if (getNetworkCachedTags.isEmpty() && getCachedTags.isNotEmpty()) {
                tagDao.deleteTags()
                return@flow emit(Resource.Success(data = emptyList()))
            }
            tagDao.insertTags(tags = getNetworkCachedTags.map { it.mapToEntity() })
            emit(Resource.Success(data = getNetworkCachedTags))
        } catch (e: IOException) {
            emit(
                Resource.Error(
                    data = getCachedTags,
                    message = e.message ?: networkError
                )
            )
        } catch (e: HttpException) {
            emit(
                Resource.Error(
                    data = getCachedTags,
                    message = e.message ?: serverError
                )
            )
        }
    }

    override suspend fun getProductsByTags(tags: List<Tag>): Flow<Resource<List<Product>>> = flow {
        emit(Resource.Loading())
        val findCachedProductContainingTags = tagDao.getProductByTags(
            tags = tags.map { tag -> tag.id }
        ).products.map { product -> product.mapToDomain() }

        if (findCachedProductContainingTags.isNotEmpty()) {
            emit(Resource.Success(data = findCachedProductContainingTags))
        }

        try {
            val findNetworkProductsContainingTags = canteenApi.getProductsByTag(
                tags = tags.map { tag -> tag.tag }
            )
            if (findNetworkProductsContainingTags.isEmpty()) {
                tagDao.deleteProductTag()
                return@flow emit(Resource.Success(data = emptyList()))
            }
            findNetworkProductsContainingTags.map { product ->
                product.tags!!.map { tag ->
                    ProductTagCrossRef(
                        productId = product.id,
                        tagId = tag.id
                    )
                }
            }.forEach { productTagCrossRefs ->
                tagDao.insertTagProduct(crossRefs = productTagCrossRefs)
            }
            emit(Resource.Success(data = findNetworkProductsContainingTags))
        } catch (e: IOException) {
            emit(
                Resource.Error(
                    data = findCachedProductContainingTags,
                    message = e.message ?: networkError
                )
            )
        } catch (e: HttpException) {
            emit(
                Resource.Error(
                    data = findCachedProductContainingTags,
                    message = e.message ?: serverError
                )
            )
        }
    }
}