package com.aneeq.myquran.activity

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.aneeq.myquran.R
import com.aneeq.myquran.adapter.audio.AudioDisplayAdapter
import com.aneeq.myquran.models.Reciters
import com.aneeq.myquran.util.ConnectionManager
import org.json.JSONException

class AudioDisplayActivity : AppCompatActivity() {
    lateinit var txtSelectln: TextView
    lateinit var recycleAD: RecyclerView
    lateinit var progressLayout: RelativeLayout
    lateinit var progressBar: ProgressBar
    lateinit var audioDisplayAdapter: AudioDisplayAdapter
    val adList = ArrayList<Reciters>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_display)
        txtSelectln = findViewById(R.id.txtSelectln)
        recycleAD = findViewById(R.id.recycleAD)
        progressLayout = findViewById(R.id.progressLayout)
        progressBar = findViewById(R.id.progressBar)

        setUpRecycler()


    }

    private fun setUpRecycler() {
        val queue = Volley.newRequestQueue(this)
        val url = "https://everyayah.com/data/recitations.js"

        if (ConnectionManager().checkConnection(this)) {

//creating a json object
            val jsonObjectRequest = object : JsonObjectRequest(
                Method.GET, url, null, Response.Listener {

                    progressLayout.visibility = View.GONE

                    try {
                        for (i in 1..79) {
                            val numers = it.getJSONObject("$i")
                            if (!numers.getString("subfolder")
                                    .contains("/")
                            ) {
                                val ad = Reciters(
                                    numers.getString("subfolder"),
                                    numers.getString("name"),
                                    numers.getString("bitrate")
                                )
                                adList.add(ad)
                            }
                        }


                        //attach dashboard fragment to adapter(bridge between data and view)
                        audioDisplayAdapter =
                            AudioDisplayAdapter(this, adList)
                        val mLayoutManager =
                            GridLayoutManager(this, 2)
                        recycleAD.layoutManager = mLayoutManager
                        recycleAD.itemAnimator = DefaultItemAnimator()
                        recycleAD.adapter = audioDisplayAdapter
                        recycleAD.setHasFixedSize(true)


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
                    Toast.makeText(
                        this,
                        "Volley Error Occurred",
                        Toast.LENGTH_LONG
                    )
                        .show()
                }) {

            }
            queue.add(jsonObjectRequest)
        } else {

            openInternet()
        }

    }

    private fun openInternet() {
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