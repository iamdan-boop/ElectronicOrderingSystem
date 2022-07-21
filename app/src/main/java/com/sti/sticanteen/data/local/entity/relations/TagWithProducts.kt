package com.sti.sticanteen.data.local.entity.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.sti.sticanteen.data.local.entity.ProductEntity
import com.sti.sticanteen.data.local.entity.TagEntity
import com.sti.sticanteen.data.local.entity.relations.cross_refs.ProductTagCrossRef

data class TagWithProducts(
    @Embedded val tag: TagEntity,
    @Relation(
        parentColumn = "tagId",
        entityColumn = "productId",
        associateBy = Junction(ProductTagCrossRef::class)
    )
    val products: List<ProductEntity>
)

