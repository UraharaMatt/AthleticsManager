package com.example.athleticsmanager

import android.app.DownloadManager
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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


class FileList : AppCompatActivity() {
    private lateinit var storage: FirebaseStorage
    private lateinit var storageRef: StorageReference
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityFileListBinding
    //private lateinit var adapter1: UploadFileListAdapter
    var uploadList: MutableList<Upload> = ArrayList()
    private lateinit var adapter: UploadListAdapter
    private lateinit var recyclerView: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file_list)
        binding = ActivityFileListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        storage = Firebase.storage
        storageRef = Firebase.storage.reference
        auth = Firebase.auth
        val currentuser= auth.currentUser?.uid.toString()
        recyclerView=binding.recyclerview
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager=LinearLayoutManager(this)

        val dbRef = Firebase.database.reference
        val uploadRef = dbRef.child("upload").child(currentuser)
        uploadRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for(postData in dataSnapshot.children) {
                    val item = postData.getValue(Upload::class.java)
                    Log.d(TAG,"${item?.filename} + ${item?.url}")
                    uploadList.add(item!!)
                }
                /*val uploads : ArrayList<Upload> = ArrayList()
                Log.d(TAG,"$uploadList")
                for (item in uploadList){
                    Log.d(TAG, "${item.filename}")
                    //uploads.add(uploadList[item].getName())
                    uploads.add(item)
                }*/
                //adapter = ArrayAdapter(applicationContext,android.R.layout.simple_list_item_1,uploads)
                /*adapter = UploadFileListAdapter(applicationContext,
                    uploadList
                )
                binding.filelistView.adapter = adapter*/
                adapter = UploadListAdapter(applicationContext,currentuser,uploadRef,storageRef,uploadList)
                Log.d(TAG, adapter.itemCount.toString())
                recyclerView.adapter=adapter

            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
/* downlaod file form list
        binding.filelistView.onItemClickListener = OnItemClickListener { parent, view, position, id ->
            val namingList = uploadList[position]
            Log.d(TAG, "$position - "+namingList.url.toString())
            //
            //Opening the upload file in browser using the upload url
           /* val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(namingList.url)
            startActivity(intent)*/
            //END OpenBrowser
            // DOWNLOAD IMMAGINE IN FILE LOCALE
            download(namingList.url,namingList.naming)
        }
        /*binding.filelistView.setOnItemClickListener{adVi,view,position,id->
            val itemPOS=uploadList[position]

        }*/
 */
        binding.buttonBack.setOnClickListener { returnBack()}
   }

    private fun download(url: String?, filename: String?) {
        try{
            val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val imageLink = Uri.parse(url)
            val request = DownloadManager.Request(imageLink)
            request
                //.setMimeType("image/*")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setTitle(filename)
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
            val intent = Intent(this, LoginActivity::class.java)
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