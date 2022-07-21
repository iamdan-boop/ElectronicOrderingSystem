package com.sti.sticanteen.data.mapper

import com.sti.sticanteen.data.local.entity.OrderItemEntity
import com.sti.sticanteen.data.network.entity.OrderItem


fun OrderItem.mapToEntity() : OrderItemEntity {
    return OrderItemEntity(
        orderItemId = id,
        orderId = orderId,
        productId = product.id,
        quantity = quantity,
    )
}