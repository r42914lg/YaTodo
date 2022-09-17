package com.r42914lg.arkados.yatodo.network

import android.app.Application
import com.r42914lg.arkados.yatodo.utils.SharedPrefManager
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.PATCH
import javax.inject.Inject
import javax.inject.Singleton

interface TodoService {
    @PATCH("items")
    suspend fun updateAll(@Body request: List<NetworkTotoItem>): Response<List<NetworkTotoItem>>
}

@Singleton
class TodoNetwork @Inject constructor(
    private val authInterceptor: AuthInterceptor,
    private val app: Application
) {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://10.0.2.2:8443/")
        .addConverterFactory(MoshiConverterFactory.create())
        .client(okhttpClient())
        .build()

    private val _service: TodoService = retrofit.create(TodoService::class.java)
    val service: TodoService
        get() = _service

    private fun okhttpClient(): OkHttpClient {
        val sslContext = SslUtils.getSslContextForCertificateFile("mypublic.cert", app)
        return OkHttpClient.Builder()
            .hostnameVerifier { _, _ -> true } // ignore hostname verification for testing purposes
            .addInterceptor(authInterceptor)
            .sslSocketFactory(sslContext.socketFactory)
            .build()
    }
}

@Singleton
class AuthInterceptor @Inject constructor(private val sharedPrefManager: SharedPrefManager) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val requestBuilder = chain.request().newBuilder()
        sharedPrefManager.token?.let {
            requestBuilder.addHeader("Authorization", "Bearer $it")
        }
        return chain.proceed(requestBuilder.build())
    }
}