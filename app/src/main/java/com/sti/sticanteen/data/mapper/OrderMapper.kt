package com.sti.sticanteen.data.mapper

import com.sti.sticanteen.data.local.entity.OrderEntity
import com.sti.sticanteen.data.network.entity.Order



fun Order.mapToEntity() : OrderEntity {
    return OrderEntity(
        orderId = id,
        referenceNumber = referenceNumber,
        ownerId = userId,
    )
}


fun OrderEntity.mapToDomain() : Order {
    return Order(
        id = orderId,
        userId = ownerId,
        referenceNumber = referenceNumber
    )
}