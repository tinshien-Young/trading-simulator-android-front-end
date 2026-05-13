package com.example.mytradingsimulatorlite.data.network



//this file is of no use


/*
import okhttp3.Credentials
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    private const val BASE_URL = "http://10.0.2.2:8080/api/"
    // 10.0.2.2 是模拟器里访问本机的地址
    // 真机调试的话换成同学电脑的局域网 IP，比如 http://192.168.x.x:8080/api/

    private var username = ""
    private var password = ""

    // 登录后调用这个设置认证信息
    fun setCredentials(user: String, pass: String) {
        username = user
        password = pass
    }

    private fun buildClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .header("Authorization", Credentials.basic(username, password))
                    .build()
                chain.proceed(request)
            }
            .build()
    }

    fun getInstance(): ApiService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(buildClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}

 */