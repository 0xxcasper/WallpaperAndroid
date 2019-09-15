package com.example.wallpaper

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_external_app.view.*

class ExternalAppAdapter(val arrExternalApp: ArrayList<ExternalAppModel>): RecyclerView.Adapter<ExternalViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExternalViewHolder {
        context = parent.context
        return ExternalViewHolder(LayoutInflater.from(context).inflate(R.layout.item_external_app, parent, false))
    }

    override fun getItemCount(): Int {
        return arrExternalApp.size
    }

    override fun onBindViewHolder(holder: ExternalViewHolder, position: Int) {
        holder.txtNameExternal.text = arrExternalApp[position].name
        holder.txtNameExternal.setCompoundDrawablesWithIntrinsicBounds(arrExternalApp[position].image, 0, 0, 0)
    }
}

class ExternalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val txtNameExternal = itemView.txtNameExternal
}