package com.example.athleticsmanager

import android.app.DownloadManager
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.athleticsmanager.databinding.ActivityFileListBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.io.File


class FileList : AppCompatActivity() {
    lateinit var storage: FirebaseStorage
    private lateinit var storageRef: StorageReference
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityFileListBinding
    private lateinit var adapter: ArrayAdapter<String?>
    var uploadList: MutableList<Upload> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file_list)
        binding = ActivityFileListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        storage = Firebase.storage
        storageRef = Firebase.storage.reference
        auth = Firebase.auth
        val currentuser= auth.currentUser
        val dbRef = Firebase.database.reference
        val uploadRef = dbRef.child("upload")
        uploadRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for(postData in dataSnapshot.children) {
                    val item = postData.getValue(Upload::class.java)
                    uploadList.add(item!!)
                }
                val uploads = arrayListOf<String?>()
                Log.d(TAG,"$uploadList.lastIndex")
                for (item in 1..uploadList.lastIndex){
                    Log.d(TAG, "$item" + "-" + uploadList[item].getName().toString() )
                    uploads.add(uploadList[item].getName())
                }
                adapter = ArrayAdapter(applicationContext,android.R.layout.simple_list_item_1,uploads)
                binding.filelistView.adapter = adapter
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
        binding.filelistView.onItemClickListener = OnItemClickListener { parent, view, position, id ->
            val namingList = uploadList[position]
            Log.d(TAG, "$position - "+namingList.url.toString())
            //Opening the upload file in browser using the upload url
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(namingList.url)
            startActivity(intent)
            //END OpenBrowser
            //download(namingList.url,namingList.naming)
        }
        binding.buttonBack.setOnClickListener { returnBack()}
   }

    private fun download(url: String?, naming: String?) {
        try{
            val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val imageLink = Uri.parse(url)
            val request = DownloadManager.Request(imageLink)
            request
                //.setMimeType("image/*")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setTitle(naming)
                .setAllowedOverMetered(true)
            downloadManager.enqueue(request)
        }
        catch (e:Exception){

        }
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
    private fun getFileExtension(uri: String?): String? {
        val cR = contentResolver
        val mime = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(cR.getType(Uri.parse(uri!!)))
    }

}