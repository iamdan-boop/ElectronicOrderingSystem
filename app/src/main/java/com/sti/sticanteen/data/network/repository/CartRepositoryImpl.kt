package com.sti.sticanteen.data.network.repository

import android.util.Log
import androidx.datastore.core.DataStore
import com.sti.sticanteen.AppUserPreference
import com.sti.sticanteen.data.local.dao.CartDao
import com.sti.sticanteen.data.local.entity.relations.cross_refs.CartProductCrossRef
import com.sti.sticanteen.data.mapper.mapToDomain
import com.sti.sticanteen.data.network.api.CanteenApi
import com.sti.sticanteen.data.network.entity.Product
import com.sti.sticanteen.data.network.request.CheckoutRequest
import com.sti.sticanteen.data.network.request.ProductRequest
import com.sti.sticanteen.domain.repository.CartRepository
import com.sti.sticanteen.utils.Constants.networkError
import com.sti.sticanteen.utils.Constants.serverError
import com.sti.sticanteen.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val canteenApi: CanteenApi,
    private val appDataStore: DataStore<AppUserPreference>,
    private val cartDao: CartDao
) : CartRepository {


    override suspend fun addProductToCart(
        productId: Long,
    ): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        try {
            val appUser = appDataStore.data.first()
            canteenApi.addProductToCart(productId)
            val fetchRemoteCartProducts = canteenApi.getCartProducts()
            if (fetchRemoteCartProducts.products.isEmpty()) {
                val findAndTransformCachedCartProducts =
                    cartDao.getCartProducts(appUser.cartId.toLong())
                        .products.map { product ->
                            CartProductCrossRef(
                                cartId = appUser.cartId.toLong(),
                                productId = product.productId
                            )
                        }
                cartDao.deleteCartProducts(cartProducts = findAndTransformCachedCartProducts)
                return@flow emit(Resource.Success(Unit))
            }

            cartDao.insertCartProduct(
                crossRef = CartProductCrossRef(
                    productId = productId,
                    cartId = appUser.cartId.toLong()
                )
            )
            val transformRemoteProductsToCrossRef =
                fetchRemoteCartProducts.products.map { product ->
                    CartProductCrossRef(
                        cartId = appUser.cartId.toLong(),
                        productId = product.id,
                    )
                }
            cartDao.insertCartProduct(crossRefs = transformRemoteProductsToCrossRef)
            emit(Resource.Success(Unit))
        } catch (e: HttpException) {
            emit(Resource.Error(message = e.message ?: serverError))
        } catch (e: IOException) {
            emit(Resource.Error(message = e.message ?: networkError))
        }
    }

    override suspend fun removeProductToCart(productId: Long) {

        val cartId = appDataStore.data.first().cartId
        cartDao.deleteProductCart(
            CartProductCrossRef(
                cartId = cartId.toLong(),
                productId = productId,
            )
        )

        try {
            canteenApi.removeProductToCart(productId)
            val fetchUpdatedRemoteProducts = canteenApi.getCartProducts()
            if (fetchUpdatedRemoteProducts.products.isEmpty()) {
                val findCachedCartProducts =
                    cartDao.getCartProducts(appDataStore.data.first().id.toLong()).products.map { product ->
                        CartProductCrossRef(
                            cartId = cartId.toLong(),
                            productId = product.productId
                        )
                    }
                cartDao.deleteCartProducts(findCachedCartProducts)
                return
            }

            val mapToCartProductCrossRef = fetchUpdatedRemoteProducts.products.map { product ->
                CartProductCrossRef(cartId = cartId.toLong(), productId = product.id)
            }
            cartDao.insertCartProduct(crossRefs = mapToCartProductCrossRef)
        } catch (e: HttpException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override suspend fun getProducts(): Flow<Resource<List<Product>>> = flow {
        emit(Resource.Loading())

        val findCachedCartProducts =
            cartDao.getCartProducts(appDataStore.data.first().id.toLong())
        Log.i("findCachedProducts", "$findCachedCartProducts")

        val transformToDomainProducts = findCachedCartProducts.products.map { cachedProduct ->
            cachedProduct.mapToDomain()
        }

        if (transformToDomainProducts.isNotEmpty()) {
            emit(
                Resource.Success(data = transformToDomainProducts)
            )
        }

        try {
            val fetchRemoteCartProducts = canteenApi.getCartProducts()
            Log.i(TAG, "$fetchRemoteCartProducts")
            if (fetchRemoteCartProducts.products.isEmpty()) {
                val cachedProductsToCrossRef = findCachedCartProducts.products.map { product ->
                    CartProductCrossRef(
                        cartId = findCachedCartProducts.cart.cartId,
                        productId = product.productId,
                    )
                }
                cartDao.deleteCartProducts(cachedProductsToCrossRef)
                return@flow emit(Resource.Success(data = emptyList()))
            }

            val remoteCartProductsToCrossRef =
                fetchRemoteCartProducts.products.map { remoteProduct ->
                    CartProductCrossRef(
                        cartId = findCachedCartProducts.cart.cartId,
                        productId = remoteProduct.id,
                    )
                }
            cartDao.insertCartProduct(remoteCartProductsToCrossRef)
            Log.i("fetchRemoteProducts", "$fetchRemoteCartProducts")
            emit(Resource.Success(data = fetchRemoteCartProducts.products))
        } catch (e: HttpException) {
            emit(
                Resource.Error(
                    data = transformToDomainProducts,
                    message = e.message ?: serverError,
                )
            )
        } catch (e: IOException) {
            emit(
                Resource.Error(
                    data = transformToDomainProducts,
                    message = e.message ?: networkError,
                )
            )
        }
    }

    override suspend fun checkoutProducts(
        products: List<Product>
    ): Flow<Resource<Unit>> = flow {
//        emit(Resource.Loading(Unit))
//
//        val transformToRequest = CheckoutRequest(
//            products = products.map { cartProduct ->
//                ProductRequest(
//                    productId = cartProduct.id,
//                )
//            }
//        )
//
//        try {
//            canteenApi.checkoutCartProducts(transformToRequest)
//            val toRemoveInCartCache = transformToRequest.products.map { productRequest ->
//                CartProductCrossRef(
//                    cartId = appDataStore.data.first().cartId.toLong(),
//                    productId = productRequest.productId
//                )
//            }
//            cartDao.deleteCartProducts(cartProducts = toRemoveInCartCache)
//            return@flow emit(Resource.Success(data = Unit))
//        } catch (e: HttpException) {
//            emit(
//                Resource.Error(
//                    message = e.message ?: serverError
//                )
//            )
//        } catch (e: IOException) {
//            emit(
//                Resource.Error(
//                    message = e.message ?: networkError
//                )
//            )
//        }
    }


    companion object {
        const val TAG = "CART_REPOSITORY"
    }
}