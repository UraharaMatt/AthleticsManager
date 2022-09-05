package com.example.athleticsmanager

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
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

        //binding.filelistView.setOnItemClickListener(AdapterView.OnItemClickListener())


        binding.filelistView.onItemClickListener = OnItemClickListener { parent, view, position, id ->
            val namingList = uploadList[position]
            Log.d(ContentValues.TAG, "${namingList.naming}")
            /*
            val intentDWNL = Intent()
            intentDWNL.action = Intent.ACTION_VIEW
            intentDWNL.setData(Uri.parse(namingList.getLink()))
            launchItemClickAction.launch(intentDWNL)
*/
        }

        val dbRef = Firebase.database.reference
        val uploadRef = dbRef.child("upload")//.child(currentuser.toString())
        uploadRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val item = snapshot.getValue(Upload::class.java)
                uploadList.add(item!!)

            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        val uploads = arrayListOf<String?>()
        for (item in 1..uploadList.size){
            uploads.add(uploadList[item].getName())
        }
        adapter = ArrayAdapter(this,android.R.layout.simple_list_item_1,uploads)
        binding.filelistView.adapter = adapter

        binding.buttonBack.setOnClickListener { returnBack()}
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