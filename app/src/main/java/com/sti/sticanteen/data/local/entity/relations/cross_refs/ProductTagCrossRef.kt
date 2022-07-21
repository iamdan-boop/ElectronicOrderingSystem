package com.sti.sticanteen.data.local.entity.relations.cross_refs

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "product_tag",
    primaryKeys = ["productId", "tagId"]
)
data class ProductTagCrossRef(
    val productId: Long,
    @ColumnInfo(index = true)
    val tagId: Long,
)