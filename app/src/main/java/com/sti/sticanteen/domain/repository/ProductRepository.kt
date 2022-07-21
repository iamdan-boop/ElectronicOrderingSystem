package com.sti.sticanteen.domain.repository

import com.sti.sticanteen.data.network.entity.Product
import com.sti.sticanteen.utils.Resource
import kotlinx.coroutines.flow.Flow

interface ProductRepository {

    suspend fun getProducts(
        type: Int? = null,
    ): Flow<Resource<List<Product>>>

    suspend fun searchProducts(
        query: String,
        shouldSearchRemote: Boolean,
    ): Flow<Resource<List<Product>>>
}