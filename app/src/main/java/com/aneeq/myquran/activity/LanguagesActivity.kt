package com.aneeq.myquran.activity

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.aneeq.myquran.R
import com.aneeq.myquran.adapter.settings.LanguageAdapter
import com.aneeq.myquran.models.Languages
import com.aneeq.myquran.util.ConnectionManager
import org.json.JSONException

class LanguagesActivity : AppCompatActivity() {
    lateinit var recycleLang: RecyclerView
    lateinit var progressLayout: RelativeLayout
    lateinit var lnRecyclerAdapter: LanguageAdapter
    lateinit var progressBar: ProgressBar
    lateinit var sharedPreferences: SharedPreferences
    val langList = arrayListOf<Languages>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences=getSharedPreferences("Messenger Preferences", Context.MODE_PRIVATE)
        
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
        setContentView(R.layout.activity_languages)

        if (isLoggedIn) {
            val intent = Intent(this, NavigActivity::class.java)
            startActivity(intent)
            finish()

        }

        progressLayout =findViewById(R.id.progressLayout)
        progressBar =findViewById(R.id.progressBar)
        progressLayout.visibility = View.VISIBLE

        setUpRecycler()
    }
    private fun setUpRecycler() {
        recycleLang = findViewById(R.id.recycleLang)
        val queue = Volley.newRequestQueue(this)
        val url = "http://api.alquran.cloud/v1/edition/language"

        if (ConnectionManager().checkConnection(this)) {

//creating a json object
            val jsonObjectRequest = object : JsonObjectRequest(
                Method.GET, url, null, Response.Listener {

                    progressLayout.visibility = View.GONE

                    try {

                        val resArray = it.getJSONArray("data")
                        for (i in 0 until resArray.length()) {
                            val lang=Languages(resArray[i] as String)
                            langList.add(lang)


                            //attach dashboard fragment to adapter(bridge between data and view)
                            if (this != null) {
                                lnRecyclerAdapter =
                                    LanguageAdapter(this, langList)
                                val mLayoutManager: LinearLayoutManager =
                                    LinearLayoutManager(this)
                                recycleLang.layoutManager = mLayoutManager
                                recycleLang.itemAnimator = DefaultItemAnimator()
                                recycleLang.adapter = lnRecyclerAdapter
                                recycleLang.setHasFixedSize(true)
                            }
                        }


                    } catch (e: JSONException) {
                        e.printStackTrace()
                        Toast.makeText(
                            this,
                            "Some UnExpected Error Occurred",
                            Toast.LENGTH_LONG
                        )
                            .show()
                    }


                },
                Response.ErrorListener {
                    //println("Error is $it")
                    if (this != null) {
                        Toast.makeText(
                            this,
                            "Volley Error Occurred",
                            Toast.LENGTH_LONG
                        )
                            .show()
                    }
                }) {

            }
            queue.add(jsonObjectRequest)
        } else {

            val dialog = AlertDialog.Builder(this)
            dialog.setTitle("Failure")
            dialog.setMessage("Internet Connection NOT Found")
            dialog.setPositiveButton("Open Settings") { _, _ ->
                val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                finish()
            }
            dialog.setNegativeButton("Exit") { _, _ ->
                ActivityCompat.finishAffinity(this)
            }
            dialog.create()
            dialog.show()
        }

    }
}