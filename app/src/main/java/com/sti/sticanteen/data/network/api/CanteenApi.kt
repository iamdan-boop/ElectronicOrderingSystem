package com.sti.sticanteen.data.network.api

import com.sti.sticanteen.data.network.entity.*
import com.sti.sticanteen.data.network.entity.Tag
import com.sti.sticanteen.data.network.request.ProductRequest
import com.sti.sticanteen.data.network.request.RegisterRequest
import retrofit2.http.*

interface CanteenApi {

    @POST("register")
    suspend fun register(@Body request: RegisterRequest): AuthToken


    @FormUrlEncoded
    @POST("authenticate")
    suspend fun authenticate(
        @Field("email") email: String,
        @Field("password") password: String
    ): AuthToken

    @GET("me")
    suspend fun me(): AuthToken

    @POST("logout")
    suspend fun logout()


    // Products
    @GET("products/")
    suspend fun getProducts(
        @Query("type") type: Int? = null,
    ): List<Product>

    @GET("products/search")
    suspend fun searchProducts(
        @Query("query") query: String
    ): List<Product>

    // Tags
    @GET("tags")
    suspend fun getTags(): List<Tag>

    @FormUrlEncoded
    @GET("tags/search")
    suspend fun getProductsByTag(
        @Field("tags") tags: List<String>
    ): List<Product>


    // Cart
    @GET("cart/")
    suspend fun getCartProducts(): Cart

    @FormUrlEncoded
    @POST("cart/addToCart")
    suspend fun addProductToCart(
        @Field("productId") productId: Long,
    )

    @PUT("cart/removeToCart/{productId}")
    suspend fun removeProductToCart(@Path("productId") productId: Long)


    @FormUrlEncoded
    @POST("cart/checkout")
    suspend fun checkoutCartProducts(
        @Field("products") products: List<ProductRequest>
    ): ReferenceNumber


    // orders
    @GET("orders")
    suspend fun getOrders(): List<Order>
}