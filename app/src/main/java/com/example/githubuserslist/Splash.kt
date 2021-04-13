package com.example.githubuserslist

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity


class Splash : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val handler = Handler()
        handler.postDelayed(Runnable {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()}, 4000)
    }
}