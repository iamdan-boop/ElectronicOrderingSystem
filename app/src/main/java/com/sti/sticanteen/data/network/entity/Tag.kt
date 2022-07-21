package com.sti.sticanteen.data.network.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Tag(
    val id: Long,
    val tag: String
) : Parcelable