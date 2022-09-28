package com.example.athleticsmanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.example.athleticsmanager.databinding.ActivityLoginBinding
import com.example.athleticsmanager.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
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
                buttonLogin.setOnClickListener{onLoginClick(binding)}
                redirectSignup.setOnClickListener{onSignUpClick()}
                ForgottenPassword.setOnClickListener { reSendPassword() }
        }
    }
    private fun onLoginClick(binding: ActivityLoginBinding) {
        val email = binding.emailLogin.editText?.text.toString().trim()
        val password = binding.passwordConfirm.editText?.text.toString().trim()
        if (email.isEmpty()) {
            binding.emailLogin.error = "Enter email"
            return
        }
        if (password.isEmpty()) {
            binding.passwordConfirm.error = "Enter password"
            return
        }
        loginUser(email, password)
    }
    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val currentlyUser = auth.currentUser
                    val uid = currentlyUser!!.uid
                    val database = Firebase.database.reference
                    val ref = database.child("users").child(uid)
                    ref.addValueEventListener(object : ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {
                            TODO("Not yet implemented")
                        }
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            // Get club value and control the spinner
                                Log.d(TAG, "signInWithEmail:success")
                                val intent = Intent(applicationContext, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                        }
                    })
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    val builder = AlertDialog.Builder(this)
                    with(builder)
                    {
                        setTitle("Authentication failed")
                        setMessage(task.exception?.message)
                        setPositiveButton("OK", null)
                        show()
                    }
                }
            }
    }
    private fun onSignUpClick() {
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
        finish()
    }
    private fun reSendPassword() {
        val currentlyUser = auth.currentUser
        val uid = currentlyUser!!.uid
        var emailAddress =""
        val emailRef = Firebase.database.reference.child("users").child(uid).child("email")
            .get().addOnSuccessListener {
                Log.i("firebase", "Got value ${it.value}")
                emailAddress=it.value.toString().trim()
            }.addOnFailureListener{
                Log.e("firebase", "Error getting data", it)
            }
        auth.sendPasswordResetEmail(emailAddress)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Email sent.")
                }
            }
    }
}