package com.sti.sticanteen.data.network.entity

import com.google.gson.annotations.SerializedName

data class Order(
    val id: Long,
    @SerializedName("user_id")
    val userId: Long,
    @SerializedName("reference_number")
    val referenceNumber: String,
    val items: List<OrderItem> = emptyList()
)