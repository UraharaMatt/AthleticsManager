package com.example.athleticsmanager

import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.athleticsmanager.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.google.firebase.storage.ktx.component1
import com.google.firebase.storage.ktx.component2

class MainActivity : AppCompatActivity() {
    lateinit var storage: FirebaseStorage
    private lateinit var storageRef: StorageReference
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityMainBinding
    lateinit var fileUri: Uri


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Inizializzazione parametri
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        storage = Firebase.storage
        storageRef = Firebase.storage.reference
        auth = Firebase.auth
        //Activity Result
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
        //button onClickListener
        with(binding){
            buttonChoose.setOnClickListener { showFileChooser(launchUploadActivity) }
            buttonUpload.setOnClickListener { onUploadClick() }
            buttonsignA.setOnClickListener { signInAnonymously() }
            textViewShow.setOnClickListener { showFileList() }
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
    private fun showFileChooser(launchUploadActivity: ActivityResultLauncher<Intent>) {
        val intentUp = Intent()
        intentUp.type = "image/*"
        intentUp.action = Intent.ACTION_GET_CONTENT
        launchUploadActivity.launch(intentUp)
    }
    private fun onUploadClick() {
        val dbRef = Firebase.database.reference
        val currentUser = auth.currentUser?.uid.toString()

        val filename = binding.eTXTNomeFile.text.toString().trim()
        if (filename.isEmpty()) {
            binding.eTXTNomeFile.error = "Inserire il nome dell'atleta"
            return
        }
        val filenameEst = filename+"."+ getFileExtension(fileUri)
        // [START create_child_reference]
        // Create a child reference
        // uploadRef now points to "upload"
        val uploadref = storageRef.child("upload/$filenameEst")
        Log.d(TAG, "$uploadref")

        val uploadtask = uploadref.putFile(fileUri)
        Log.d(TAG, "$uploadtask")
        uploadtask.addOnFailureListener {
            //Toast.makeText(this, "File Not Uploaded", Toast.LENGTH_SHORT).show()
            Log.d(TAG, "File Uploaded:NULL")
        }.addOnSuccessListener {
            binding.imageView.setImageURI(null)
            binding.eTXTNomeFile.text=null
        }.addOnProgressListener { (bytesTransferred, totalByteCount) ->
            val progress = (100.0 * bytesTransferred) / totalByteCount
            Log.d(TAG, "Upload is $progress% done")
        }.addOnCompleteListener{
            Log.d(TAG, "File Uploaded:SUCCESS")
            //recupero link download e lo carico nel DB
            uploadref.downloadUrl.addOnSuccessListener { urlTask ->
                // download URL is available here
                val url = urlTask.toString()
                val infoUpload = Upload(filename,url)
                dbRef.child("upload").push().setValue(infoUpload)//.child(currentUser)
            }.addOnFailureListener { e ->
                // Handle any errors
            }
        }
    }
    private fun getFileExtension(uri: Uri?): String? {
        val cR = contentResolver
        val mime = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(cR.getType(uri!!))
    }
    private fun showFileList(){
        val intent = Intent(this, FileList::class.java)
        startActivity(intent)
        finish()
    }

}