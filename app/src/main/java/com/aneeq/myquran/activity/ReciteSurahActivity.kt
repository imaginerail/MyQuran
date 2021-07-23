package com.aneeq.myquran.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.aneeq.myquran.R
import com.aneeq.myquran.adapter.ReciteSurahAdapter
import com.aneeq.myquran.models.OriginalAyahs
import com.aneeq.myquran.util.ConnectionManager
import org.json.JSONException


class ReciteSurahActivity : AppCompatActivity() {
    lateinit var sharedPreferences: SharedPreferences
    lateinit var toolbar: Toolbar
    lateinit var btnPrev: Button
    lateinit var txtCount: TextView
    lateinit var btnNext: Button
    lateinit var recycleRecite: RecyclerView
    lateinit var progressLayout: RelativeLayout
    lateinit var progressBar: ProgressBar
    lateinit var reciteSurahAdapter: ReciteSurahAdapter
    val oList = arrayListOf<OriginalAyahs>()
    var orgsurahAyahs: OriginalAyahs? = null
    var url2 = ""
    //var c by Delegates.notNull<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recite_surah)
        sharedPreferences = getSharedPreferences("Messenger Preferences", Context.MODE_PRIVATE)
        btnPrev = findViewById(R.id.btnPrev)
        txtCount = findViewById(R.id.txtCount)
        btnNext = findViewById(R.id.btnNext)
        recycleRecite = findViewById(R.id.recycleRecite)
        progressLayout = findViewById(R.id.progressLayout)
        progressLayout.visibility = View.VISIBLE
        progressBar = findViewById(R.id.progressBar)
        toolbar = findViewById(R.id.toolbar)

        var c = intent.getIntExtra("surahnumber", 114)




        btnNext.setOnClickListener {
            progressLayout.visibility = View.VISIBLE
            oList.clear()
            setUpRecyclerSurah(++c)
            txtCount.text = "$c/114"
        }
        btnPrev.setOnClickListener {
            progressLayout.visibility = View.VISIBLE
            oList.clear()
            setUpRecyclerSurah(--c)
            txtCount.text = "$c/114"
        }
        txtCount.text = "$c/114"
        setUpRecyclerSurah(c)
    }

    private fun setUpToolbar(title: String) {
        setSupportActionBar(toolbar)
        supportActionBar?.title = title
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_tool, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menusettings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
            }
            else -> {
                super.onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)

    }

    private fun setUpRecyclerSurah(c: Int) {
        recycleRecite = findViewById(R.id.recycleRecite)
        if (c == 1) btnPrev.visibility = View.GONE else btnPrev.visibility = View.VISIBLE
        if (c == 114) btnNext.visibility = View.GONE else btnNext.visibility = View.VISIBLE


        val queue = Volley.newRequestQueue(this)

        val url1 = "http://api.alquran.cloud/v1/surah/$c"
        url2 = if (sharedPreferences.getBoolean("transstate", true)) {
            "http://api.alquran.cloud/v1/surah/$c/${sharedPreferences.getString(
                "identifier",
                "en.maududi"
            )}"
        } else {
            ""
        }


        val url = arrayOf(url1, url2)

        if (ConnectionManager().checkConnection(this)) {

            for (j in url.indices) {

                val jsonObjectRequest1 = object : JsonObjectRequest(
                    Method.GET, url[j], null, Response.Listener {

                        progressLayout.visibility = View.GONE

                        try {
                            val data = it.getJSONObject("data")
                            setUpToolbar(data.getString("name") + "(" + data.getString("englishNameTranslation") + ")")
                            val jsonArray = data.getJSONArray("ayahs")
                            for (i in 0 until jsonArray.length()) {
                                val jsonObject = jsonArray.getJSONObject(i)
                                orgsurahAyahs = OriginalAyahs(
                                    jsonObject.getString("text"),
                                    jsonObject.getInt("numberInSurah"),
                                    (i * 2) + j,
                                    j

                                )
                                oList.add(orgsurahAyahs!!)
                                //comboList.add(Combo((i * 2) + 1, 1, dupsurahAyahs!!))
                            }

                            val sortedList = oList.sortedWith(compareBy { it.key })

                            reciteSurahAdapter =
                                ReciteSurahAdapter(this, sortedList)
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
//                        Toast.makeText(
//                            this,
//                            "Volley Error Occurred",
//                            Toast.LENGTH_LONG
//                        )
//                            .show()
                    }) {}
                queue.add(jsonObjectRequest1)
            }
        } else openInternetDialog()
    }

    private fun openConfirmDialog() {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Message")
        dialog.setMessage("Are you sure you want to change the Edition?")
        dialog.setPositiveButton("Yes") { _, _ ->
            sharedPreferences.edit().clear().apply()
            startActivity(Intent(this, LanguagesActivity::class.java))
        }
        dialog.setNegativeButton("No") { _, _ ->
            dialog.show().cancel()
        }
        dialog.create()
        dialog.show()

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
        dialog.setNegativeButton("Exit")
        { _, _ ->
            ActivityCompat.finishAffinity(this)
        }
        dialog.create()
        dialog.show()
    }

}