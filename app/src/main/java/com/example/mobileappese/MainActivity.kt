package com.example.mobileappese

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

import androidx.fragment.app.Fragment

class MainActivity : AppCompatActivity() {
    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val fragment: Fragment = LoginPage()
        supportFragmentManager.beginTransaction()
            .add(R.id.frame, fragment)
            .commit()

        val loginSwitch: Button = findViewById(R.id.login_switch)
        val signupSwitch: Button = findViewById(R.id.signup_switch)

        loginSwitch.setOnClickListener {
            loginSwitch.background = resources.getDrawable(R.drawable.button_black, theme)
            signupSwitch.background = resources.getDrawable(R.drawable.button_background, theme)

            val loginFragment = LoginPage()
            replaceFragment(loginFragment)
        }

        signupSwitch.setOnClickListener {
            signupSwitch.background = resources.getDrawable(R.drawable.button_black, theme)
            loginSwitch.background = resources.getDrawable(R.drawable.button_background, theme)

            val signUpFragment = SignUpPage()
            replaceFragment(signUpFragment)
        }

    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame, fragment)
            .addToBackStack(null)
            .commit()
    }
}
