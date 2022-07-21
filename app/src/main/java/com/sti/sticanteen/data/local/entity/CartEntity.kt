package com.sti.sticanteen.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "carts")
data class CartEntity(
    @PrimaryKey(autoGenerate = false)
    val cartId: Long,
    val ownerId: Long
)