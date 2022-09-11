package com.example.athleticsmanager

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageButton
import android.widget.TextView
import com.google.firebase.database.ValueEventListener
import java.util.ArrayList

class UploadFileListAdapter(private var activity: ValueEventListener, private var items: ArrayList<Upload>) : BaseAdapter(){
    private class ViewHolder(row: View?){
        var nameView: TextView? = null
        //var userView: TextView? = null
        //var urlView: ImageButton? = null
        init{
            this.nameView = row?.findViewById(R.id.athleteNameView)
           // this.urlView = row?.findViewById(R.id.imageButton)
        }
    }
    override fun getCount(): Int {
        return items.size
    }
    override fun getItem(position: Int): Upload {
        return items[position]
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
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
    }
}