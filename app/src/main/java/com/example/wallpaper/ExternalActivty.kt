package com.example.wallpaper

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_category_list.*
import kotlinx.android.synthetic.main.activity_external_activty.*

class ExternalActivty : AppCompatActivity() {

    private val arrExternalApp = ArrayList<ExternalAppModel>()

    private lateinit var externalAppAdapter: ExternalAppAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_external_activty)
        initView()
        btnBack.setOnClickListener {
            this.onBackPressed()
        }
    }

    private fun initView() {
        arrExternalApp.add(ExternalAppModel(name = "Rate us", image = R.mipmap.ic_launcher ))
        arrExternalApp.add(ExternalAppModel(name = "More app", image = R.mipmap.ic_launcher ))
        rvExternalApp.layoutManager = LinearLayoutManager(this)
        externalAppAdapter = ExternalAppAdapter(arrExternalApp)
        rvExternalApp.adapter = externalAppAdapter
    }
}
