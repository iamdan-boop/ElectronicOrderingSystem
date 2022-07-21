package com.sti.sticanteen.data.network.interceptor

import android.util.Log
import com.sti.sticanteen.domain.repository.AuthenticationRepository
import com.sti.sticanteen.utils.Constants.TOKEN_SHARED_PREFS_KEY
import com.sti.sticanteen.utils.SharedPrefWrapper
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Provider

const val AUTHENTICATE_ROUTE = "authenticate"
const val REGISTER_ROUTE = "register"
const val ME_ROUTE = "me"

class TokenInterceptor @Inject constructor(
    private val sharedPrefWrapper: SharedPrefWrapper,
    private val authenticationRepository: Provider<AuthenticationRepository>
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val authToken =
            sharedPrefWrapper.readKeyValueString(TOKEN_SHARED_PREFS_KEY) ?: chain.proceed(
                request
            )

        if (chain.request().url.pathSegments.last() == AUTHENTICATE_ROUTE ||
            chain.request().url.pathSegments.last() == REGISTER_ROUTE
        ) {
            return chain.proceed(request)
        }

        val authorizedRequest = request.newBuilder()
            .addHeader("Authorization", "Bearer $authToken")
            .build()
        Log.i("sendRequest", "${authorizedRequest.headers}")
        return chain.proceed(authorizedRequest)
            .also { response ->
                /* check for initial request for the app to recheck if
                * the token is still valid and invalidate it and replace a new
                * add guard check for returning 401 so it doesn't end up with infinite loop calling
                * the initial request("/me") unless were on different route
                * */
                if (response.request.url.pathSegments.last() == ME_ROUTE) {
                    return@also
                }
                if (response.code == 401) {
                    runBlocking {
                        authenticationRepository.get().requestLogout()
                    }
                }
            }
    }
}