package com.example.athleticsmanager

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.athleticsmanager.databinding.ActivityFileListBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.google.firebase.storage.ktx.component1
import com.google.firebase.storage.ktx.component2

class FileList : AppCompatActivity() {
    lateinit var storage: FirebaseStorage
    private lateinit var storageRef: StorageReference
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityFileListBinding
    var fileListItems: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file_list)
        binding = ActivityFileListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        storage = Firebase.storage
        storageRef = Firebase.storage.reference
        auth = Firebase.auth

        binding.buttonBack.setOnClickListener { returnBack() }
        val uploadRef=storageRef.child("Upload")

        uploadRef.listAll().addOnSuccessListener { (items, prefixes) ->
                prefixes.forEach { prefix ->
                    // All the prefixes under listRef.
                    // You may call listAll() recursively on them.
                    fileListItems.add(prefix.name)
                    Log.d(TAG, "${prefix.name}")
                }

                items.forEach { item ->
                   /* fileListItems.add(0,item.name)
                    // All the items under listRef.*/
                    //Log.d(TAG, "${item.name}")
                }
            }
            .addOnFailureListener {
                // Uh-oh, an error occurred!
                Log.d(TAG, "nessun item caricato")
            }
        binding.filelistView.adapter = ArrayAdapter(this,android.R.layout.simple_list_item_1,fileListItems)
    //val launchListFileActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
    }
    private fun returnBack()
    {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null)
        val currentUser = auth.currentUser
        if (currentUser == null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

}