package com.aneeq.myquran.activity

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
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
import com.arges.sepan.argmusicplayer.Enums.AudioType
import com.arges.sepan.argmusicplayer.Models.ArgAudio
import com.arges.sepan.argmusicplayer.Models.ArgAudioList
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
    lateinit var txtwhois: TextView
    lateinit var rl2: RelativeLayout
    lateinit var btnDown: Button

    val aList = arrayListOf<AudioClass>()
    var audioClass: AudioClass? = null
    var surahref = ""
    var ayahref = ""
    var urlC = ""
    var currentIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recite_audio)
        sharedPreferences = getSharedPreferences("Messenger Preferences", Context.MODE_PRIVATE)
        recycleRecite = findViewById(R.id.recycleRecite)
        progressLayout = findViewById(R.id.progressLayout)
        btnDown = findViewById(R.id.btnDown)
        progressBar = findViewById(R.id.progressBar)
        toolbar = findViewById(R.id.toolbar)
        argmusicplayer = findViewById(R.id.argmusicplayer)
        fabplay = findViewById(R.id.fabplay)
        txtwhois = findViewById(R.id.txtwhois)
        rl2 = findViewById(R.id.rl2)

        rl2.visibility = View.VISIBLE

        val s = "You are listening to ${
            sharedPreferences.getString(
                "imam",
                "Abdul Basit Murattal"
            )
        }.\n To change the reciter, go to settings."
        txtwhois.text = s
        setUpRecycler()

//        fabplay.setOnClickListener {
//            playPlaylist()
//        }
        btnDown.setOnClickListener {
            downloadPlaylist()
        }

    }

    private fun downloadPlaylist() {
        val fileUri: Uri =
            Uri.parse("https://everyayah.com/data/Abdul_Basit_Murattal_64kbps/001002.mp3")
        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        val request = DownloadManager.Request(fileUri)

        request.setTitle("Audio Download")
        request.setDescription("Android Audio download using DownloadManager.")


        //Set the local destination for the downloaded file to a path
        //within the application's external files directory
        request.setDestinationInExternalPublicDir(
            Environment.DIRECTORY_DOWNLOADS + "/",
            "Abdul_Basit_Murattal_64kbps_001002.mp3"
        )
        downloadManager.enqueue(request)
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


    private fun setUpRecycler() {
        recycleRecite = findViewById(R.id.recycleRecite)

        val queue = Volley.newRequestQueue(this)

        val url = "http://api.alquran.cloud/v1/surah/${intent.getIntExtra("num", 1)}/quran-simple"

        if (ConnectionManager().checkConnection(this)) {

            val jsonObjectRequest1 = object : JsonObjectRequest(
                Method.GET, url, null, Response.Listener {

                    progressLayout.visibility = View.GONE


                    try {
                        val data = it.getJSONObject("data")
                        val ayahs = data.getJSONArray("ayahs")

                        setUpToolbar("${data.get("name")}[${data.get("englishNameTranslation")}]")
                        for (i in 0 until ayahs.length()) {
                            val jsonObject = ayahs.getJSONObject(i)
                            val surahNum = data.getInt("number")
                            val ayahNum = jsonObject.getInt("numberInSurah")

                            surahref = when {
                                surahNum < 10 -> "00${surahNum}"
                                surahNum in 10..99 -> "0${surahNum}"
                                else -> "$surahNum"
                            }

                            ayahref = when {
                                ayahNum < 10 -> "00${ayahNum}"
                                ayahNum in 10..99 -> "0${ayahNum}"
                                else -> "$ayahNum"
                            }
                            urlC = "https://everyayah.com/data/" +
                                    "${
                                        sharedPreferences.getString(
                                            "audio",
                                            "Abdul_Basit_Murattal_64kbps"
                                        )
                                            .toString()
                                    }/" +
                                    "$surahref$ayahref.mp3"

                            audioClass = AudioClass(
                                surahNum,
                                ayahNum,
                                jsonObject.getString("text"),
                                urlC, false
                            )
                            aList.add(audioClass!!)
                        }
                        reciteSurahAdapter =
                            ReciteAudioAdapter(
                                this, aList,
                                argmusicplayer, fabplay, recycleRecite
                            )
                        val mLayoutManager =
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
            sharedPreferences.edit().putString("audio", "").apply()
            startActivity(Intent(this, AudioDisplayActivity::class.java))
        }
        dialog.setNegativeButton("Cancel")
        { _, _ ->
            dialog.show().cancel()
        }

        dialog.create()
        dialog.show()
    }

    private fun playPlaylist() {
        val playlist = ArgAudioList(true)
        for (i in 0 until aList.size) {
            playlist.add(
                ArgAudio(
                    "",
                    "",
                    aList[i].audioUrl,
                    AudioType.URL
                )
            )
        }
        argmusicplayer.playPlaylist(playlist)

        argmusicplayer.setOnPlayingListener {
            currentIndex = argmusicplayer.currentAudio.id
            (recycleRecite.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(
                argmusicplayer.currentAudio.id,
                0
            )

            Log.d("cp", "${argmusicplayer.currentAudio.id}")
        }
        argmusicplayer.setOnErrorListener { errorType, description ->

            argmusicplayer.playPlaylistItem(currentIndex - 1)
            argmusicplayer.loadPlaylist(playlist)
            argmusicplayer.playLoadedPlaylist()
        }
    }
}
