package com.sti.sticanteen.domain.repository

import com.sti.sticanteen.data.network.entity.Order
import com.sti.sticanteen.utils.Resource
import kotlinx.coroutines.flow.Flow

interface OrderRepository {

    suspend fun getOrders() : Flow<Resource<List<Order>>>
}