package com.sti.sticanteen.domain.repository

import com.sti.sticanteen.data.network.entity.Product
import com.sti.sticanteen.data.network.entity.Tag
import com.sti.sticanteen.utils.Resource
import kotlinx.coroutines.flow.Flow

interface TagRepository {

    suspend fun getTags(): Flow<Resource<List<Tag>>>

    suspend fun getProductsByTags(tags: List<Tag>): Flow<Resource<List<Product>>>
}