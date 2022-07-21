package com.sti.sticanteen.domain.repository

import com.sti.sticanteen.data.network.entity.Product
import com.sti.sticanteen.utils.Resource
import kotlinx.coroutines.flow.Flow

interface CartRepository {

    suspend fun addProductToCart(productId: Long): Flow<Resource<Unit>>

    suspend fun removeProductToCart(productId: Long)

    suspend fun getProducts(): Flow<Resource<List<Product>>>

    suspend fun checkoutProducts(
        products: List<Product>
    ): Flow<Resource<Unit>>

}
