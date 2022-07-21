package com.sti.sticanteen.data.mapper

import com.sti.sticanteen.data.local.entity.TagEntity
import com.sti.sticanteen.data.network.entity.Tag


fun Tag.mapToEntity() : TagEntity {
    return TagEntity(
        tagId = id,
        tag = tag
    )
}


fun TagEntity.mapToDomain() : Tag {
    return Tag(
        id = tagId,
        tag = tag,
    )
}