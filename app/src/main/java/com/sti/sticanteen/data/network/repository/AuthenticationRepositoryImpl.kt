package com.sti.sticanteen.data.network.repository

import android.util.Log
import androidx.datastore.core.DataStore
import com.sti.sticanteen.AppUserPreference
import com.sti.sticanteen.data.local.dao.CartDao
import com.sti.sticanteen.data.local.entity.CartEntity
import com.sti.sticanteen.data.network.api.CanteenApi
import com.sti.sticanteen.data.network.entity.AppUser
import com.sti.sticanteen.data.network.request.RegisterRequest
import com.sti.sticanteen.domain.repository.AuthenticationRepository
import com.sti.sticanteen.utils.AuthState
import com.sti.sticanteen.utils.AuthenticationState
import com.sti.sticanteen.utils.Constants.TOKEN_SHARED_PREFS_KEY
import com.sti.sticanteen.utils.SharedPrefWrapper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Singleton
class AuthenticationRepositoryImpl @Inject constructor(
    private val canteenApi: Provider<CanteenApi>,
    private val sharedPrefWrapper: SharedPrefWrapper,
    private val cartDao: CartDao,
    private val appDataStore: DataStore<AppUserPreference>,
) : AuthenticationRepository {

    private val _isLoggedIn = MutableStateFlow(AuthenticationState.UNKNOWN)
    override val isLoggedIn: StateFlow<AuthenticationState> = _isLoggedIn

    override suspend fun authenticate(
        email: String,
        password: String
    ): AuthState<Unit> {
        return try {
            val (authToken, user) = canteenApi.get().authenticate(
                email = email,
                password = password
            )
            sharedPrefWrapper.storeKeyValue(
                TOKEN_SHARED_PREFS_KEY,
                authToken
            )


            addToDataStore(user)
            cartDao.insertCart(
                cart = CartEntity(
                    cartId = user.cart.id.toLong(),
                    ownerId = user.id.toLong()
                )
            )
            _isLoggedIn.emit(AuthenticationState.LOGGED_IN)
            AuthState.Authorized()
        } catch (e: HttpException) {
            if (e.code() == 401) {
                AuthState.Unauthorized()
            } else AuthState.UnknownError()
        }
    }

    override suspend fun register(request: RegisterRequest): AuthState<Unit> {
        return try {
            val (authToken, user) = canteenApi.get().register(request = request)
            sharedPrefWrapper.storeKeyValue(
                TOKEN_SHARED_PREFS_KEY,
                authToken
            )
            addToDataStore(user)
            cartDao.insertCart(
                cart = CartEntity(
                    cartId = user.cart.id,
                    ownerId = user.id
                )
            )
            _isLoggedIn.emit(AuthenticationState.LOGGED_IN)
            AuthState.Authorized()
        } catch (e: HttpException) {
            AuthState.UnknownError()
        }
    }

    override suspend fun authCheck(): AuthState<Unit> {
        return try {
            val (authToken, user) = canteenApi.get().me()
            sharedPrefWrapper.storeKeyValue(
                TOKEN_SHARED_PREFS_KEY,
                authToken
            )

            addToDataStore(user)
            cartDao.insertCart(
                cart = CartEntity(
                    cartId = user.cart.id,
                    ownerId = user.id
                )
            )
            _isLoggedIn.emit(AuthenticationState.LOGGED_IN)
            AuthState.Authorized()
        } catch (e: HttpException) {
            if (e.code() == 401) {
                _isLoggedIn.emit(AuthenticationState.UNAUTHORIZED)
                AuthState.Unauthorized()
            } else AuthState.UnknownError()
        }
    }

    override suspend fun requestLogout() {
        Log.i("called", "calledLogout")
        sharedPrefWrapper.clearValues()
        appDataStore.updateData { appUser ->
            appUser.toBuilder().clear().build()
        }
        _isLoggedIn.emit(AuthenticationState.UNAUTHORIZED)
    }


    private suspend fun addToDataStore(user: AppUser) {
        Log.i("appUser", "$user")
        appDataStore.updateData { appUser ->
            appUser.toBuilder()
                .setId(user.id.toInt())
                .setEmail(user.email)
                .setName(user.name)
                .setPhoneNumber(user.phoneNumber)
                .setCartId(user.cart.id.toInt())
                .build()
        }
    }
}