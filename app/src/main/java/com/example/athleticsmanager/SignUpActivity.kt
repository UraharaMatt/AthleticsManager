package com.example.athleticsmanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.athleticsmanager.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private var TAG = "SignUpActivity"
    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth

        with(binding){
            buttonsignup.setOnClickListener{onSignupClick(binding)}
            textLoginRedirect.setOnClickListener{loginRedirect()}
        }
    }
    private fun onSignupClick(binding: ActivitySignUpBinding) {
        val email = binding.emailEDTXT.editText?.text.toString().trim()
        val password = binding.passwordSignUp.editText?.text.toString().trim()
        val userName = binding.nameEditText.editText?.text.toString().trim()
        val userSurname = binding.SurnameEditText.editText?.text.toString().trim()
        if (userName.isEmpty()) {
            binding.emailEDTXT.error = "Inserisci il Nome"
            return
        }
        if (userSurname.isEmpty()) {
            binding.SurnameEditText.error = "Inserisci il Cognome"
            return
        }
        if (email.isEmpty()) {
            binding.emailEDTXT.error = "Inserisci l email"
            return
        }
        if (password.isEmpty()) {
            binding.passwordSignUp.error = "Inserisci la password"
            return
        }
        if (password.length < 6){
            binding.passwordSignUp.error = "la password deve essere maggiore di 6 caratteri"
            return
        }
        createUser(userName, userSurname,email,password)
    }
    private fun createUser(userName: String, userSurname: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    val currentlyUser = auth.currentUser
                    val uid = currentlyUser!!.uid
                    val newUser = User(userName,userSurname,email)
                    val database = Firebase.database.reference
                    database.child("users").child(uid).setValue(newUser)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val intent = Intent(applicationContext, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                        }
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }
    fun loginRedirect() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}