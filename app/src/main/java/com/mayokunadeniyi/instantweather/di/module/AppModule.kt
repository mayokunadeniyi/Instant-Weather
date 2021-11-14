package com.mayokunadeniyi.instantweather.di.module

import android.app.Application
import android.content.Context
import com.google.gson.Gson
import com.mayokunadeniyi.instantweather.BuildConfig
import com.mayokunadeniyi.instantweather.utils.LocationLiveData
import com.mayokunadeniyi.instantweather.utils.SharedPreferenceHelper
import com.readystatesoftware.chuck.ChuckInterceptor
import dagger.Lazy
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Created by Mayokun Adeniyi on 1/18/21.
 */

@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Provides
    @Singleton
    fun provideContext(application: Application): Context {
        return application.applicationContext
    }

    @Provides
    @Singleton
    fun provideSharedPreferencesHelper(context: Context): SharedPreferenceHelper {
        return SharedPreferenceHelper.getInstance(context)
    }

    @Provides
    @Singleton
    fun provideLocationLiveData(context: Context): LocationLiveData {
        return LocationLiveData(context)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(interceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(interceptor).build()
    }

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
    }

    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()

    @Provides
    @Singleton
    fun provideGsonConverterFactory(gson: Gson): GsonConverterFactory {
        return GsonConverterFactory.create(gson)
    }

    @Provides
    @Singleton
    fun provideRetrofitBuilder(
        client: Lazy<OkHttpClient>,
        converterFactory: GsonConverterFactory,
        context: Context
    ): Retrofit {
        val retrofitBuilder = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(client.get())
            .addConverterFactory(converterFactory)

        val okHttpClientBuilder = OkHttpClient.Builder()
            .addInterceptor { chain ->

                val original = chain.request()
                val originalHttpUrl = original.url

                val url = originalHttpUrl.newBuilder()
                    .addQueryParameter("appid", BuildConfig.API_KEY)
                    .build()

                Timber.d("Started making network call")

                val requestBuilder = original.newBuilder()
                    .url(url)

                val request = requestBuilder.build()
                return@addInterceptor chain.proceed(request)
            }
            .readTimeout(60, TimeUnit.SECONDS)
        if (BuildConfig.DEBUG) {
            okHttpClientBuilder.addInterceptor(ChuckInterceptor(context))
        }
        return retrofitBuilder.client(okHttpClientBuilder.build()).build()
    }
}
