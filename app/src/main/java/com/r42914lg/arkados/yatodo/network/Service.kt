package com.r42914lg.arkados.yatodo.network

import android.app.Application
import com.r42914lg.arkados.yatodo.YaTodoApp.Companion.BASE_URL
import com.r42914lg.arkados.yatodo.utils.UserManager
import com.squareup.moshi.JsonClass
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.PATCH
import javax.inject.Inject
import javax.inject.Singleton

@JsonClass(generateAdapter = true)
data class TodoItemsContainer(val items: List<NetworkTotoItem>, val deviceId: String)

interface TodoService {
    @PATCH("items")
    suspend fun updateAll(@Body request: TodoItemsContainer): Response<List<NetworkTotoItem>>
}

@Singleton
class TodoNetwork @Inject constructor(
    private val authInterceptor: AuthInterceptor,
    private val app: Application
) {

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .client(okhttpClient())
        .build()

    private val _service: TodoService = retrofit.create(TodoService::class.java)
    val service: TodoService
        get() = _service

    private fun okhttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
//  for testing purposes uncomment and add certificate file to /resources -->
//            .hostnameVerifier { _, _ -> true } // ignore hostname verification for testing purposes
//            .sslSocketFactory(SslUtils.getSslContextForCertificateFile("mypublic.cert", app).socketFactory)
            .addInterceptor(authInterceptor)
            .build()
    }
}

@Singleton
class AuthInterceptor @Inject constructor(private val userManager: UserManager) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val requestBuilder = chain.request().newBuilder()
        userManager.token?.let {
            requestBuilder.addHeader("Authorization", "Bearer $it")
        }
        return chain.proceed(requestBuilder.build())
    }
}