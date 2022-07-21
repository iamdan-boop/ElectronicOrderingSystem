package com.sti.sticanteen.data.network.entity

import com.google.gson.annotations.SerializedName

data class ReferenceNumber(
    @SerializedName("reference_number")
    val referenceNumber: String,
)