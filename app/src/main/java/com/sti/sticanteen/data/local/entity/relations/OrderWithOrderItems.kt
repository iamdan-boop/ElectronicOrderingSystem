package com.sti.sticanteen.data.local.entity.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.sti.sticanteen.data.local.entity.OrderEntity
import com.sti.sticanteen.data.local.entity.OrderItemEntity

data class OrderWithOrderItems(
    @Embedded val order: OrderEntity,
    @Relation(
        entity = OrderItemEntity::class,
        parentColumn = "orderId",
        entityColumn = "orderId",
    )
    val orderItems: List<OrderItemAndProduct>
)