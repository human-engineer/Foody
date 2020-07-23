package com.humansuit.foody.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object RecipesApiFactory {

    private const val RECIPES_BASE_URL = "https://api.spoonacular.com/recipes/"

    fun makeRetrofitService(): RecipesService {
        return Retrofit.Builder()
            .baseUrl(RECIPES_BASE_URL)
            .client(makeOkHttpClient())
            .addConverterFactory(MoshiConverterFactory.create())
            .build().create(RecipesService::class.java)
    }


    private fun makeOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(makeLoggingInterceptor())
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(90, TimeUnit.SECONDS)
            .build()
    }


    private fun makeLoggingInterceptor()
            = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
            return this
        }

}




