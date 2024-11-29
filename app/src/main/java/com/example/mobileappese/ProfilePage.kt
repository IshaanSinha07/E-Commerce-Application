package com.example.mobileappese

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment

class ProfilePage : Fragment(R.layout.profile_page) {
    private lateinit var username: String
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        username = arguments?.getString(ARG_USERNAME) ?: "Guest"
        val uname:TextView = view.findViewById(R.id.uname)
        uname.text = username

        val logout:Button = view.findViewById(R.id.logout)
        logout.setOnClickListener{
            val intent = Intent(requireActivity(), MainActivity::class.java)
            startActivity(intent)
        }
    }
    companion object {
        private const val ARG_USERNAME = "username"

        fun newInstance(username: String) = ProfilePage().apply {
            arguments = Bundle().apply {
                putString(ARG_USERNAME, username)
            }
        }
    }
}