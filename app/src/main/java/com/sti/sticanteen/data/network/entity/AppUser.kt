package com.sti.sticanteen.data.network.entity

import com.google.gson.annotations.SerializedName

data class AppUser(
    val id: Long,
    val email: String,
    val name: String,
    @SerializedName("phone_number")
    val phoneNumber: String,
    val cart: Cart
)