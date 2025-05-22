package com.example.leblanc_lepere_android_project

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.leblanc_lepere_android_project.model.Product
import kotlinx.coroutines.launch

class ListProductActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var searchField: EditText
    private lateinit var searchButton: ImageButton
    private lateinit var cartButton: ImageButton

    private lateinit var adapter: ProductAdapter
    private var allProducts: List<Product> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_product)

        recyclerView = findViewById(R.id.product_recycler_view)
        searchField = findViewById(R.id.search_field)
        searchButton = findViewById(R.id.search_button)
        cartButton = findViewById(R.id.cart_button)

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ProductAdapter(emptyList()) { product ->
            val intent = Intent(this, ProductDetailActivity::class.java)
            intent.putExtra("PRODUCT_ID", product.id)
            startActivity(intent)
        }
        recyclerView.adapter = adapter

        lifecycleScope.launch {
            allProducts = Product.getAllProducts()
            adapter.updateProducts(allProducts)
        }

        searchButton.setOnClickListener {
            val query = searchField.text.toString()
            val filtered = allProducts.filter {
                it.title.contains(query, ignoreCase = true)
            }
            adapter.updateProducts(filtered)
        }

        cartButton.setOnClickListener {
            Toast.makeText(this, "Panier à implémenter 😄", Toast.LENGTH_SHORT).show()
        }
    }
}
