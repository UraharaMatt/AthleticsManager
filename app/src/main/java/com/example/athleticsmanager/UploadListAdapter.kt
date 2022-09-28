package com.example.athleticsmanager

import android.app.DownloadManager
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.Button
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference

class UploadListAdapter(
    context: Context,
    currentuser: String,
    uploadRef: DatabaseReference,
    storage: StorageReference,
    private var items: MutableList<Upload>
): RecyclerView.Adapter<UploadListAdapter.ViewHolder>() {
    private val storageRef = storage
    private val localContext=context
    private val user=currentuser
    private val dbRef=uploadRef
    class ViewHolder(rowView: View): RecyclerView.ViewHolder(rowView){
        val nameView = rowView.findViewById(R.id.athleteNameView) as TextView
        //val userView = rowView.findViewById(R.id.User) as TextView
        val downloadButton = rowView.findViewById(R.id.btnDownloadFile) as Button
        val delButton = rowView.findViewById(R.id.btnDeleteFile) as Button
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate((R.layout.upload_list_item),parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val uploads = items[position]
        holder.nameView.text=uploads.filename
        holder.downloadButton.setOnClickListener {
                try{
                    val downloadManager = holder.itemView.context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                    val imageLink = Uri.parse(uploads.url)
                    val request = DownloadManager.Request(imageLink)
                    request
                        .setMimeType("image/*")
                        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                        .setTitle(uploads.filename)
                        .setAllowedOverMetered(true)
                    downloadManager.enqueue(request)
                }
                catch (e:Exception){
                }
        }
        holder.delButton.setOnClickListener{
            Log.d(ContentValues.TAG, "Delete CLICK ${uploads.filename}")
            //val namewithext =
            val delref= storageRef.child("upload").child(user).child("${uploads.filename}.jpg")
            Log.d(ContentValues.TAG, "$delref")
            delref.delete().addOnSuccessListener {
                Log.d(ContentValues.TAG, "Delete ${uploads.filename}")
                val delname=uploads.filename
                val delUrl=uploads.url
                if (delname != null && delUrl!=null) {
                    /*dbRef.child(delname).removeValue()
                        .addOnCompleteListener{Log.d(ContentValues.TAG, "cancellato $delname")}
                        .addOnFailureListener{Log.d(ContentValues.TAG, "non cancellato $delname")}
                    dbRef.child(delUrl).removeValue()
                        .addOnCompleteListener{Log.d(ContentValues.TAG, "cancellato $delref")}
                        .addOnFailureListener{Log.d(ContentValues.TAG, "non cancellato $delref")}
                    */Log.d(ContentValues.TAG, "cancellato? ${dbRef.child(delname)}")
                }
            }.addOnFailureListener {
                Log.d(ContentValues.TAG, "NOT Delete ${uploads.filename}")
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    private fun getFileExtension(uri: Uri?, context: Context): String? {
        val cR = context.contentResolver
        val mime = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(cR.getType(uri!!))
    }
}