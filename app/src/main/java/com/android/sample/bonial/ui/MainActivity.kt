package com.android.sample.bonial.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.sample.bonial.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}