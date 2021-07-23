package com.aneeq.myquran.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.ImageView
import android.widget.TextView
import com.aneeq.myquran.R

class SplashActivity : AppCompatActivity() {
    lateinit var txtalqc: TextView
    lateinit var imgsrc: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        imgsrc = findViewById(R.id.imgsrc)
        txtalqc = findViewById(R.id.txtalqc)
        Handler().postDelayed({
            startActivity(Intent(this, StartReadingActivity::class.java))
            finish()
        }, 3000)
    }
}