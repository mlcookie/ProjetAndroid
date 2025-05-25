package com.example.leblanc_lepere_android_project

import com.example.leblanc_lepere_android_project.model.CartProduct

object CartManager {
    private val cartProducts = mutableListOf<CartProduct>()

    fun addProduct(productId: Int, quantity: Int = 1) {
        val existing = cartProducts.find { it.productId == productId }
        if (existing != null) {
            val updated = existing.copy(quantity = existing.quantity + quantity)
            cartProducts.remove(existing)
            cartProducts.add(updated)
        } else {
            cartProducts.add(CartProduct(productId, quantity))
        }
    }

    fun getProducts(): List<CartProduct> = cartProducts.toList()

    fun removeProduct(productId: Int) {
        cartProducts.removeIf { it.productId == productId }
    }
}
