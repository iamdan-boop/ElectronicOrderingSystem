package com.sti.canteen_ordering_app.infrastructure.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sti.canteen_ordering_app.infrastructure.local.entity.ProductEntity

@Dao
interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(products: List<ProductEntity>)

    @Query("SELECT * FROM products")
    suspend fun getProducts(): List<ProductEntity>


    @Query("DELETE FROM products")
    suspend fun clearProducts()

    @Query(
        """
        SELECT * FROM products
        WHERE LOWER(productName) LIKE '%' || LOWER(:query) || '%'
    """
    )
    suspend fun searchProductList(query: String): List<ProductEntity>
}