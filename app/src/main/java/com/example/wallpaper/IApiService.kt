package com.example.wallpaper

import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface IApiService {
    @GET("api.php?latest&")
    fun getLastestHdWallpaper(
        @Query("page") page: Int
    ): Observable<WallpaperModel>

    @GET("api.php?")
    fun getCategoryList(
        @Query("cat_list") catList: Int = 0
    ): Observable<CategoryListModel>

    @GET("api.php?")
    fun getHdWallpaperByCatId(
        @Query("cat_id") catId: Int = 0,
        @Query("page") page: Int = 1
    ): Observable<WallpaperModel>

    companion object {
        fun createApi(): IApiService {
            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(
                    GsonConverterFactory.create())
                .baseUrl("https://foriphone.net/php_web_services/")
                .build()

            return retrofit.create(IApiService::class.java)
        }
    }

}