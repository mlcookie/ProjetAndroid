package com.example.leblanc_lepere_android_project

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.leblanc_lepere_android_project.model.Product

class CartAdapter(
    private var products: List<Product>,
    private val onQuantityChanged: () -> Unit,
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    inner class CartViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.cart_item_image)
        val titleView: TextView = view.findViewById(R.id.cart_item_title)
        val priceView: TextView = view.findViewById(R.id.cart_item_price)
        val quantityView: TextView = view.findViewById(R.id.cart_item_quantity)
        val plusButton: ImageButton = view.findViewById(R.id.button_increase_quantity)
        val minusButton: ImageButton = view.findViewById(R.id.button_decrease_quantity)
        val deleteButton: ImageButton = view.findViewById(R.id.button_delete_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cart_view, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val product = products[position]
        val cartProduct = CartManager.getProducts().find { it.productId == product.id }
        val quantity = cartProduct?.quantity ?: 1

        holder.titleView.text = product.title
        holder.priceView.text = "%.2f €".format(product.price)
        holder.quantityView.text = quantity.toString()

        Glide.with(holder.itemView.context)
            .load(product.image)
            .into(holder.imageView)

        holder.plusButton.setOnClickListener {
            CartManager.addProduct(product.id)
            onQuantityChanged()
        }

        holder.minusButton.setOnClickListener {
            if (quantity > 1) {
                CartManager.addProduct(product.id, -1)
            } else {
                CartManager.removeProduct(product.id)
            }
            onQuantityChanged()
        }

        holder.deleteButton.setOnClickListener {
            CartManager.removeProduct(product.id)
            onQuantityChanged()
        }
    }

    override fun getItemCount(): Int = products.size

    fun updateProducts(newProducts: List<Product>) {
        this.products = newProducts
        notifyDataSetChanged()
    }
}
