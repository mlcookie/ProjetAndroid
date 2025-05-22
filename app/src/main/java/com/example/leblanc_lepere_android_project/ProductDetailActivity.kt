package com.example.leblanc_lepere_android_project


import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
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
    }
}
