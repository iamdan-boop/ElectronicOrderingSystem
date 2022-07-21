package com.sti.sticanteen.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "order_items")
data class OrderItemEntity(
    @PrimaryKey(autoGenerate = false)
    val orderItemId: Long,
    val quantity: Int,
    val orderId: Long,
    val productId: Long,
)