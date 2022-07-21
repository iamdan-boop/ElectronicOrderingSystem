package com.sti.sticanteen.data.network.entity

import com.google.gson.annotations.SerializedName

data class OrderItem(
    val id: Long,
    val product: Product,
    @SerializedName("order_id")
    val orderId: Long,
    val quantity: Int,
)