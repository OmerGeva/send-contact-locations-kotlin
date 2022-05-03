package com.example.omer_geva_drill_ii

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class RecyclerAdapter(
    names: MutableList<String>,
    numbers: MutableList<String>,
    location: String?,
    mainActivity: MainActivity
) : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {
    private var names = names
    private var numbers = numbers
    private var location = location
    private var mainActivity = mainActivity

    override fun onCreateViewHolder(holder: ViewGroup, p1: Int): RecyclerAdapter.ViewHolder {
        val v = LayoutInflater.from(holder.context).inflate(R.layout.card_layout, holder, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: RecyclerAdapter.ViewHolder, position: Int) {
        holder.itemTitle.text = names[position]
        holder.itemContent.text = numbers[position]
    }

    override fun getItemCount(): Int {
        return names.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var itemTitle: TextView
        var itemContent: TextView

        init {
            itemTitle = itemView.findViewById(R.id.itemTitle)
            itemContent = itemView.findViewById(R.id.itemContent)
            itemView.setOnClickListener {
                val url = "https://api.whatsapp.com/send?phone=" + itemContent.text + "&text=" + location
                val uri = Uri.parse(url)
                val intent = Intent(Intent.ACTION_VIEW, uri)

                mainActivity.startActivity(intent)
            }
        }
    }
}