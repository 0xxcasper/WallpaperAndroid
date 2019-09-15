package com.example.wallpaper

import com.google.gson.annotations.SerializedName

data class WallpaperModel(
    @SerializedName("HD_WALLPAPER") val hdWallpaper: ArrayList<HDWallpaper>
)

data class HDWallpaper(
    @SerializedName("num") val num: String,
    @SerializedName("id") val id: String,
    @SerializedName("cat_id") val catId: String,
    @SerializedName("wallpaper_type") val wallpaperType: String,
    @SerializedName("wallpaper_image") val wallpaperImage: String,
    @SerializedName("wallpaper_image_thumb") val wallpaperImageThumb: String,
    @SerializedName("total_views") val totalViews: String,
    @SerializedName("total_rate") val totalRate: String,
    @SerializedName("rate_avg") val rateAvg: String,
    @SerializedName("wall_tags") val wallTags: String,
    @SerializedName("cid") val cid: String,
    @SerializedName("category_name") val categoryName: String,
    @SerializedName("category_image") val categoryImage: String,
    @SerializedName("category_image_thumb") val categoryImageThumb: String
)