package com.example.leblanc_lepere_android_project

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.leblanc_lepere_android_project.model.Product
import kotlinx.coroutines.launch

class CartActivity : AppCompatActivity(){
    private lateinit var cartRecyclerView: RecyclerView
    private lateinit var cartTitle: TextView
    private lateinit var emptyCartText: TextView
    private lateinit var totalPrice: TextView
    private lateinit var checkoutButton: Button
    private lateinit var backToProductButton: Button

    private lateinit var adapter: CartAdapter
    private var cartProducts: List<Product> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        cartRecyclerView = findViewById(R.id.cart_recycler_view)
        cartTitle = findViewById(R.id.cart_title)
        emptyCartText = findViewById(R.id.empty_cart_text)
        totalPrice = findViewById(R.id.total_price)
        checkoutButton = findViewById(R.id.checkout_button)
        backToProductButton = findViewById(R.id.back_to_product_button)

        cartRecyclerView.layoutManager = LinearLayoutManager(this)
        adapter = CartAdapter(emptyList()) {
            updateCartDisplay()
        }
        cartRecyclerView.adapter = adapter

        updateCartDisplay()

        lifecycleScope.launch {
            val cartItems = CartManager.getProducts()
            val allProducts = mutableListOf<Product>()

            for (cartProduct in cartItems) {
                val product = Product.getProductById(cartProduct.productId)
                allProducts.add(product.copy(price = product.price * cartProduct.quantity))
            }

            cartProducts = allProducts
            adapter.updateProducts(cartProducts)

            val total = allProducts.sumOf { it.price.toDouble() }
            totalPrice.text = "Total : %.2f €".format(total)

            if (cartProducts.isEmpty()) {
                cartRecyclerView.visibility = View.GONE
                totalPrice.visibility = View.GONE
                checkoutButton.visibility = View.GONE
            } else {
                emptyCartText.visibility = View.GONE
            }
        }

        backToProductButton.setOnClickListener {
            val intent = Intent(this, ListProductActivity::class.java)
            startActivity(intent)
        }
    }

    private fun updateCartDisplay() {
        lifecycleScope.launch {
            val cartItems = CartManager.getProducts()
            val allProducts = mutableListOf<Product>()

            for (cartProduct in cartItems) {
                val product = Product.getProductById(cartProduct.productId)
                allProducts.add(product)
            }

            cartProducts = allProducts
            adapter.updateProducts(cartProducts)

            val total = cartItems.sumOf { item ->
                val prod = Product.getProductById(item.productId)
                (prod.price * item.quantity).toDouble()
            }
            totalPrice.text = "Total : %.2f €".format(total)

            if (cartItems.isEmpty()) {
                cartRecyclerView.visibility = View.GONE
                totalPrice.visibility = View.GONE
                checkoutButton.visibility = View.GONE
                emptyCartText.visibility = View.VISIBLE
            } else {
                cartRecyclerView.visibility = View.VISIBLE
                totalPrice.visibility = View.VISIBLE
                checkoutButton.visibility = View.VISIBLE
                emptyCartText.visibility = View.GONE
            }
        }
    }

}