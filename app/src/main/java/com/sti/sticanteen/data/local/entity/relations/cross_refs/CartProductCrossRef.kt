package com.sti.sticanteen.data.local.entity.relations.cross_refs

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "cart_product",
    primaryKeys = ["productId", "cartId"],
)
data class CartProductCrossRef(
    val productId: Long,
    @ColumnInfo(index = true)
    val cartId: Long
)