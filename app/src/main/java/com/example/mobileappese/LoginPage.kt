package com.example.mobileappese

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.mobileappese.model.User
import com.example.mobileappese.network.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginPage : Fragment(R.layout.login_page) {

    private var userList: List<User> = emptyList()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val username: EditText = view.findViewById(R.id.username)
        val password: EditText = view.findViewById(R.id.password)
        val button: Button = view.findViewById(R.id.login)

        fetchUsers()

        button.setOnClickListener {
            val enteredUsername = username.text.toString()
            val enteredPassword = password.text.toString()

            if (userList.isNotEmpty()) {
                for (user in userList) {
                    if (user.Username == enteredUsername && user.Password == enteredPassword) {
                        Toast.makeText(requireContext(), "Login Successful", Toast.LENGTH_LONG).show()
                        val intent = Intent(requireActivity(), MainPage::class.java)
                        intent.putExtra("Username",user.Username)
                        startActivity(intent)
                        return@setOnClickListener
                    }
                }

                Toast.makeText(requireContext(), "User doesn't exist. Please Sign In first", Toast.LENGTH_LONG).show()
            }
            else {
                Toast.makeText(requireContext(), "User doesn't exist. Please Sign In first", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun fetchUsers() {
        val call = RetrofitInstance.api.getUsers()
        call.enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                if (response.isSuccessful) {
                    val users = response.body() ?: emptyList()
                    userList = users
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                Log.e("LoginPage", "Error fetching users: ${t.message}")
            }
        })
    }
}
