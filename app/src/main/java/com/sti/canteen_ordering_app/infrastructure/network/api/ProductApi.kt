package com.sti.canteen_ordering_app.infrastructure.network.api

import com.sti.canteen_ordering_app.infrastructure.network.dto.ProductDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ProductApi {

    @GET("/")
    suspend fun getProducts(): List<ProductDto>

    @GET("/")
    suspend fun searchProducts(
        @Query("product") query: String
    ): List<ProductDto>
}