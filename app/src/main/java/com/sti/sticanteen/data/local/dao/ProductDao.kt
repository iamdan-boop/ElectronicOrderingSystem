package com.sti.sticanteen.data.local.dao

import androidx.room.*
import com.sti.sticanteen.data.local.entity.ProductEntity
import com.sti.sticanteen.data.local.entity.relations.ProductWithTags


@Dao
interface ProductDao {

    @Transaction
    @Query("SELECT * FROM products")
    suspend fun getProducts(): List<ProductWithTags>

    @Transaction
    @Query("SELECT * FROM products WHERE type = :type")
    suspend fun getProducts(type: Int): List<ProductWithTags>

    @Query("SELECT * FROM products WHERE productName LIKE '%' || :query || '%'")
    suspend fun searchProduct(query: String): List<ProductEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(products: List<ProductEntity>)

    @Query("DELETE FROM products")
    suspend fun deleteProducts()
}