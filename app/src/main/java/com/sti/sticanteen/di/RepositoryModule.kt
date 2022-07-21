package com.sti.sticanteen.di

import com.sti.sticanteen.data.network.repository.CartRepositoryImpl
import com.sti.sticanteen.data.network.repository.CheckoutRepositoryImpl
import com.sti.sticanteen.data.network.repository.ProductRepositoryImpl
import com.sti.sticanteen.data.network.repository.TagRepositoryImpl
import com.sti.sticanteen.domain.repository.CartRepository
import com.sti.sticanteen.domain.repository.CheckoutRepository
import com.sti.sticanteen.domain.repository.ProductRepository
import com.sti.sticanteen.domain.repository.TagRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun provideProductRepository(
        productRepositoryImpl: ProductRepositoryImpl
    ): ProductRepository


    @Binds
    abstract fun provideCartRepository(
        cartRepositoryImpl: CartRepositoryImpl
    ): CartRepository


    @Binds
    abstract fun provideTagRepository(
        tagRepositoryImpl: TagRepositoryImpl
    ): TagRepository


    @Binds
    abstract fun provideCheckoutRepository(
        checkoutRepositoryImpl: CheckoutRepositoryImpl
    ): CheckoutRepository
}