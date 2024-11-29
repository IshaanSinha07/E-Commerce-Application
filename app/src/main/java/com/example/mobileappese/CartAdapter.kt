package com.example.mobileappese

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileappese.network.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CartAdapter(
    private var items: MutableList<Map<String, Any>>,
    private val username: String,
    private val updateTotalCallback: () -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    inner class CartViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val productImage: ImageView = view.findViewById(R.id.cart_image)
        val productName: TextView = view.findViewById(R.id.pname)
        val productPrice: TextView = view.findViewById(R.id.price_parent)
        val quantityText: TextView = view.findViewById(R.id.quantity)
        val plusButton: TextView = view.findViewById(R.id.plus)
        val minusButton: TextView = view.findViewById(R.id.minus)
    }

    private val productImageMap = mapOf(
        "Keyboard" to R.drawable.keyboard,
        "Mouse" to R.drawable.mouse,
        "Laptop" to R.drawable.laptop,
        "Iphone 16 Pro" to R.drawable.iphone
    )
    fun clearItems() {
        items.clear()
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cart_product, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val item = items[position]
        val productName = item["pname"] as? String ?: "Unknown"
        val productPrice = item["pprice"] as? Double ?: 0.0
        var quantity = (item["quantity"] as? Double ?: 1.0).toInt()

        holder.productName.text = productName
        holder.productPrice.text = "Price: $productPrice"
        holder.quantityText.text = quantity.toString()

        val imageResId = productImageMap[productName] ?: R.drawable.keyboard
        holder.productImage.setImageResource(imageResId)

        holder.plusButton.setOnClickListener {
            quantity += 1
            holder.quantityText.text = quantity.toString()
            updateCartQuantity(username, productName, productPrice.toInt(), 1, holder.itemView)
            items[position] = item.toMutableMap().apply { put("quantity", quantity.toDouble()) }
            updateTotalCallback()
        }

        holder.minusButton.setOnClickListener {
            if (quantity > 1) {
                quantity -= 1
                holder.quantityText.text = quantity.toString()
                items[position] = item.toMutableMap().apply { put("quantity", quantity.toDouble()) }
            }
            else {
                items.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, items.size)
            }
            updateCartQuantity(username, productName, productPrice.toInt(), -1, holder.itemView)
            updateTotalCallback()
        }
    }

    override fun getItemCount() = items.size

    private fun updateCartQuantity(
        username: String,
        pname: String,
        pprice: Int,
        quantity: Int,
        itemView: View
    ) {
        val call = RetrofitInstance.api.addToCart(username, pname, pprice, quantity)
        call.enqueue(object : Callback<Map<String, String>> {
            override fun onResponse(call: Call<Map<String, String>>, response: Response<Map<String, String>>) {
                if (response.isSuccessful) {
                    Log.d("CartAdapter", "Quantity updated: ${response.body()?.get("message")}")
                } else {
                    Toast.makeText(itemView.context, "Error updating quantity!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Map<String, String>>, t: Throwable) {
                Log.e("CartAdapter", "Error updating quantity: ${t.message}")
                Toast.makeText(itemView.context, "Error updating quantity: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    fun updateItems(newItems: MutableList<Map<String, Any>>) {
        items = newItems
        notifyDataSetChanged()
    }
}
