package com.example.wallpaper

import android.Manifest
import android.app.ActivityOptions
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import android.graphics.*
import android.os.Environment
import java.io.FileOutputStream
import java.util.*
import kotlin.collections.ArrayList
import android.graphics.Bitmap
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.widget.LinearLayout
import jp.wasabeef.blurry.Blurry
import kotlinx.android.synthetic.main.dialog_confirm_save.*
import java.io.File
import java.text.SimpleDateFormat


class MainActivity : AppCompatActivity() {

    private val PERMISSION_CODE = 123

    private val listPermission = listOf(
        Manifest.permission.CAMERA,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.ACCESS_MEDIA_LOCATION
    )

    private var arrWallpaper = ArrayList<HDWallpaper>()

    private var down = 0F

    private var up = 0F

    private var indexImage = 0

    private var isFirstLoad = true

    private var page = 1

    private lateinit var progressDialog: ProgressDialog

    private val iApiService by lazy {
        IApiService.createApi()
    }

    private var disposable: Disposable? = null

    var isLastestHdWallpaper: Boolean = true

    var cid: Int = 0

    var blurValue = 0

    var categoryName = "Recent"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Wait while loading...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        checkPermission()

        val bundle = intent.extras
        bundle?.let {
            isLastestHdWallpaper = bundle.getBoolean("isLastestHdWallpaper")
            cid = bundle.getInt("cid")
            categoryName = bundle.getString("categoryName", "Recent")
        }

        swipeImage()

        addEvent()

        btnRecent.text = categoryName

    }

    override fun onResume() {
        super.onResume()
        if(isLastestHdWallpaper) {
            getLastestHdWallpaper(page)
        } else {
            getHdWallpaperByCatId(cid, page)
        }

    }

    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(this@MainActivity, Manifest.permission.ACCESS_MEDIA_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            requestPermissions()
        }
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(this@MainActivity, listPermission.toTypedArray(), PERMISSION_CODE)

    }

    private fun initView(url: String) {
        Picasso.with(this@MainActivity)
            .load(url)
            .placeholder(R.drawable.ic_loading)
            .into(imgWallpaperHDNew)
        Picasso.with(this@MainActivity)
            .load(url)
            .placeholder(R.drawable.ic_loading)
            .into(imgWallpaperHD)
    }

    private fun addEvent() {

        btnEdit.setOnClickListener {
            if(seekBarEdit.visibility == View.GONE) {
                seekBarEdit.visibility = View.VISIBLE
                btnRecent.visibility = View.INVISIBLE
                imgWallpaperHD.isEnabled = false
                imgWallpaperHDNew.isEnabled = false
            } else {
                seekBarEdit.visibility = View.GONE
                btnRecent.visibility = View.VISIBLE
                imgWallpaperHD.isEnabled = true
                imgWallpaperHDNew.isEnabled = true
            }
        }

        btnRecent.setOnClickListener {
            val intent = Intent(this@MainActivity, CategoryListActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            val activityOptions = ActivityOptions.makeCustomAnimation(this@MainActivity, R.anim.fade_in, R.anim.fade_out)
            startActivity(intent, activityOptions.toBundle())
        }

        btnSave.setOnClickListener {
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.dialog_confirm_save)
            dialog.window?.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            dialog.show()

            dialog.layoutSaveImage.visibility = View.VISIBLE

            dialog.btnConfirmSave.setOnClickListener {
                Log.i("here", "hree")
                saveImage()
                dialog.dismiss()
            }
            dialog.btnCancel.setOnClickListener {
                dialog.dismiss()
            }

            dialog.txtContent.text = "Do you want to save this image?"
        }

        seekBarEdit.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, value: Int, p2: Boolean) {

                blurValue = (value/4)

                Blurry.with(this@MainActivity)
                    .radius(blurValue)
                    .capture(imgWallpaperHD)
                    .into(imgWallpaperHDNew)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }
        })

    }

    private fun saveImage() {
        val bitmapDrawable = imgWallpaperHDNew.drawable as BitmapDrawable
        val bitmapResource = bitmapDrawable.bitmap

        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())

        val root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString()

        val myDir = File(root)
        myDir.mkdirs()

        val fname = "$timeStamp.jpg"
        val file = File(myDir, fname)
        try {
            val out = FileOutputStream(file)
            bitmapResource.compress(Bitmap.CompressFormat.JPEG, 100, out)
            out.flush()
            out.close()

            val dialog = Dialog(this)
            dialog.setContentView(R.layout.dialog_confirm_save)
            dialog.window?.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            dialog.show()

            dialog.btnClose.visibility = View.VISIBLE
            dialog.btnClose.setOnClickListener {
                dialog.dismiss()
                btnRecent.visibility = View.VISIBLE
            }

            dialog.btnCancel.setOnClickListener {
                dialog.dismiss()
            }

            dialog.txtContent.text = "Image has been saved to your photos"
        } catch (e: Throwable) {
            Toast.makeText(this@MainActivity, "Permissions denied. You can't save image", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    private fun resetSeekbar() {
        seekBarEdit.progress = 0
    }

    private fun swipeImage() {
        imgWallpaperHD.setOnTouchListener(object: View.OnTouchListener {
            override fun onTouch(p0: View?, event: MotionEvent?): Boolean {
                if(event?.action == MotionEvent.ACTION_DOWN) {
                    down = event.x
                    return true
                }
                if(event?.action == MotionEvent.ACTION_UP) {
                    up = event.x
                    if((up - down) > 100) {
                        if(indexImage > 0) {
                            indexImage--
                            initView(arrWallpaper[indexImage].wallpaperImage)
                            resetSeekbar()
                            Log.i("scroll", indexImage.toString())
                        }
                        return true
                    }
                    if((down - up) > 100) {
                        if(indexImage < arrWallpaper.size - 1) {
                            indexImage++
                            initView(arrWallpaper[indexImage].wallpaperImage)
                            resetSeekbar()
                            Log.i("scroll", indexImage.toString())
                        }
                        if(indexImage == arrWallpaper.size - 2) {
                            if(isLastestHdWallpaper) {
                                page++
                                getLastestHdWallpaper(page)
                            } else {
                                page++
                                getHdWallpaperByCatId(cid, page)
                            }
                        }
                        return true
                    }
                }

                return false
            }

        })
    }

    private fun getLastestHdWallpaper(page: Int) {
        Log.i("page", page.toString())
        disposable = iApiService.getLastestHdWallpaper(page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { response -> for (index in response.hdWallpaper) {
                    arrWallpaper.add(index)
                }
                    if(isFirstLoad) {
                        initView(arrWallpaper[indexImage].wallpaperImage)
                        resetSeekbar()
                        isFirstLoad = false
                    }
                    progressDialog.dismiss()
                },
                {
                    Toast.makeText(this@MainActivity, "Some thing went wrong!", Toast.LENGTH_LONG).show()
                    progressDialog.dismiss()
                }
             )
    }

    private fun getHdWallpaperByCatId(catId: Int = 0, page: Int = 1) {
        Log.i("catId", catId.toString())
        disposable = iApiService.getHdWallpaperByCatId(catId, page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { response ->
                    for (index in response.hdWallpaper) {
                        arrWallpaper.add(index)
                    }
                    if(isFirstLoad) {
                        initView(arrWallpaper[indexImage].wallpaperImage)
                        resetSeekbar()
                        isFirstLoad = false
                    }
                    progressDialog.dismiss()
                },
                {
                    Toast.makeText(this@MainActivity, "Some thing went wrong!", Toast.LENGTH_LONG).show()
                    progressDialog.dismiss()
                }
            )
    }

    override fun onPause() {
        super.onPause()
        disposable?.dispose()
    }
}
