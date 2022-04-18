package com.sti.canteen_ordering_app.infrastructure.network.repository

import com.sti.canteen_ordering_app.domain.mappers.toDomain
import com.sti.canteen_ordering_app.domain.mappers.toEntity
import com.sti.canteen_ordering_app.domain.models.Product
import com.sti.canteen_ordering_app.domain.repository.ProductRepository
import com.sti.canteen_ordering_app.infrastructure.local.dao.ProductDao
import com.sti.canteen_ordering_app.infrastructure.local.database.ProductDatabase
import com.sti.canteen_ordering_app.infrastructure.network.api.ProductApi
import com.sti.canteen_ordering_app.utils.Constants.networkError
import com.sti.canteen_ordering_app.utils.Constants.unknownError
import com.sti.canteen_ordering_app.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ProductRepositoryImpl @Inject constructor(
    private val productApi: ProductApi,
    private val productCacheDb: ProductDatabase,
) : ProductRepository {

    private val productDao: ProductDao = productCacheDb.productDao


    override suspend fun getProducts(): Flow<Resource<List<Product>>> = flow {
        emit(Resource.Loading(isLoading = true))
        val cachedProducts = productDao.getProducts().map { it.toDomain() }
        emit(Resource.Success(data = cachedProducts))

        try {
            val remoteProducts = productApi.getProducts().map { it.toEntity() }
            productDao.insertProducts(remoteProducts)

            val refetchCachedProducts = productDao.getProducts().map { it.toDomain() }
            emit(Resource.Success(data = refetchCachedProducts))
        } catch (e: IOException) {
            emit(
                Resource.Error(
                    message = networkError,
                    data = cachedProducts
                )
            )
        } catch (e: Exception) {
            emit(
                Resource.Error(
                    message = unknownError,
                    data = cachedProducts,
                )
            )
        }
    }

    override suspend fun searchProducts(
        query: String
    ): Flow<Resource<List<Product>>> = flow {
        emit(Resource.Loading(isLoading = true))
        if (query.isBlank()) return@flow emit(Resource.Loading(isLoading = false))

        val searchCachedProducts = productDao.searchProductList(query)
        if (searchCachedProducts.isNotEmpty()) {
            return@flow emit(Resource.Success(searchCachedProducts.map { it.toDomain() }))
        }

        try {
            val searchRemoteProducts = productApi.searchProducts(query)
            if (searchRemoteProducts.isEmpty()) {
                return@flow emit(Resource.Success(emptyList()))
            }

            productDao.insertProducts(searchRemoteProducts.map { it.toEntity() })
            emit(Resource.Success(data = searchRemoteProducts.map { it.toDomain() }))
        } catch (e: IOException) {
            emit(Resource.Error(networkError, data = searchCachedProducts.map { it.toDomain() }))
        } catch (e: Exception) {
            emit(Resource.Error(unknownError, data = searchCachedProducts.map { it.toDomain() }))
        }
    }
}