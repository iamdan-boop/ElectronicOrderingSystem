package com.sti.sticanteen.data.network.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    val id: Long,
    val productName: String,
    val price: Double,
    val type: Int,
    val imageUrl: List<Media> = emptyList(),
    val tags: List<Tag>? = emptyList()
) : Parcelable {
    companion object {
        const val PRODUCT_BEVERAGES = "Beverages"
        const val PRODUCT_DRINKS = "Drinks"
        const val PRODUCT_SNACKS = "Snacks"
    }
}


fun Product.getProductType(): String {
    if (type == 1) return Product.PRODUCT_DRINKS
    if (type == 2) return Product.PRODUCT_BEVERAGES
    return Product.PRODUCT_SNACKS
}