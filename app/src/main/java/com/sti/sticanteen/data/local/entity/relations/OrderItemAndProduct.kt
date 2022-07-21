package com.sti.sticanteen.data.local.entity.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.sti.sticanteen.data.local.entity.OrderItemEntity
import com.sti.sticanteen.data.local.entity.ProductEntity

data class OrderItemAndProduct(
    @Embedded
    val orderItem: OrderItemEntity,
    @Relation(
        parentColumn = "productId",
        entityColumn = "productId"
    )
    val product: ProductEntity
)