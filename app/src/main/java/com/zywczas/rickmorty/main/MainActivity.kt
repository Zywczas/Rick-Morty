package com.zywczas.rickmorty.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.zywczas.rickmorty.R
import com.zywczas.rickmorty.factories.UniversalFragmentFactory
import dagger.android.AndroidInjection
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var fragmentFactory: UniversalFragmentFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        supportFragmentManager.fragmentFactory = fragmentFactory
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }


}