package com.aneeq.myquran.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.aneeq.myquran.R
import com.aneeq.myquran.adapter.browse.ReciteJuzAdapter
import com.aneeq.myquran.models.OriginalJuz
import com.aneeq.myquran.util.ConnectionManager
import org.json.JSONException

class ReciteJuzActivity : AppCompatActivity() {
    lateinit var rlsupreme: RelativeLayout
    lateinit var sharedPreferences: SharedPreferences
    lateinit var toolbar: Toolbar
    lateinit var btnPrev: Button
    lateinit var etCount: EditText
    lateinit var txtCount: TextView
    lateinit var txtCountFixed: TextView
    lateinit var txtSurah: TextView
    lateinit var btnNext: Button
    lateinit var recycleRecite: RecyclerView
    lateinit var progressLayout: RelativeLayout
    lateinit var progressBar: ProgressBar
    lateinit var recitejuzAdapter: ReciteJuzAdapter
    val oList = arrayListOf<OriginalJuz>()
    var orgjuzAyahs: OriginalJuz? = null
    var jpsList = arrayListOf<String>()
    var url2 = ""
    var c = 1
    var pos = 1

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recite_juz)
        sharedPreferences = getSharedPreferences("Messenger Preferences", Context.MODE_PRIVATE)
        btnPrev = findViewById(R.id.btnPrev)
        txtCount = findViewById(R.id.txtCount)
        txtCountFixed = findViewById(R.id.txtCountFixed)
        btnNext = findViewById(R.id.btnNext)
        txtSurah = findViewById(R.id.txtSurah)
        recycleRecite = findViewById(R.id.recycleRecite)
        progressLayout = findViewById(R.id.progressLayout)
        progressLayout.visibility = View.VISIBLE
        progressBar = findViewById(R.id.progressBar)
        toolbar = findViewById(R.id.toolbar)
        rlsupreme = findViewById(R.id.rlsupreme)
        etCount = findViewById(R.id.etCount)

        etCount.visibility = View.GONE





        c = intent.getIntExtra("page", 604)




        btnNext.setOnClickListener {
            progressLayout.visibility = View.VISIBLE
            jpsList.clear()
            oList.clear()
            setUpRecyclerSurah(++c)
            txtCount.text = "$c"
        }
        btnPrev.setOnClickListener {
            progressLayout.visibility = View.VISIBLE
            jpsList.clear()
            oList.clear()
            setUpRecyclerSurah(--c)
            txtCount.text = "$c"
        }
        txtCount.text = "$c"
        setUpRecyclerSurah(c)
        txtCount.setOnClickListener {
            txtCount.visibility = View.GONE
            etCount.visibility = View.VISIBLE
            etCount.setText(txtCount.text.toString())
            editortext()
        }


        ////////////////////////////////////////////////////////////////////////////////


        recycleRecite.setOnTouchListener(object : OnSwipeTouchListener(this@ReciteJuzActivity) {
            override fun onSwipeLeft() {

                if (c < 604) {
                    progressLayout.visibility = View.VISIBLE
                    jpsList.clear()
                    oList.clear()
                    setUpRecyclerSurah(++c)
                    txtCount.text = "$c"
                } else {
                    super.onSwipeLeft()
                }
            }

            override fun onSwipeRight() {
                if (c > 1) {
                    progressLayout.visibility = View.VISIBLE
                    jpsList.clear()
                    oList.clear()
                    setUpRecyclerSurah(--c)
                    txtCount.text = "$c"
                } else {
                    super.onSwipeRight()
                }
            }
        })
    }


    ////////////////////////////////////////////////////////////////////////////
    private fun editortext() {


        etCount.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {
                val pg = v.text.toString().toInt()
                if (pg < 1 || pg > 604) {
                    etCount.error = "Please Enter between [1 and 604]"
                    etCount.requestFocus()
                } else {
                    c = pg
                    etCount.visibility = View.GONE
                    txtCount.visibility = View.VISIBLE
                    txtCount.text = v.text.toString()
                    progressLayout.visibility = View.VISIBLE
                    jpsList.clear()
                    oList.clear()
                    setUpRecyclerSurah(pg)

                    (v.context.getSystemService(INPUT_METHOD_SERVICE)
                            as InputMethodManager)
                        .hideSoftInputFromWindow(v.windowToken, 0)

                    return@OnEditorActionListener true
                }
            }
            false
        })
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

    private fun setUpRecyclerSurah(c: Int) {
        recycleRecite = findViewById(R.id.recycleRecite)
        if (c == 1) btnPrev.visibility = View.GONE else btnPrev.visibility = View.VISIBLE
        if (c == 604) btnNext.visibility = View.GONE else btnNext.visibility = View.VISIBLE
        toolbarSetting(c)

        val queue = Volley.newRequestQueue(this)

        val url1 = "http://api.alquran.cloud/v1/page/$c/quran-simple"


        url2 = if (sharedPreferences.getBoolean("transstate", true)) {
            "http://api.alquran.cloud/v1/page/$c/${
                sharedPreferences.getString(
                    "identifier",
                    "en.maududi"
                )
            }"
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
                            //  setUpToolbar(data.getString("name") + "(" + data.getString("englishNameTranslation") + ")")
                            val ayahs = data.getJSONArray("ayahs")
                            for (i in 0 until ayahs.length()) {
                                val jsonObject = ayahs.getJSONObject(i)

                                orgjuzAyahs = OriginalJuz(
                                    jsonObject.getJSONObject("surah").getInt("number"),
                                    jsonObject.getInt("numberInSurah"),
                                    jsonObject.getString("text"),
                                    (i * 2) + j,
                                    j
                                )
                                oList.add(orgjuzAyahs!!)

                                val surah = jsonObject.getJSONObject("surah")
                                jpsList.add(surah.getString("name"))

                                if (jsonObject.getJSONObject("surah")
                                        .getInt("number") == intent.getIntExtra(
                                        "pos",
                                        114
                                    ) && jsonObject.getInt("numberInSurah") == 1
                                ) {
                                    pos = (i * 2)
                                }

                            }
                            displaySurahOnthisPage(jpsList)
                            val sortedList = oList.sortedWith(compareBy { it.key })

                            recitejuzAdapter =
                                ReciteJuzAdapter(
                                    this,
                                    sortedList,
                                    recycleRecite,
                                    intent.getBooleanExtra("isSurah", false)
                                )
                            val mLayoutManager: LinearLayoutManager =
                                LinearLayoutManager(this)
                            recycleRecite.layoutManager = mLayoutManager
                            recycleRecite.itemAnimator = DefaultItemAnimator()
                            recycleRecite.adapter = recitejuzAdapter
                            recycleRecite.setHasFixedSize(true)

//                            (recycleRecite.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(
//                                pos,
//                                0
//                            )
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

    private fun displaySurahOnthisPage(juzpgsr: ArrayList<String>) {
        val set = LinkedHashSet<String>()
        set.addAll(juzpgsr)
        txtSurah.text = set.toString()
    }

    private fun toolbarSetting(c: Int) {

        when (c) {
            in 1..21 -> setUpToolbar("Alif Laam Meem")
            in 22..41 -> setUpToolbar("Sayaqoolu")
            in 42..61 -> setUpToolbar("Tilkar-Rusul")
            in 62..81 -> setUpToolbar("Lan Tana Loo")
            in 82..101 -> setUpToolbar("Wal Mohsanaat")
            in 102..121 -> setUpToolbar("La Yuhibbullah")
            in 122..141 -> setUpToolbar("Wa Iza Samiu")
            in 142..161 -> setUpToolbar("Wa Lau Annana")
            in 162..181 -> setUpToolbar("Qaulal Malaoo")
            in 182..201 -> setUpToolbar("Wa'lamu")
            in 202..221 -> setUpToolbar("YaTazeeroon")
            in 222..241 -> setUpToolbar("Wa Ma Min Daabbah")
            in 242..261 -> setUpToolbar("Wa ma Ubarri-oo")
            in 262..281 -> setUpToolbar("Rubama")
            in 282..301 -> setUpToolbar("Subhanallazi")
            in 302..321 -> setUpToolbar("Qaula Alam")
            in 322..341 -> setUpToolbar("IqTaraba")
            in 342..361 -> setUpToolbar("Qad Aflaha")
            in 362..381 -> setUpToolbar("Wa Qaulallazina")
            in 382..401 -> setUpToolbar("Amman Qalaq")
            in 402..421 -> setUpToolbar("Utlu Maa Oohiya")
            in 422..441 -> setUpToolbar("Wa Maii-Yaqnut")
            in 442..461 -> setUpToolbar("WaMa-Liya")
            in 462..481 -> setUpToolbar("Faman Azlamu")
            in 482..501 -> setUpToolbar("Ilaihi Yuraddu")
            in 502..521 -> setUpToolbar("Haa Meem")
            in 522..541 -> setUpToolbar("Qaula Fa Ma khatbukum")
            in 542..561 -> setUpToolbar("Qad Sami-Ullah")
            in 562..581 -> setUpToolbar("Tabara-kallazi")
            in 582..604 -> setUpToolbar("Amma")

        }
    }

    override fun onBackPressed() {
        sharedPreferences.edit().putInt("resume", txtCount.text.toString().toInt()).apply()
        super.onBackPressed()
    }

}






