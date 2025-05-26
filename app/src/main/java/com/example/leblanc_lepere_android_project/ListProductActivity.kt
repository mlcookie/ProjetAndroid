package com.example.leblanc_lepere_android_project

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
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
    private lateinit var sortingButton: Button
    private lateinit var cartButton: ImageButton

    private lateinit var adapter: ProductAdapter
    private var allProducts: List<Product> = listOf()
    private var currentFilteredProducts: List<Product> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_product)

        recyclerView = findViewById(R.id.product_recycler_view)
        searchField = findViewById(R.id.search_field)
        searchButton = findViewById(R.id.search_button)
        sortingButton = findViewById(R.id.sorting_button)
        cartButton = findViewById(R.id.cart_button)

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ProductAdapter(emptyList()) { product ->
            val intent = Intent(this, ProductDetailActivity::class.java)
            intent.putExtra("PRODUCT_ID", product.id)
            startActivity(intent)
        }
        recyclerView.adapter = adapter

        lifecycleScope.launch {
            try {
                allProducts = Product.getAllProducts()
                currentFilteredProducts = allProducts
                adapter.updateProducts(allProducts)
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(
                    this@ListProductActivity,
                    "Failed to load products. Check your internet connection.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        searchButton.setOnClickListener {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(searchField.windowToken, 0)
            val query = searchField.text.toString()
            val filtered = allProducts.filter {
                it.title.contains(query, ignoreCase = true)
            }
            currentFilteredProducts = filtered
            adapter.updateProducts(filtered)
            sortingButton.text = "Trier la recherche"
        }

        searchField.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE) {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(searchField.windowToken, 0)
                val query = searchField.text.toString()
                val filtered = allProducts.filter {
                    it.title.contains(query, ignoreCase = true)
                }
                currentFilteredProducts = filtered
                adapter.updateProducts(filtered)
                sortingButton.text = "Trier la recherche"
                true
            } else {
                false
            }

        }

        sortingButton.setOnClickListener {
            val popup = android.widget.PopupMenu(this, sortingButton)
            popup.menuInflater.inflate(R.menu.menu_sort_options, popup.menu)

            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.sort_price_croissant -> {
                        val sorted = currentFilteredProducts.sortedBy { it.price }
                        adapter.updateProducts(sorted)
                        sortingButton.text = "Prix croissant"
                        true
                    }
                    R.id.sort_price_decroissant -> {
                        val sorted = currentFilteredProducts.sortedByDescending { it.price }
                        adapter.updateProducts(sorted)
                        sortingButton.text = "Prix décroissant"
                        true
                    }
                    R.id.sort_default -> {
                        adapter.updateProducts(currentFilteredProducts)
                        sortingButton.text = "Trier la recherche"
                        true
                    }
                    else -> false
                }
            }

            popup.show()
        }

        cartButton.setOnClickListener() {
            val intent = Intent (this, CartActivity::class.java)
            startActivity(intent)
        }


    }
}
