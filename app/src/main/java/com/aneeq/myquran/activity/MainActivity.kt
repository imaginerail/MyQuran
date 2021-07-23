package com.aneeq.myquran.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.aneeq.myquran.R
import com.aneeq.myquran.adapter.SQRecyclerAdapter
import com.aneeq.myquran.models.SelectQuran
import com.aneeq.myquran.util.ConnectionManager
import org.json.JSONException

class MainActivity : AppCompatActivity() {
    lateinit var recycleSelect: RecyclerView
    lateinit var progressLayout: RelativeLayout
    lateinit var sqRecyclerAdapter: SQRecyclerAdapter
    lateinit var progressBar: ProgressBar
    val selectQuranList = arrayListOf<SelectQuran>()
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences=getSharedPreferences("Messenger Preferences", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
        setContentView(R.layout.activity_main)

        if (isLoggedIn) {
            val intent = Intent(this, StartReadingActivity::class.java)
            startActivity(intent)
            finish()

        }
        progressLayout =findViewById(R.id.progressLayout)
        progressBar =findViewById(R.id.progressBar)
        progressLayout.visibility = View.VISIBLE
        openDialog()
        setUpRecycler()

    }

    private fun openDialog() {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Assalamualaikum!")
        dialog.setMessage("Welcome to Al Quran CLoud App, Please select the edition of the Quran to read.")
        dialog.setPositiveButton("Select Quran") { _, _ ->
            dialog.show().cancel()
        }
        dialog.create()
        dialog.show()
    }

    private fun setUpRecycler() {
        recycleSelect = findViewById(R.id.recycleSelect)
        val queue = Volley.newRequestQueue(this)
        val url = "http://api.alquran.cloud/v1/edition/type/quran"

        if (ConnectionManager().checkConnection(this)) {

//creating a json object
            val jsonObjectRequest = object : JsonObjectRequest(
                Method.GET, url, null, Response.Listener {

                    progressLayout.visibility = View.GONE

                    try {
                        val jsonArray = it.getJSONArray("data")
                        for (i in 0 until jsonArray.length()) {
                            val jsonObject = jsonArray.getJSONObject(i)
                            val selectQuran = SelectQuran(
                                jsonObject.getString("identifier"),
                                jsonObject.getString("language"),
                                jsonObject.getString("name"),
                                jsonObject.getString("englishName"),
                                jsonObject.getString("format"),
                                jsonObject.getString("type"),
                                jsonObject.getString("direction")
                            )
                            selectQuranList.add(selectQuran)
                            if (this != null) {
                                sqRecyclerAdapter =
                                    SQRecyclerAdapter(this, selectQuranList)
                                val mLayoutManager =
                                    GridLayoutManager(this, 2)
                                recycleSelect.layoutManager = mLayoutManager
                                recycleSelect.itemAnimator = DefaultItemAnimator()
                                recycleSelect.adapter = sqRecyclerAdapter
                                recycleSelect.setHasFixedSize(true)
                            }
                        }

                    } catch (e: JSONException) {
                        e.printStackTrace()

                    }


                },
                Response.ErrorListener {
                    Toast.makeText(
                        this,
                        "Volley Error Occurred",
                        Toast.LENGTH_LONG
                    )
                        .show()
                }) {
                //headers are required for type and unique token
            }
            queue.add(jsonObjectRequest)
        } else openInternetDialog() //creating a dialogue box if there is no internet connection
    }

    private fun openInternetDialog() {
        val dialog = android.app.AlertDialog.Builder(this)
        dialog.setTitle("Failure")
        dialog.setMessage("Internet Connection NOT Found")
        dialog.setPositiveButton("Open Settings")
        { _, _ ->
            val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
            startActivity(settingsIntent)
            finish()
        }
        dialog.setNegativeButton("Exit")
        { _, _ ->
            ActivityCompat.finishAffinity(this)
        }
        dialog.create()
        dialog.show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
}

