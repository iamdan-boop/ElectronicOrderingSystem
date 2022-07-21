package com.sti.sticanteen.data.network.repository

import com.sti.sticanteen.data.network.api.CanteenApi
import com.sti.sticanteen.data.network.entity.ReferenceNumber
import com.sti.sticanteen.data.network.request.ProductRequest
import com.sti.sticanteen.domain.repository.CheckoutRepository
import com.sti.sticanteen.presentation.dashboard.screens.cart.checkout.viewmodel.ProductCheckout
import com.sti.sticanteen.utils.Constants.networkError
import com.sti.sticanteen.utils.Constants.serverError
import com.sti.sticanteen.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class CheckoutRepositoryImpl @Inject constructor(
    private val canteenApi: CanteenApi,
) : CheckoutRepository {


    override suspend fun checkoutProducts(
        checkoutProducts: List<ProductCheckout>
    ): Flow<Resource<ReferenceNumber>> = flow {
        emit(Resource.Loading())
        try {
            val transformToRequest = checkoutProducts.map { checkoutProduct ->
                ProductRequest(
                    productId = checkoutProduct.product.id,
                    quantity = checkoutProduct.quantity
                )
            }
            val referenceNumber =
                canteenApi.checkoutCartProducts(products = transformToRequest)
            emit(Resource.Success(data = referenceNumber))
        } catch (e: IOException) {
            emit(
                Resource.Error(
                    message = e.message ?: networkError

                )
            )
        } catch (e: HttpException) {
            emit(
                Resource.Error(
                    message = e.message ?: serverError
                )
            )
        }
    }

    override suspend fun payOrder(referenceNumber: ReferenceNumber): Resource<Unit> {
        TODO("Not yet implemented")
    }
}