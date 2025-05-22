package com.example.leblanc_lepere_android_project.model

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import com.example.leblanc_lepere_android_project.model.ProductService

data class Product(
    val id: Int,
    val title: String,
    val price: Float,
    val description: String,
    val category: String,
    val image: String
) {
    companion object Utils {
        suspend fun getProductById(id: Int): Product {
            val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl("https://fakestoreapi.com/")
                .addConverterFactory(MoshiConverterFactory.create())
                .client(client)
                .build()

            val productService = retrofit.create(ProductService::class.java)

            return productService.getProductById(id)
        }
        suspend fun getAllProducts(): List<Product> {
            val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl("https://fakestoreapi.com/")
                .addConverterFactory(MoshiConverterFactory.create())
                .client(client)
                .build()

            val productService = retrofit.create(ProductService::class.java)

            return productService.getAllProducts()
        }

    }
}

