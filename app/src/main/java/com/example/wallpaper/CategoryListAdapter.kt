package com.example.wallpaper

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_category_list.view.*

class CategoryListAdapter(var arrCategoryList: ArrayList<HDwallpaperCategoryList>): RecyclerView.Adapter<CategoryListViewHolder>() {

    private lateinit var context: Context

     lateinit var iItemCategoryListOnClick: IItemCategoryListOnClick

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryListViewHolder {
        context = parent.context
        return CategoryListViewHolder(LayoutInflater.from(context).inflate(R.layout.item_category_list, parent,false))
    }

    override fun getItemCount(): Int {
        return arrCategoryList.size
    }

    override fun onBindViewHolder(holder: CategoryListViewHolder, position: Int) {
        Picasso.with(context)
            .load(arrCategoryList[position].categoryImage)
            .placeholder(R.drawable.ic_loading)
            .error(R.drawable.ic_loading)
            .into(holder.imgCategoryList)

        holder.txtCategoryName.text = arrCategoryList[position].categoryName

        holder.itemView.setOnClickListener {
            iItemCategoryListOnClick.iItemCategoryListOnClick(
                arrCategoryList[position].cid.toInt(),
                arrCategoryList[position].categoryName)
        }

    }
}

class CategoryListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val txtCategoryName = itemView.txtCategoryName
    val imgCategoryList = itemView.imgCategoryList
}

interface IItemCategoryListOnClick {
    fun iItemCategoryListOnClick(cid: Int, categoryName: String)
}