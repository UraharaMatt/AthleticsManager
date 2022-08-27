package com.example.athleticsmanager

import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.athleticsmanager.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.io.File

class MainActivity : AppCompatActivity() {
    lateinit var storage: FirebaseStorage
    private lateinit var storageRef: StorageReference
    lateinit var fileUri: Uri
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        storage = Firebase.storage
        storageRef = Firebase.storage.reference
        auth = Firebase.auth

        val launchUploadActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        { result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                if (data != null){
                    fileUri  = data.data!!
                    binding.imageView.setImageURI(fileUri)
                }
            }
        }
        with(binding){
            buttonChoose.setOnClickListener { showFileChooser(launchUploadActivity) }
            buttonUpload.setOnClickListener { onUploadClick() }
            buttonsignA.setOnClickListener { signInAnonymously() }
        }

    }
    public override fun onStart() {
        super.onStart()
        //updateUI(auth.currentUser)
    }
    private fun signInAnonymously() {
        // Sign in anonymously. Authentication is required to read or write from Firebase Storage.
        auth.signInAnonymously()
            .addOnSuccessListener(this) { authResult ->
                Log.d(TAG, "signInAnonymously:SUCCESS")
                //updateUI(authResult.user)
            }
            .addOnFailureListener(this) { exception ->
                Log.e(TAG, "signInAnonymously:FAILURE", exception)
                //updateUI(null)
            }
    }
    private fun updateUI(user: FirebaseUser?) {
        with(binding) {
            // Signed in or Signed out
            /*if (user != null) {
                buttonsignA.visibility = View.GONE
            } else {
                buttonsignA.visibility = View.VISIBLE
            }*/
        }
    }
    /*private fun uploadFiles() {
        //Recupero immagine dalla memoria telefono
        val launchUploadActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        { result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                if (data != null){
                    fileUri  = data.data!!
                    binding.imageView.setImageURI(fileUri)
                }
            }
        }
    }*/
    private fun showFileChooser(launchUploadActivity: ActivityResultLauncher<Intent>) {
        val intentUp = Intent()
        intentUp.type = "image/*"
        intentUp.action = Intent.ACTION_GET_CONTENT
        launchUploadActivity.launch(intentUp)
    }
    private fun onUploadClick() {
        /*val filename = binding.eTXTNomeFile.text.toString().trim()
        if (filename.isEmpty()) {
            binding.eTXTNomeFile.error = "Inserire il nome dell'atleta"
            return
        }*/
        val filenameEst = "prova8.jpg"//filename+"."+ getFileExtension(fileUri)
        // [START create_child_reference]
        // Create a child reference
        // imagesRef now points to "upload"
        val uploadref = storageRef.child("upload")
        val uploadtask = uploadref.child("/$filenameEst").putFile(fileUri)
        uploadtask.addOnFailureListener {
            Toast.makeText(this, "File Not Uploaded", Toast.LENGTH_SHORT).show()
            Log.d(TAG, "File Uploaded:NULL")
        }.addOnSuccessListener {
            binding.imageView.setImageURI(null)
            Toast.makeText(this, "File Uploaded", Toast.LENGTH_SHORT).show()
            Log.d(TAG, "File Uploaded:SUCCESS")

        }
    }
    fun getFileExtension(uri: Uri?): String? {
        val cR = contentResolver
        val mime = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(cR.getType(uri!!))
    }

}