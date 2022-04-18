package com.sti.canteen_ordering_app.domain.mappers

import com.sti.canteen_ordering_app.domain.models.Product
import com.sti.canteen_ordering_app.infrastructure.local.entity.ProductEntity
import com.sti.canteen_ordering_app.infrastructure.network.dto.ProductDto

fun ProductEntity.toDomain(): Product {
    return Product(
        id = id!!,
        productName = productName,
        price = price,
        imageUrl = imageUrl,
    );
}


fun Product.toEntity(): ProductEntity {
    return ProductEntity(
        productName = productName,
        price = price,
        imageUrl = imageUrl,
    );
}


fun ProductDto.toEntity(): ProductEntity {
    return ProductEntity(
        id = id,
        productName = productName,
        price = price,
        imageUrl = imageUrl,
    )
}


fun ProductDto.toDomain(): Product {
    return Product(
        id = id,
        productName = productName,
        price = price,
        imageUrl = imageUrl,
    )
}