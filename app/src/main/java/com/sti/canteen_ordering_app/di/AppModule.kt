package com.sti.canteen_ordering_app.di

import android.content.Context
import androidx.room.Room
import com.sti.canteen_ordering_app.domain.repository.ProductRepository
import com.sti.canteen_ordering_app.infrastructure.local.dao.ProductDao
import com.sti.canteen_ordering_app.infrastructure.local.database.ProductDatabase
import com.sti.canteen_ordering_app.infrastructure.network.api.ProductApi
import com.sti.canteen_ordering_app.infrastructure.network.repository.ProductRepositoryImpl
import com.sti.canteen_ordering_app.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun provideProductApi(): ProductApi {
        return Retrofit.Builder()
            .baseUrl(Constants.baseURL)
            .build()
            .create(ProductApi::class.java)
    }


    @Singleton
    @Provides
    fun provideProductRoomDatabase(
        @ApplicationContext context: Context
    ): ProductDatabase {
        return Room.databaseBuilder(
            context,
            ProductDatabase::class.java,
            "product_db"
        ).build()
    }

    @Singleton
    @Provides
    fun provideProductDao(productDatabase: ProductDatabase):
            ProductDao = productDatabase.productDao


    @Singleton
    @Provides
    fun provideProductRepository(
        productApi: ProductApi,
        productDatabase: ProductDatabase,
    ): ProductRepository {
        return ProductRepositoryImpl(
            productApi = productApi,
            productCacheDb = productDatabase,
        );
    }
}