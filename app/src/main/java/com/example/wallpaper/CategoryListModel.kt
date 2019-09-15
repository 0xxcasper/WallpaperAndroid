package com.example.wallpaper

import com.google.gson.annotations.SerializedName

data class CategoryListModel(
    @SerializedName("HD_WALLPAPER") val hdWallpaperCategoryList: ArrayList<HDwallpaperCategoryList>
)
data class HDwallpaperCategoryList(
    @SerializedName("cid") var cid: String,
    @SerializedName("category_name") var categoryName: String,
    @SerializedName("category_image") var categoryImage: String,
    @SerializedName("category_image_thumb") var categoryImageThumb: String,
    @SerializedName("category_total_wall") var categoryTotalWall: String
)