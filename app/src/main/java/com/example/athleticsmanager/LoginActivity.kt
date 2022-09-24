package com.example.athleticsmanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.athleticsmanager.databinding.ActivityLoginBinding
import com.example.athleticsmanager.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private var TAG = "LoginActivity"
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
            with(binding) {
                buttonLogin.setOnClickListener{}
                textsignupRedirect.setOnClickListener{}
        }
    }
}