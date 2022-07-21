package com.sti.sticanteen.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey(autoGenerate = false)
    val orderId: Long,
    val ownerId: Long,
    val referenceNumber: String
)