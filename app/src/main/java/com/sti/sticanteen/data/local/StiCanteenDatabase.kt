package com.sti.sticanteen.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sti.sticanteen.data.local.dao.CartDao
import com.sti.sticanteen.data.local.dao.OrderDao
import com.sti.sticanteen.data.local.dao.ProductDao
import com.sti.sticanteen.data.local.dao.TagDao
import com.sti.sticanteen.data.local.entity.*
import com.sti.sticanteen.data.local.entity.relations.cross_refs.CartProductCrossRef
import com.sti.sticanteen.data.local.entity.relations.cross_refs.ProductTagCrossRef

@Database(
    entities = [
        ProductEntity::class,
        CartEntity::class,
        TagEntity::class,
        OrderEntity::class,
        OrderItemEntity::class,
        CartProductCrossRef::class,
        ProductTagCrossRef::class,
    ],
    version = 8,
    exportSchema = false
)
abstract class StiCanteenDatabase : RoomDatabase() {
    abstract val productDao: ProductDao
    abstract val cartDao: CartDao
    abstract val tagDao: TagDao
    abstract val orderDao: OrderDao
}