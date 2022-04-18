package com.sti.canteen_ordering_app.infrastructure.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sti.canteen_ordering_app.infrastructure.local.dao.ProductDao
import com.sti.canteen_ordering_app.infrastructure.local.entity.ProductEntity

@Database(
    entities = [ProductEntity::class],
    version = 1
)
abstract class ProductDatabase : RoomDatabase() {
    abstract val productDao: ProductDao
}