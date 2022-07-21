package com.sti.sticanteen.domain.repository

import com.sti.sticanteen.data.network.entity.ReferenceNumber
import com.sti.sticanteen.presentation.dashboard.screens.cart.checkout.viewmodel.ProductCheckout
import com.sti.sticanteen.utils.Resource
import kotlinx.coroutines.flow.Flow

interface CheckoutRepository {

    suspend fun checkoutProducts(
        checkoutProducts: List<ProductCheckout>
    ): Flow<Resource<ReferenceNumber>>

    suspend fun payOrder(
        referenceNumber: ReferenceNumber
    ): Resource<Unit>


}