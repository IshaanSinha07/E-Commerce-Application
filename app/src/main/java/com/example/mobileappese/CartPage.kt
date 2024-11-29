package com.example.mobileappese

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileappese.network.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CartPage : Fragment(R.layout.cart_page) {

    private lateinit var username: String
    private lateinit var cartRecyclerView: RecyclerView
    private lateinit var totalPrice: TextView
    private val itemsLiveData = MutableLiveData<MutableList<Map<String, Any>>>()
    private lateinit var cartAdapter: CartAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        totalPrice = view.findViewById(R.id.total_price)
        username = arguments?.getString(ARG_USERNAME) ?: "Guest"
        cartRecyclerView = view.findViewById(R.id.cartRecycler)
        cartRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        fetchCartItems(username)

        itemsLiveData.observe(viewLifecycleOwner, Observer { updatedItems ->
            cartAdapter = CartAdapter(updatedItems, username) {
                updateTotalPrice(updatedItems)
            }
            cartRecyclerView.adapter = cartAdapter
            updateTotalPrice(updatedItems)
        })

        val orderAll: Button = view.findViewById(R.id.order_all)

        itemsLiveData.observe(viewLifecycleOwner) { items ->
            orderAll.setOnClickListener {
                if (items.isNotEmpty()) {
                    clearCart(username)
                    updateTotalPrice(items)
                }
                else {
                    Toast.makeText(requireContext(), "Your cart is empty.", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    private fun updateTotalPrice(items: List<Map<String, Any>>) {
        if(items.isEmpty()){
            totalPrice.text = "Total: 0.0"
        }
        else{
            val total = items.sumOf { item ->
                val itemPrice = (item["pprice"] as? Double ?: 0.0).toInt()
                val quantity = (item["quantity"] as? Double ?: 1.0).toInt()
                itemPrice * quantity
            }
            totalPrice.text = "Total: ${total.toDouble()}"
        }
    }
    fun clearCart(username: String) {
        val call = RetrofitInstance.api.deleteFromCart(username)

        call.enqueue(object : Callback<Map<String, String>> {
            override fun onResponse(call: Call<Map<String, String>>, response: Response<Map<String, String>>) {
                if (response.isSuccessful && response.body()?.get("status") == "success") {
                    Toast.makeText(requireContext(), "All items Ordered successfully", Toast.LENGTH_LONG).show()
                    cartAdapter.clearItems()
                    updateTotalPrice(emptyList())
                } else {
                    Toast.makeText(requireContext(), "Failed to order", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Map<String, String>>, t: Throwable) {
                Toast.makeText(requireContext(), "Error ordering items: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
    private fun fetchCartItems(username: String) {
        val call = RetrofitInstance.api.getItemsFromCart(username)
        call.enqueue(object : Callback<Map<String, Any>> {
            override fun onResponse(call: Call<Map<String, Any>>, response: Response<Map<String, Any>>) {
                if (response.isSuccessful) {
                    val items = response.body()?.get("items") as? MutableList<Map<String, Any>>
                    if (items != null) {
                        itemsLiveData.value = items
                    } else {
                        updateTotalPrice(emptyList())
                        Toast.makeText(requireContext(), "No items in cart.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "Failed to load cart items.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Map<String, Any>>, t: Throwable) {
                Log.e("CartPage", "Error fetching cart items: ${t.message}")
                Toast.makeText(requireContext(), "Error fetching cart items: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    companion object {
        private const val ARG_USERNAME = "username"

        fun newInstance(username: String) = CartPage().apply {
            arguments = Bundle().apply {
                putString(ARG_USERNAME, username)
            }
        }
    }
}
