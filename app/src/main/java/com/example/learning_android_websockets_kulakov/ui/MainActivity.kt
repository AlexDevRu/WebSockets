package com.example.learning_android_websockets_kulakov.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.learning_android_websockets_kulakov.R
import com.example.learning_android_websockets_kulakov.ui.observable_coins.ObservableCoinsFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment, ObservableCoinsFragment())
            .commit()
    }
}