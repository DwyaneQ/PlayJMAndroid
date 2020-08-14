package com.dwyaneq.playandroidkotlin.common.network

import com.dwyaneq.jetpack_mvvm_base.network.interceptor.logging.LogInterceptor
import com.dwyaneq.playandroidkotlin.MApplication
import com.dwyaneq.playandroidkotlin.common.config.AppConfig
import com.dwyaneq.playandroidkotlin.service.NetworkApiService
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import com.google.gson.GsonBuilder
import me.hgj.jetpackmvvm.network.BaseNetworkApi
import com.dwyaneq.jetpack_mvvm_base.network.CoroutineCallAdapterFactory
import me.hgj.jetpackmvvm.network.interceptor.CacheInterceptor
import com.dwyaneq.jetpack_mvvm_base.network.interceptor.HeadInterceptor
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * Created by DWQ on 2020/4/28.
 * E-Mail:lomapa@163.com
 * 可添加拦截器
 */
class NetworkApi : BaseNetworkApi() {
    val service: NetworkApiService by lazy {
        getApi(NetworkApiService::class.java, AppConfig.BASE_URL)
    }

    val cookieJar: PersistentCookieJar by lazy {
        PersistentCookieJar(SetCookieCache(), SharedPrefsCookiePersistor(MApplication.instance))
    }

    override fun setHttpClientBuilder(builder: OkHttpClient.Builder): OkHttpClient.Builder {
        builder.apply {
            //设置缓存配置 缓存最大10M
            cache(
                Cache(
                    File(MApplication.instance.cacheDir, "WJMAndroid_cache"),
                    AppConfig.CACHE_SIZE
                )
            )
            // 添加cookie自动持  久化mk
            cookieJar(cookieJar)
            //添加公共heads 注意要设置在日志拦截器之前，不然Log中会不显示head信息
            addInterceptor(HeadInterceptor(mapOf()))
            //添加缓存拦截器 可传入缓存天数，不传默认7天
            addInterceptor(CacheInterceptor())
            // 日志拦截器
            addInterceptor(LogInterceptor())
            //超时时间 连接、读、写
            connectTimeout(AppConfig.HTTP_CONNECTION_TIME_OUT, TimeUnit.SECONDS)
            readTimeout(AppConfig.HTTP_READ_TIME_OUT, TimeUnit.SECONDS)
            writeTimeout(AppConfig.HTTP_WRITE_TIME_OUT, TimeUnit.SECONDS)
        }
        return builder
    }

    /**
     * 实现重写父类的setRetrofitBuilder方法，
     * 在这里可以对Retrofit.Builder做任意操作，比如添加GSON解析器，protobuf等
     */
    override fun setRetrofitBuilder(builder: Retrofit.Builder): Retrofit.Builder {
        return builder.apply {
            addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            addCallAdapterFactory(CoroutineCallAdapterFactory())
        }
    }
}