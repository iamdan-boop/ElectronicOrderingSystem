package com.sti.canteen_ordering_app.domain.repository

import com.sti.canteen_ordering_app.domain.models.Product
import com.sti.canteen_ordering_app.utils.Resource
import kotlinx.coroutines.flow.Flow

interface ProductRepository {

    suspend fun getProducts(): Flow<Resource<List<Product>>>

    suspend fun searchProducts(
        query: String
    ): Flow<Resource<List<Product>>>
}