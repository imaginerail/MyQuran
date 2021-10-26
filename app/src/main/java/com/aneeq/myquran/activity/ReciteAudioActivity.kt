package com.aneeq.myquran.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.aneeq.myquran.R
import com.aneeq.myquran.adapter.audio.ReciteAudioAdapter
import com.aneeq.myquran.models.AudioClass
import com.aneeq.myquran.util.ConnectionManager
import com.arges.sepan.argmusicplayer.PlayerViews.ArgPlayerSmallView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.json.JSONException

class ReciteAudioActivity : AppCompatActivity() {
    lateinit var sharedPreferences: SharedPreferences
    lateinit var toolbar: Toolbar
    lateinit var recycleRecite: RecyclerView
    lateinit var progressLayout: RelativeLayout
    lateinit var progressBar: ProgressBar
    lateinit var reciteSurahAdapter: ReciteAudioAdapter
    lateinit var argmusicplayer: ArgPlayerSmallView
    lateinit var fabplay: FloatingActionButton
    val aList = arrayListOf<AudioClass>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recite_audio)
        sharedPreferences = getSharedPreferences("Messenger Preferences", Context.MODE_PRIVATE)
        recycleRecite = findViewById(R.id.recycleRecite)
        progressLayout = findViewById(R.id.progressLayout)
        progressLayout.visibility = View.VISIBLE
        progressBar = findViewById(R.id.progressBar)
        toolbar = findViewById(R.id.toolbar)
        argmusicplayer = findViewById(R.id.argmusicplayer)
        fabplay = findViewById(R.id.fabplay)


        setUpRecyclerSurah()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_tool, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menusettings -> {
                askConfirmation()
            }
            else -> {
                super.onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)

    }

    private fun setUpToolbar(title: String) {
        setSupportActionBar(toolbar)
        supportActionBar?.title = title
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
    }

    private fun setUpRecyclerSurah() {
        recycleRecite = findViewById(R.id.recycleRecite)


        val queue = Volley.newRequestQueue(this)

        val url = "http://quran-endpoint.herokuapp.com/quran/${
            intent.getIntExtra(
                "num",
                1
            )
        }?imamId=${sharedPreferences.getInt("audio", 1)}"


        if (ConnectionManager().checkConnection(this)) {

            val jsonObjectRequest1 = object : JsonObjectRequest(
                Method.GET, url, null, Response.Listener {

                    progressLayout.visibility = View.GONE

                    try {
                        val data = it.getJSONObject("data")
                        val surahName =
                            data.getJSONObject("asma").getJSONObject("ar").getString("long")
                        val translation =
                            data.getJSONObject("asma").getJSONObject("translation").getString("en")

                        setUpToolbar("$surahName [$translation]")
                        val ayahs = data.getJSONArray("ayahs")
                        for (i in 0 until ayahs.length()) {
                            val arabicText =
                                ayahs.getJSONObject(i).getJSONObject("text").getString("ar")
                            val audiourl =
                                ayahs.getJSONObject(i).getJSONObject("audio").getString("url")
                            val numberInSurah =
                                ayahs.getJSONObject(i).getJSONObject("number").getInt("insurah")
                            val read =
                                ayahs.getJSONObject(i).getJSONObject("text").getString("read")
                            val audioClass = AudioClass(
                                intent.getIntExtra(
                                    "num",
                                    1
                                ),
                                audiourl,
                                arabicText,
                                read,
                                numberInSurah
                            )
                            aList.add(audioClass)
                        }

                        reciteSurahAdapter =
                            ReciteAudioAdapter(this, aList, argmusicplayer, fabplay)
                        val mLayoutManager: LinearLayoutManager =
                            LinearLayoutManager(this)
                        recycleRecite.layoutManager = mLayoutManager
                        recycleRecite.itemAnimator = DefaultItemAnimator()
                        recycleRecite.adapter = reciteSurahAdapter
                        recycleRecite.setHasFixedSize(true)


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
                }) {}
            queue.add(jsonObjectRequest1)
        } else openInternetDialog()
    }

    private fun openInternetDialog() {
        val dialog = android.app.AlertDialog.Builder(this)
        dialog.setTitle("Failure")
        dialog.setMessage("Internet Connection NOT Found")
        dialog.setPositiveButton("Open Settings")
        { _, _ ->
            val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
            startActivity(settingsIntent)
            finishAffinity()
        }
        dialog.setNegativeButton("Retry")
        { _, _ ->
            dialog.show().cancel()
        }

        dialog.create()
        dialog.show()
    }

    private fun askConfirmation() {
        val dialog = android.app.AlertDialog.Builder(this)
        dialog.setTitle("Confirmation")
        val msg = "Your selected Reciter is ${
            sharedPreferences.getString(
                "imam",
                "Abdul Basit Murattal"
            )
        }.Do you want to change it?"
        dialog.setMessage(msg)
        dialog.setPositiveButton("Change Reciter")
        { _, _ ->
            sharedPreferences.edit().putInt("audio", 0).apply()
            startActivity(Intent(this, AudioDisplayActivity::class.java))
        }
        dialog.setNegativeButton("Cancel")
        { _, _ ->
            dialog.show().cancel()
        }

        dialog.create()
        dialog.show()
    }
}