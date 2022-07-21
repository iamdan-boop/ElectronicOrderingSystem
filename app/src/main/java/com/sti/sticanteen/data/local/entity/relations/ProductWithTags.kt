package com.sti.sticanteen.data.local.entity.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.sti.sticanteen.data.local.entity.ProductEntity
import com.sti.sticanteen.data.local.entity.TagEntity
import com.sti.sticanteen.data.local.entity.relations.cross_refs.ProductTagCrossRef

data class ProductWithTags(
    @Embedded val product: ProductEntity,
    @Relation(
        parentColumn = "productId",
        entityColumn = "tagId",
        associateBy = Junction(ProductTagCrossRef::class)
    )
    val tags: List<TagEntity>
)