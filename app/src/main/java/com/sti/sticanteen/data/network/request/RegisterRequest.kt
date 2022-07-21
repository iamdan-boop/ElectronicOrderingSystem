package com.sti.sticanteen.data.network.request

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    val name: String,
    @SerializedName(value = "phone_number")
    val phoneNumber: String,
    val email: String,
    val password: String
)