package com.aneeq.myquran.activity


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.aneeq.myquran.R
import com.aneeq.myquran.TestMusicGithubActivity

class StartReadingActivity : AppCompatActivity() {
    lateinit var scrollView: ScrollView
    lateinit var txtyourselected: TextView
    lateinit var imgsq: ImageView
    lateinit var btnstart: Button
    lateinit var btnResume: Button
    lateinit var btntest: Button
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_reading)
        sharedPreferences = getSharedPreferences("Messenger Preferences", Context.MODE_PRIVATE)

        scrollView = findViewById(R.id.scrollView)
        txtyourselected = findViewById(R.id.txtyourselected)
        btnResume = findViewById(R.id.btnResume)
        imgsq = findViewById(R.id.imgsq)
        btnstart = findViewById(R.id.btnstart)
        btntest = findViewById(R.id.btntest)
        if (sharedPreferences.contains("resume")) btnResume.visibility = View.VISIBLE
        else btnResume.visibility = View.GONE

        btnResume.setOnClickListener {
            val i = Intent(this, ReciteJuzActivity::class.java)
            i.putExtra("page", sharedPreferences.getInt("resume", 1))
            startActivity(i)

        }



        btnstart.setOnClickListener {
            val i = Intent(this, NavigActivity::class.java)
            startActivity(i)
        }

        btntest.setOnClickListener {
            val i = Intent(this, TestMusicGithubActivity::class.java)
            startActivity(i)
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }


}