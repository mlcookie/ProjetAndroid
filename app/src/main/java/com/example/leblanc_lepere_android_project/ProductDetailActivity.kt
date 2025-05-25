package com.example.leblanc_lepere_android_project


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.leblanc_lepere_android_project.model.Product
import kotlinx.coroutines.launch

class ProductDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        val productId = intent.getIntExtra("PRODUCT_ID", -1)
        if (productId == -1) {
            finish() // Pas d'ID => on quitte
            return
        }

        val imageView = findViewById<ImageView>(R.id.product_image)
        val titleView = findViewById<TextView>(R.id.product_title)
        val categoryView = findViewById<TextView>(R.id.product_category)
        val priceView = findViewById<TextView>(R.id.product_price)
        val descriptionView = findViewById<TextView>(R.id.product_description)

        lifecycleScope.launch {
            try {
                val product = Product.getProductById(productId)
                titleView.text = product.title
                categoryView.text = "Catégorie : ${product.category}"
                priceView.text = "%.2f €".format(product.price)
                descriptionView.text = product.description

                Glide.with(this@ProductDetailActivity)
                    .load(product.image)
                    .into(imageView)

            } catch (e: Exception) {
                titleView.text = "Erreur de chargement"
                descriptionView.text = e.localizedMessage
            }
        }
        val backButton = findViewById<Button>(R.id.button_back)
        val addToCartButton = findViewById<Button>(R.id.button_add_to_cart)

        backButton.setOnClickListener {
            finish()
        }

        addToCartButton.setOnClickListener {
            CartManager.addProduct(productId)

            Toast.makeText(
                this@ProductDetailActivity,
                "Produit ajouté au panier",
                Toast.LENGTH_SHORT
            ).show()

            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
            /*val userId = 1
            val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                .format(java.util.Date())

            lifecycleScope.launch {
                try {
                    val existingCart = try {
                        Cart.getCartById(userId)
                    } catch (e: Exception) {
                        null
                    }
                    val updatedProducts = existingCart?.products?.toMutableList() ?: mutableListOf()
                    val existingProduct = updatedProducts.find { it.productId == productId }
                    if (existingProduct != null) {
                        val updatedProduct =
                            existingProduct.copy(quantity = existingProduct.quantity + 1)
                        updatedProducts.remove(existingProduct)
                        updatedProducts.add(updatedProduct)
                    } else {
                        updatedProducts.add(CartProduct(productId, 1))
                    }

                    val newCart = Cart(userId = userId, date = date, products = updatedProducts)
                    if (existingCart != null) {
                        Cart.updateCart(existingCart.userId, newCart)
                    } else {
                        Cart.createCart(newCart)
                    }

                    Toast.makeText(
                        this@ProductDetailActivity,
                        "Produit ajouté au panier",
                        Toast.LENGTH_SHORT
                    ).show()

                } catch (e: Exception) {
                    Toast.makeText(
                        this@ProductDetailActivity,
                        "Erreur : ${e.localizedMessage}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            val intent = Intent (this, CartActivity::class.java)
            startActivity(intent)*/
        }

    }
}
