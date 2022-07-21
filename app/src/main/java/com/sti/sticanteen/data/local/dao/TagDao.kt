package com.sti.sticanteen.data.local.dao

import androidx.room.*
import com.sti.sticanteen.data.local.entity.TagEntity
import com.sti.sticanteen.data.local.entity.relations.TagWithProducts
import com.sti.sticanteen.data.local.entity.relations.cross_refs.ProductTagCrossRef

@Dao
interface TagDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTags(tags: List<TagEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTagProduct(crossRefs: List<ProductTagCrossRef>)

    @Query("SELECT * FROM tags")
    suspend fun getTags(): List<TagEntity>

    @Transaction
    @Query("SELECT * FROM tags WHERE tagId IN (:tags)")
    suspend fun getProductByTags(tags: List<Long>): TagWithProducts

    @Query("DELETE FROM product_tag")
    suspend fun deleteProductTag()

    @Delete
    suspend fun deleteTags(tags: List<TagEntity>)

    @Query("DELETE FROM tags")
    suspend fun deleteTags()
}