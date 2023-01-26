package com.example.newsapp.data.datasource.remote

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()


class RetrofitProvider {
    companion object {
        fun getRetrofit(baseUrl: String) : Retrofit {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .baseUrl(baseUrl)
                .build()
            return retrofit
        }
    }
}