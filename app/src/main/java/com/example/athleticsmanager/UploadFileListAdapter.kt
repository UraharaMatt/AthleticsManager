package com.example.athleticsmanager

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.ContextCompat.startActivity
import java.util.ArrayList

class UploadFileListAdapter(context: Context, private var items: ArrayList<Upload>) : BaseAdapter(){
    private val inflater: LayoutInflater
            = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return items.size
    }
    override fun getItem(position: Int): Upload {
        return items[position]
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // Get view for row item
        val rowView = inflater.inflate(R.layout.upload_list_item, parent, false)
        val nameView = rowView.findViewById(R.id.athleteNameView) as TextView
        val userView = rowView.findViewById(R.id.User) as TextView
        val imgButton = rowView.findViewById(R.id.btnDownloadFile) as ImageButton
        val res = items[position]
        nameView.text=res.naming
        userView.text=""
        updateEvent(imgButton, res.url, res.naming)
        return rowView
    }
    private fun updateEvent(imgButton: ImageButton, url: String?, naming: String?) {
        imgButton.setOnClickListener{
            //try{
              //  val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                //val imageLink = Uri.parse(url)
                //val request = DownloadManager.Request(imageLink)
                //request
                    //.setMimeType("image/*")
                  //  .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    //.setTitle(naming)
                    //.setAllowedOverMetered(true)
                //downloadManager.enqueue(request)
            //}
            /*catch (e:Exception){

            }*//*
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)*/
        }
    }
    /*
    private class ViewHolder(row: View?){
        var nameView: TextView? = null
        //var userView: TextView? = null
        //var urlView: ImageButton? = null
        init{
            this.nameView = row?.findViewById(R.id.athleteNameView)
           // this.urlView = row?.findViewById(R.id.imageButton)
        }
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val viewHolder: ViewHolder
        if (convertView == null) {
            val inflater = activity?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.upload_list_item, null)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }
        val res = items[position]
        viewHolder.nameView?.text = res.naming
//        viewHolder.urlView?.setImageURI() = null
        return view
    }*/
}