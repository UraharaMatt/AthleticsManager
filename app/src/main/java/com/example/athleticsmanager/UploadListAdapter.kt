package com.example.athleticsmanager

import android.app.DownloadManager
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class UploadListAdapter (context: Context, private var items: MutableList<Upload>):
    RecyclerView.Adapter<UploadListAdapter.ViewHolder>() {

    //var onItemClick : ((Upload) -> Unit)? = null

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
        //holder.userView.text=uploads.url.toString().trim()
        holder.downloadButton.setOnClickListener {
            //onItemClick?.invoke(uploads)
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



        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}