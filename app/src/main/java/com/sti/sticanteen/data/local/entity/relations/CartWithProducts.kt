package com.sti.sticanteen.data.local.entity.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.sti.sticanteen.data.local.entity.CartEntity
import com.sti.sticanteen.data.local.entity.ProductEntity
import com.sti.sticanteen.data.local.entity.relations.cross_refs.CartProductCrossRef

data class CartWithProducts(
    @Embedded val cart: CartEntity,
    @Relation(
        parentColumn = "cartId",
        entityColumn = "productId",
        associateBy = Junction(CartProductCrossRef::class)
    )
    val products: List<ProductEntity>
)