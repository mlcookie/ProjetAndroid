package com.example.leblanc_lepere_android_project.model

import  com.example.leblanc_lepere_android_project.model.Product
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductService {
    @GET("products/{id}")
    suspend fun getProductById(@Path("id") id: Int): Product

    @GET("products")
    suspend fun getAllProducts(): List<Product>
}

