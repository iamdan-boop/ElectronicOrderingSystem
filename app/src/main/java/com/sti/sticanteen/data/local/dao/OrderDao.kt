package com.sti.sticanteen.data.local.dao

import androidx.room.*
import com.sti.sticanteen.data.local.entity.OrderEntity
import com.sti.sticanteen.data.local.entity.OrderItemEntity
import com.sti.sticanteen.data.local.entity.relations.OrderWithOrderItems


@Dao
interface OrderDao {

    @Transaction
    @Query("SELECT * FROM orders WHERE ownerId = :ownerId")
    suspend fun getOrders(ownerId: Long): List<OrderWithOrderItems>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(orders: List<OrderEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrderItems(orderItem: List<OrderItemEntity>)

    @Query("DELETE FROM orders WHERE ownerId = :ownerId")
    suspend fun deleteOrders(ownerId: Long)
}