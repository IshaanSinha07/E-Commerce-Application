package com.example.mobileappese

import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment

class MainPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.main_page)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.bottom_nav)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            val params = view.layoutParams as ViewGroup.MarginLayoutParams
            params.bottomMargin = systemBars.bottom
            params.topMargin = systemBars.top
            view.layoutParams = params

            insets
        }


        val username = intent.getStringExtra("Username") ?: "Guest"
        val fragment: Fragment = HomePage.newInstance(username)
        supportFragmentManager.beginTransaction()
            .add(R.id.frameMain, fragment)
            .commit()

        val home:ImageView = findViewById(R.id.home)
        val cart:ImageView = findViewById(R.id.cart)
        val profile:ImageView = findViewById(R.id.profile)

        home.setOnClickListener{
            home.setImageResource(R.drawable.homewhite)
            cart.setImageResource(R.drawable.cart)
            profile.setImageResource(R.drawable.profile_icon)
            val homeFrag = HomePage.newInstance(username)
            replaceFragment(homeFrag)
        }
        cart.setOnClickListener{
            home.setImageResource(R.drawable.home)
            cart.setImageResource(R.drawable.cartwhite)
            profile.setImageResource(R.drawable.profile_icon)
            val cartPage = CartPage.newInstance(username)
            replaceFragment(cartPage)
        }
        profile.setOnClickListener{
            home.setImageResource(R.drawable.home)
            cart.setImageResource(R.drawable.cart)
            profile.setImageResource(R.drawable.profile_iconwhite)
            val profilePage = ProfilePage.newInstance(username)
            replaceFragment(profilePage)
        }
    }
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frameMain, fragment)
            .addToBackStack(null)
            .commit()
    }
}