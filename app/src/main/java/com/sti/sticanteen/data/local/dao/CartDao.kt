package com.sti.sticanteen.data.local.dao

import androidx.room.*
import com.sti.sticanteen.data.local.entity.CartEntity
import com.sti.sticanteen.data.local.entity.relations.CartWithProducts
import com.sti.sticanteen.data.local.entity.relations.cross_refs.CartProductCrossRef


@Dao
interface CartDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCart(cart: CartEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartProduct(crossRef: CartProductCrossRef)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartProduct(crossRefs: List<CartProductCrossRef>)

    @Transaction
    @Query("SELECT * FROM carts WHERE ownerId = :ownerId LIMIT 1")
    suspend fun getCartProducts(ownerId: Long): CartWithProducts

    @Query("SELECT * FROM carts WHERE ownerId = :ownerId")
    suspend fun findCart(ownerId: Long): CartEntity

    @Query("SELECT * FROM carts")
    suspend fun getCarts(): List<CartEntity>

    @Transaction
    @Delete
    suspend fun deleteProductCart(cartProducts: CartProductCrossRef)

    @Transaction
    @Delete
    suspend fun deleteCartProducts(cartProducts: List<CartProductCrossRef>)
}