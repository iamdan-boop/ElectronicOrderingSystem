package com.sti.sticanteen.di

import com.sti.sticanteen.data.network.api.CanteenApi
import com.sti.sticanteen.data.network.interceptor.TokenInterceptor
import com.sti.sticanteen.domain.repository.AuthenticationRepository
import com.sti.sticanteen.utils.Constants.apiUrl
import com.sti.sticanteen.utils.SharedPrefWrapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Provider
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object ApiModule {


    @Provides
    @Singleton
    fun provideOkHttp(
        sharedPrefsWrapper: SharedPrefWrapper,
        authenticationRepository: Provider<AuthenticationRepository>,
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(1, TimeUnit.MINUTES)
            .addInterceptor(
                TokenInterceptor(
                    sharedPrefWrapper = sharedPrefsWrapper,
                    authenticationRepository = authenticationRepository,
                )
            )
            .addInterceptor(
                HttpLoggingInterceptor(HttpLoggingInterceptor.Logger.DEFAULT).setLevel(
                    HttpLoggingInterceptor.Level.BODY
                )
            )
            .build()
    }

    @Provides
    @Singleton
    fun provideCanteenApi(
        okHttpClient: OkHttpClient
    ): CanteenApi {
        return Retrofit.Builder()
            .baseUrl(apiUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CanteenApi::class.java)
    }

}