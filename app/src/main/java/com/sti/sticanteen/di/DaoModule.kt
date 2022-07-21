package com.sti.sticanteen.di

import com.sti.sticanteen.data.local.StiCanteenDatabase
import com.sti.sticanteen.data.local.dao.CartDao
import com.sti.sticanteen.data.local.dao.ProductDao
import com.sti.sticanteen.data.local.dao.TagDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DaoModule {

    @Provides
    @Singleton
    fun provideProductDao(db: StiCanteenDatabase): ProductDao = db.productDao


    @Provides
    @Singleton
    fun provideCartDao(db: StiCanteenDatabase): CartDao = db.cartDao


    @Provides
    @Singleton
    fun provideTagDao(db: StiCanteenDatabase): TagDao = db.tagDao
}
