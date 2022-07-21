package com.sti.sticanteen.data.mapper

import com.sti.sticanteen.data.local.entity.ProductEntity
import com.sti.sticanteen.data.network.entity.Media
import com.sti.sticanteen.data.network.entity.Product


fun Product.mapToEntity(): ProductEntity {
    return ProductEntity(
        productId = id.toLong(),
        productName = productName,
        price = price,
        type = type,
        imageUrl = imageUrl.first().fileName
    )
}


fun ProductEntity.mapToDomain(): Product {
    return Product(
        id = productId,
        productName = productName,
        price = price,
        type = type,
        imageUrl = listOf(Media(imageUrl))
    )
}