package com.example.mobileappese

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.mobileappese.network.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomePage : Fragment(R.layout.home_page) {
    private val productList: List<String> = listOf("Keyboard", "Mouse", "Laptop","Iphone 16 Pro")
    private val infoList: HashMap<String, HashMap<String, Any>> = hashMapOf(
        "Keyboard" to hashMapOf("imageSrc" to R.drawable.keyboard, "price" to 1000),
        "Mouse" to hashMapOf("imageSrc" to R.drawable.mouse, "price" to 900),
        "Laptop" to hashMapOf("imageSrc" to R.drawable.laptop, "price" to 50000),
        "Iphone 16 Pro" to hashMapOf("imageSrc" to R.drawable.iphone,"price" to 1_19_900)
    )
    private var currIndex = 0
    private var username: String? = null
    companion object {
        private const val ARG_USERNAME = "username"

        fun newInstance(username: String): HomePage {
            val fragment = HomePage()
            val args = Bundle()
            args.putString(ARG_USERNAME, username)
            fragment.arguments = args
            return fragment
        }
    }

    private fun imageChanger(image: ImageView) {
        val imageResource = infoList[productList[currIndex]]?.get("imageSrc") as? Int
        imageResource?.let {
            image.setImageResource(it)
        }
    }

    private fun detailChange(pName: String, pPrice: String, productName: TextView, productPrice: TextView) {
        productName.text = "Name: $pName"
        productPrice.text = "Price: $pPrice Rs"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        username = arguments?.getString(ARG_USERNAME)

        val productName: TextView = view.findViewById(R.id.productName)
        val productPrice: TextView = view.findViewById(R.id.productPrice)
        val image: ImageView = view.findViewById(R.id.image)
        val leftB: Button = view.findViewById(R.id.leftButton)
        val rightB: Button = view.findViewById(R.id.rightButton)
        val search: SearchView = view.findViewById(R.id.search)
        val addCart:Button = view.findViewById(R.id.add_cart)

        imageChanger(image)
        detailChange(productList[currIndex], infoList[productList[currIndex]]?.get("price").toString(), productName, productPrice)

        leftB.setOnClickListener {
            currIndex = if (currIndex == 0) productList.size - 1 else currIndex - 1
            imageChanger(image)
            detailChange(productList[currIndex], infoList[productList[currIndex]]?.get("price").toString(), productName, productPrice)
        }

        rightB.setOnClickListener {
            currIndex = if (currIndex == productList.size - 1) 0 else currIndex + 1
            imageChanger(image)
            detailChange(productList[currIndex], infoList[productList[currIndex]]?.get("price").toString(), productName, productPrice)
        }

        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                for (i in productList) {
                    if (newText?.let { i.startsWith(it, ignoreCase = true) } == true) {
                        val imageResource = infoList[i]?.get("imageSrc") as? Int
                        imageResource?.let {
                            image.setImageResource(it)
                        }
                        detailChange(i, infoList[i]?.get("price").toString(), productName, productPrice)
                        break
                    }
                }
                return false
            }
        })

        addCart.setOnClickListener{
            username?.let { it1 -> addToCart(it1,productList[currIndex],infoList[productList[currIndex]]?.get("price") as Int,1) }
        }
    }
    private fun addToCart(username: String, pname: String, pprice: Int, quantity: Int) {
        val call = RetrofitInstance.api.addToCart(username, pname, pprice, quantity)
        call.enqueue(object : Callback<Map<String, String>> {
            override fun onResponse(call: Call<Map<String, String>>, response: Response<Map<String, String>>) {
                if (response.isSuccessful) {
                    Log.d("HomePage", "Response: ${response.body()?.get("message")}")
                    Toast.makeText(requireContext(), "Product added to cart!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Error adding product to cart!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Map<String, String>>, t: Throwable) {
                Log.e("HomePage", "Error adding product to cart: ${t.message}")
                Toast.makeText(requireContext(), "Error adding product to cart: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

}