package com.example.leblanc_lepere_android_project

import android.os.VibrationEffect
import android.os.Vibrator
import android.content.Context
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
            finish()
            return
        }

        val imageView = findViewById<ImageView>(R.id.product_image)
        val titleView = findViewById<TextView>(R.id.product_title)
        val categoryView = findViewById<TextView>(R.id.product_category)
        val priceView = findViewById<TextView>(R.id.product_price)
        val descriptionView = findViewById<TextView>(R.id.product_description)
        val backButton = findViewById<Button>(R.id.button_back)
        val addToCartButton = findViewById<Button>(R.id.button_add_to_cart)

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
                titleView.text = "Erreur : Produit introuvable"
                descriptionView.text = "ID : $productId\n${e.localizedMessage}"
            }
        }

        backButton.setOnClickListener {
            finish()
        }


        addToCartButton.setOnClickListener {
            CartManager.addProduct(productId)
            val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                vibrator.vibrate(
                    VibrationEffect.createOneShot(150, VibrationEffect.DEFAULT_AMPLITUDE)
                )
            } else {
                vibrator.vibrate(150)
            }
            Toast.makeText(
                this@ProductDetailActivity,
                "Produit ajouté au panier",
                Toast.LENGTH_SHORT

            ).show()

            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
        }
        val shareButton = findViewById<Button>(R.id.shareButton)

        shareButton.setOnClickListener {
            val productUrl = "https://fakestoreapi.com/products/$productId"
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, "Regarde ce produit : $productUrl")
            }
            startActivity(Intent.createChooser(shareIntent, "Partager via"))
        }


    }
}
