package com.kyle.colaman.network

import java.security.cert.CertificateException
import javax.net.ssl.*
import okhttp3.OkHttpClient


/**
 *
 *     author : kyle
 *     time   : 2019/10/22
 *     desc   : 忽略ssl和证书的报错，在抓包的时候api会报错，替换掉原本的OKhttpclient
 *
 */

fun OkHttpClient.Builder.toUnsafe(): OkHttpClient.Builder {
    val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
        @Throws(CertificateException::class)
        override fun checkClientTrusted(
            chain: Array<java.security.cert.X509Certificate>,
            authType: String
        ) {
        }

        @Throws(CertificateException::class)
        override fun checkServerTrusted(
            chain: Array<java.security.cert.X509Certificate>,
            authType: String
        ) {
        }

        override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate> {
            return arrayOf()
        }
    })

    // Install the all-trusting trust manager
    val sslContext = SSLContext.getInstance("SSL")
    sslContext.init(null, trustAllCerts, java.security.SecureRandom())

    // Create an ssl socket factory with our all-trusting manager
    val sslSocketFactory = sslContext.socketFactory
    sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
    hostnameVerifier(HostnameVerifier { hostname, session -> true })
    return this

}