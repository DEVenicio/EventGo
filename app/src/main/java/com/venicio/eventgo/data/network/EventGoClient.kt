package com.venicio.eventgo.data.network

import okhttp3.OkHttpClient
import org.conscrypt.Conscrypt
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.security.Security
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.X509TrustManager


object EventGoClient {
    private val okHttpClient: OkHttpClient

    init {
        Security.insertProviderAt(Conscrypt.newProvider(), 1)

        val trustManager = object : X509TrustManager {
            override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {
                // Aceita todos os certificados de clientes
            }

            override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
                // Aceita todos os certificados do servidor
            }

            override fun getAcceptedIssuers(): Array<X509Certificate> {
                return emptyArray()
            }
        }

        val sslContext = SSLContext.getInstance("TLSv1.2", Conscrypt.newProvider())
        sslContext.init(null, arrayOf(trustManager), SecureRandom())

        okHttpClient = OkHttpClient.Builder()
            .sslSocketFactory(sslContext.socketFactory, trustManager)
            .build()
    }

    fun getInstanceRetrofit(base: String): EventGoService {
        val retrofit = Retrofit.Builder()
            .baseUrl(base)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

        return retrofit.create(EventGoService::class.java)
    }

}