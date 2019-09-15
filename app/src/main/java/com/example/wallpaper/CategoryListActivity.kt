package com.example.wallpaper

import android.app.ActivityOptions
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.Toast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_category_list.*

class CategoryListActivity : AppCompatActivity(), IItemCategoryListOnClick {

    private var disposable: Disposable? = null

    private val iApiService by lazy {
        IApiService.createApi()
    }

    private lateinit var progressDialog: ProgressDialog

    private var arrCategory = ArrayList<HDwallpaperCategoryList>()

    private lateinit var categoryListAdapter: CategoryListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_list)
        initView()
        addEvents()
        getApiCategoryList()
    }

    private fun getApiCategoryList(catList: Int = 0) {
        disposable = iApiService.getCategoryList(catList)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { response ->
                    for (index in response.hdWallpaperCategoryList) {
                        arrCategory.add(index)
                        Log.i("arrCategory", index.categoryName)
                    }
                    categoryListAdapter = CategoryListAdapter(arrCategory)
                    categoryListAdapter.iItemCategoryListOnClick = this
                    rvCategoryList.adapter = categoryListAdapter
                    progressDialog.dismiss()
                },
                {
                    Toast.makeText(this@CategoryListActivity, "Some thing went wrong!", Toast.LENGTH_LONG).show()
                    progressDialog.dismiss()
                }
            )
    }

    private fun initView() {
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Wait while loading...")
        progressDialog.setCancelable(false)
        progressDialog.show()
        rvCategoryList.layoutManager = LinearLayoutManager(this@CategoryListActivity)
    }

    private fun addEvents() {
        btnGoExternalActivity.setOnClickListener {
            val intent = Intent(this@CategoryListActivity, ExternalActivty::class.java)
            startActivity(intent)
        }
    }

    override fun iItemCategoryListOnClick(cid: Int, categoryName: String) {
        val intent = Intent(this@CategoryListActivity, MainActivity::class.java)
        intent.putExtra("isLastestHdWallpaper", false)
        intent.putExtra("cid", cid)
        intent.putExtra("categoryName", categoryName)

        val activityOptions = ActivityOptions.makeCustomAnimation(this@CategoryListActivity, R.anim.fade_in, R.anim.fade_out)
        startActivity(intent, activityOptions.toBundle())
    }
}
