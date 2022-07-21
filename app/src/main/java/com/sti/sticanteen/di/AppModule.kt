package com.sti.sticanteen.di

import android.content.Context
import android.content.SharedPreferences
import androidx.datastore.core.DataStore
import androidx.room.Room
import com.sti.sticanteen.AppUserPreference
import com.sti.sticanteen.data.local.StiCanteenDatabase
import com.sti.sticanteen.data.local.dao.CartDao
import com.sti.sticanteen.data.network.api.CanteenApi
import com.sti.sticanteen.data.network.repository.AuthenticationRepositoryImpl
import com.sti.sticanteen.di.DataStoreModule.appUserDataStore
import com.sti.sticanteen.domain.repository.AuthenticationRepository
import com.sti.sticanteen.utils.Constants.SHARED_PREFS_NAME
import com.sti.sticanteen.utils.SharedPrefWrapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Provider
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Singleton
    @Provides
    fun provideDatStore(@ApplicationContext context: Context): DataStore<AppUserPreference> {
        return context.appUserDataStore
    }

    @Singleton
    @Provides
    fun provideCanteenDatabase(
        @ApplicationContext context: Context
    ): StiCanteenDatabase {
        return Room.databaseBuilder(
            context,
            StiCanteenDatabase::class.java,
            "sti_canteen"
        ).fallbackToDestructiveMigration()
            .build()
    }


    @Singleton
    @Provides
    fun provideSharedPrefs(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
    }


    @Provides
    @Singleton
    fun provideSharedPrefWrapper(
        sharedPrefs: SharedPreferences
    ): SharedPrefWrapper {
        return SharedPrefWrapper(sharedPreferences = sharedPrefs)
    }


    @Provides
    @Singleton
    fun provideAuthRepository(
        canteenApi: Provider<CanteenApi>,
        sharedPrefsWrapper: SharedPrefWrapper,
        appDataStore: DataStore<AppUserPreference>,
        cartDao: CartDao,
    ): AuthenticationRepository {
        return AuthenticationRepositoryImpl(
            canteenApi = canteenApi,
            cartDao = cartDao,
            appDataStore = appDataStore,
            sharedPrefWrapper = sharedPrefsWrapper,
        )
    }
}