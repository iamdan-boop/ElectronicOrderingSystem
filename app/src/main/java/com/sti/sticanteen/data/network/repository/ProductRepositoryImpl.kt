package com.sti.sticanteen.data.network.repository

import android.util.Log
import com.sti.sticanteen.data.local.dao.ProductDao
import com.sti.sticanteen.data.local.dao.TagDao
import com.sti.sticanteen.data.local.entity.relations.cross_refs.ProductTagCrossRef
import com.sti.sticanteen.data.mapper.mapToDomain
import com.sti.sticanteen.data.mapper.mapToEntity
import com.sti.sticanteen.data.network.api.CanteenApi
import com.sti.sticanteen.data.network.entity.Product
import com.sti.sticanteen.domain.repository.ProductRepository
import com.sti.sticanteen.utils.Constants.networkError
import com.sti.sticanteen.utils.Constants.serverError
import com.sti.sticanteen.utils.Resource
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject


@ViewModelScoped
class ProductRepositoryImpl @Inject constructor(
    private val productDao: ProductDao,
    private val canteenApi: CanteenApi,
    private val tagDao: TagDao
) : ProductRepository {

    companion object {
        const val TAG = "PRODUCT_REPOSITORY"
    }

    override suspend fun getProducts(
        type: Int?
    ): Flow<Resource<List<Product>>> = flow {
        emit(Resource.Loading())

        val cachedProducts: List<Product> = when {
            type != null -> productDao.getProducts(type)
            else -> productDao.getProducts()
        }.map { productWithTags ->
            productWithTags.product.mapToDomain().copy(
                tags = productWithTags.tags.map { tagEntity ->
                    tagEntity.mapToDomain()
                }
            )
        }

        if (cachedProducts.isNotEmpty()) {
            emit(Resource.Loading(data = cachedProducts))
            Log.i(TAG, "$cachedProducts")
        }

        try {
            val fetchRemoteProducts = canteenApi.getProducts(type)
            val isCachedValueEqualsToRemote = (cachedProducts.toTypedArray() contentDeepEquals
                    fetchRemoteProducts.toTypedArray())

            Log.i(TAG, "$isCachedValueEqualsToRemote")
            if (type == null && !isCachedValueEqualsToRemote) {
                productDao.deleteProducts()

                /**
                 * now saved the tags and products inside the cached database (room/sqlite)
                 * after fetching all products with its associated tags (many to many)
                 */
                fetchRemoteProducts.map { product ->
                    product.tags!!.map { tag ->
                        ProductTagCrossRef(
                            productId = product.id,
                            tagId = tag.id
                        )
                    }
                }.forEach { productTagCrossRefs ->
                    Log.i(TAG, "$productTagCrossRefs")
                    tagDao.insertTagProduct(crossRefs = productTagCrossRefs)
                }

                productDao.insertProducts(fetchRemoteProducts.map { it.mapToEntity() })
                Log.i(TAG, "emittedNetworkProducts")
                return@flow emit(Resource.Success(data = fetchRemoteProducts))
            }

            productDao.insertProducts(fetchRemoteProducts.map { it.mapToEntity() })
            return@flow emit(Resource.Success(data = fetchRemoteProducts))
        } catch (e: IOException) {
            emit(
                Resource.Error(
                    message = networkError,
                    data = cachedProducts
                )
            )
        } catch (e: HttpException) {
            emit(
                Resource.Error(
                    message = serverError,
                    data = cachedProducts
                )
            )
        }
    }

    override suspend fun searchProducts(
        query: String,
        shouldSearchRemote: Boolean
    ): Flow<Resource<List<Product>>> = flow {
        if (query.isBlank()) return@flow
        emit(Resource.Loading())

        val searchCachedProducts = productDao.searchProduct(query = query).map { it.mapToDomain() }
        if (!shouldSearchRemote) {
            emit(Resource.Success(data = searchCachedProducts))
            return@flow
        }

        emit(Resource.Loading(data = searchCachedProducts))

        try {
            val searchNetworkProducts = canteenApi.searchProducts(query = query)
            if (searchNetworkProducts.isNotEmpty()) {
                productDao.insertProducts(products = searchNetworkProducts.map { it.mapToEntity() })
                return@flow emit(Resource.Success(data = searchNetworkProducts))
            }
            emit(Resource.Success(data = searchCachedProducts))
        } catch (e: IOException) {
            emit(
                Resource.Error(
                    message = networkError,
                    data = searchCachedProducts
                )
            )
        } catch (e: HttpException) {
            emit(
                Resource.Error(
                    message = serverError,
                    data = searchCachedProducts,
                )
            )
        }
    }
}