package com.r42914lg.arkados.yatodo.network

import android.annotation.SuppressLint
import android.app.Application
import com.r42914lg.arkados.yatodo.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.tls.HandshakeCertificates
import okhttp3.tls.HeldCertificate
import java.net.InetAddress
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.inject.Inject
import javax.inject.Singleton
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

interface IOkHttpProvider {
    fun getOkHttpClient(): OkHttpClient
}

@Singleton
class OkHttpProd @Inject constructor(
    private val authInterceptor: AuthInterceptor): IOkHttpProvider {

    override fun getOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
                else HttpLoggingInterceptor.Level.NONE
            })
            .build()
}

@Singleton
class OkHttpTestNoAuth @Inject constructor(): IOkHttpProvider {

    override fun getOkHttpClient(): OkHttpClient {
        val clientCertificates: HandshakeCertificates = HandshakeCertificates.Builder()
            .addTrustedCertificate(CertificateWrapper.getLocalHostCert().certificate)
            .build()

        val okHttpClient = OkHttpClient.Builder()
            .sslSocketFactory(
                clientCertificates.sslSocketFactory(),
                clientCertificates.trustManager
            )
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }).build()
        return okHttpClient
    }
}

object CertificateWrapper {
    private val localhost: String = InetAddress.getByName("localhost").canonicalHostName
    private val localhostCertificate: HeldCertificate = HeldCertificate.Builder()
        .addSubjectAlternativeName(localhost)
        .build()

    fun getLocalHostCert() : HeldCertificate =
        localhostCertificate
}

@Singleton
class OkHttpLocallySignedCert @Inject constructor(
    private val authInterceptor: AuthInterceptor,
    private val app: Application): IOkHttpProvider {

    override fun getOkHttpClient(): OkHttpClient {

        val trustAllCerts: Array<TrustManager> = arrayOf(
            @SuppressLint("CustomX509TrustManager")
            object : X509TrustManager {
                @SuppressLint("TrustAllX509TrustManager")
                @Throws(CertificateException::class)
                override fun checkClientTrusted(chain: Array<X509Certificate?>?, authType: String?) {}
                @SuppressLint("TrustAllX509TrustManager")
                @Throws(CertificateException::class)
                override fun checkServerTrusted(chain: Array<X509Certificate?>?, authType: String?) {}
                override fun getAcceptedIssuers() : Array<X509Certificate> = arrayOf()
            }
        )

        return OkHttpClient.Builder()
            .hostnameVerifier { _, _ -> true }
            .sslSocketFactory(
                SslUtils.getSslContextForCertificateFile("mypublic.cert", app).socketFactory,
                trustAllCerts[0] as X509TrustManager
            )
            .addInterceptor(authInterceptor)
            .build()
    }
}