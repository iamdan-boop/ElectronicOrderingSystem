package com.sti.sticanteen.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tags")
data class TagEntity(
    @PrimaryKey(autoGenerate = false)
    val tagId: Long,
    val tag: String,
)