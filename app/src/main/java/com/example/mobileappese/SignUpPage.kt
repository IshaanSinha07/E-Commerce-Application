package com.example.mobileappese

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.mobileappese.network.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpPage : Fragment(R.layout.sign_up_page) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val username: EditText = view.findViewById(R.id.username)
        val password: EditText = view.findViewById(R.id.password)
        val email: EditText = view.findViewById(R.id.email)
        val dob: EditText = view.findViewById(R.id.dob)
        val signUpButton: Button = view.findViewById(R.id.sign_up)

        signUpButton.setOnClickListener {
            val enteredUsername = username.text.toString()
            val enteredPassword = password.text.toString()
            val enteredEmail = email.text.toString()
            val enteredDob = dob.text.toString()
            if(enteredEmail.length < 3 || enteredPassword.length < 3 || enteredUsername.length < 3 || enteredDob.length < 3){
                Toast.makeText(requireContext(),"The Inputs should atleast be 3 in length!",Toast.LENGTH_SHORT).show()
            }
            else{
                addUser(enteredUsername, enteredPassword, enteredEmail, enteredDob)
            }
        }
    }

    private fun addUser(username: String, password: String, email: String, dob: String) {
        val call = RetrofitInstance.api.addUser(username, password, email, dob)
        call.enqueue(object : Callback<Map<String, String>> {
            override fun onResponse(call: Call<Map<String, String>>, response: Response<Map<String, String>>) {
                if (response.isSuccessful) {
                    Log.d("SignUpPage", "Response: ${response.body()?.get("message")}")
                    Toast.makeText(requireContext(), "User added successfully!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(requireActivity(), MainPage::class.java)
                    intent.putExtra("Username",username)
                    startActivity(intent)
                } else {
                    Toast.makeText(requireContext(), "Username or email already exists", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Map<String, String>>, t: Throwable) {
                Log.e("SignUpPage", "Error adding user: ${t.message}")
                Toast.makeText(requireContext(), "Username or email already exists", Toast.LENGTH_LONG).show()
            }
        })
    }
}
