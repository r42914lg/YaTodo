package com.r42914lg.arkados.yatodo.network

import android.annotation.SuppressLint
import android.app.Application
import com.r42914lg.arkados.yatodo.BuildConfig
import com.r42914lg.arkados.yatodo.IMyApp
import com.r42914lg.arkados.yatodo.utils.IUserManager
import com.squareup.moshi.JsonClass
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.tls.HandshakeCertificates
import okhttp3.tls.HeldCertificate
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.PATCH
import java.net.InetAddress
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
    okHttpProvider: IOkHttpProvider,
    app: Application
) {

    private val retrofit = Retrofit.Builder()
        .baseUrl((app as IMyApp).baseUrl)
        .addConverterFactory(MoshiConverterFactory.create())
        .client(okHttpProvider.getOkHttpClient())
        .build()

    private val _service: TodoService = retrofit.create(TodoService::class.java)
    val service: TodoService
        get() = _service
}

@Singleton
class AuthInterceptor @Inject constructor(private val userManager: IUserManager) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val requestBuilder = chain.request().newBuilder()
        userManager.token?.let {
            requestBuilder.addHeader("Authorization", "Bearer $it")
        }
        return chain.proceed(requestBuilder.build())
    }
}