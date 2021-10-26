package com.aneeq.myquran.activity

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.aneeq.myquran.R
import com.aneeq.myquran.adapter.search.SearchTextResultsAdapter
import com.aneeq.myquran.models.SearchTextResults
import com.aneeq.myquran.util.ConnectionManager
import com.chivorn.smartmaterialspinner.SmartMaterialSpinner
import com.mannan.translateapi.Language
import com.mannan.translateapi.TranslateAPI
import com.skydoves.powerspinner.IconSpinnerAdapter
import com.skydoves.powerspinner.IconSpinnerItem
import com.skydoves.powerspinner.PowerSpinnerView
import org.json.JSONException
import java.util.*
import kotlin.collections.ArrayList


class SrchByTxtActivity : AppCompatActivity() {

    private lateinit var search_surah: SmartMaterialSpinner<String>

    private var provinceList: MutableList<String>? = null

    lateinit var toolbar: Toolbar
    lateinit var scrollView: ScrollView

    lateinit var txtlang: TextView
    lateinit var psvogLang: PowerSpinnerView


    lateinit var txtet: TextView
    lateinit var psvEd: PowerSpinnerView

    lateinit var txtsr: TextView

    lateinit var etsrchkey: EditText
    private lateinit var btntrsr: Button
    lateinit var txtct: TextView

    private lateinit var rl1: RelativeLayout
    lateinit var recyclesearchresults: RecyclerView

    lateinit var noLayout: RelativeLayout
    private lateinit var txtNotAvail: TextView

    lateinit var progressLayout: RelativeLayout
    lateinit var progressBar: ProgressBar
    lateinit var searchTextResultsAdapter: SearchTextResultsAdapter

    var sarList = arrayListOf<SearchTextResults>()
    var slList = arrayListOf<String>()
    private var langarrayindex = 0
    var par1 = ""
    var par2 = "all"
    var par3 = ""
    var type = "translation"
    var gc = 0

    lateinit var btnPrev: Button
    lateinit var txtCount: TextView
    lateinit var txtCountFixed: TextView
    lateinit var btnNext: Button
    lateinit var ll786: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_srch_by_txt)

        btnPrev = findViewById(R.id.btnPrev)
        txtCount = findViewById(R.id.txtCount)
        txtCountFixed = findViewById(R.id.txtCountFixed)
        btnNext = findViewById(R.id.btnNext)
        ll786 = findViewById(R.id.ll786)


        txtct = findViewById(R.id.txtct)
        toolbar = findViewById(R.id.toolbar)
        scrollView = findViewById(R.id.scrollView)
        txtlang = findViewById(R.id.txtlang)
        psvogLang = findViewById(R.id.psvogLang)
        txtet = findViewById(R.id.txtet)
        psvEd = findViewById(R.id.psvEd)
        txtsr = findViewById(R.id.txtsr)
        search_surah = findViewById(R.id.search_surah)
        etsrchkey = findViewById(R.id.etsrchkey)
        btntrsr = findViewById(R.id.btntrsr)
        rl1 = findViewById(R.id.rl1)
        recyclesearchresults = findViewById(R.id.recyclesearchresults)
        noLayout = findViewById(R.id.noLayout)
        txtNotAvail = findViewById(R.id.txtNotAvail)
        progressLayout = findViewById(R.id.progressLayout)
        progressBar = findViewById(R.id.progressBar)


        ll786.visibility = View.GONE

        psvogLang.visibility = View.VISIBLE
        psvEd.visibility = View.VISIBLE
        noLayout.visibility = View.GONE
        setupToolBar()
        setupSpinnerLang()
        setUpSmartSurahSearchSpinner()

        btntrsr.setOnClickListener {
            sarList.clear()
            editTranslate(langarrayindex)

        }

        var cpg = 1
        btnNext.setOnClickListener {
            sarList.clear()
            val j: Int = if (gc > 10) {
                if (gc % 10 == 0)
                    gc / 10
                else
                    gc / 10 + 1
            } else 1
            if (cpg < j) {
                cpg += 1
                progressLayout.visibility = View.VISIBLE
                displayResults(par1, par2, par3, cpg)
            } else return@setOnClickListener

        }
        btnPrev.setOnClickListener {
            sarList.clear()
            if (cpg > 1) {
                cpg -= 1
                progressLayout.visibility = View.VISIBLE
                displayResults(par1, par2, par3, cpg)
            } else return@setOnClickListener


        }


    }


    private fun setupToolBar() {
        toolbar.title = "Search By Text"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onBackPressed()
        return super.onOptionsItemSelected(item)
    }

    private fun setupIconspinnerItemEditions(mine: ArrayList<String>): ArrayList<IconSpinnerItem> {
        val iconedList = ArrayList<IconSpinnerItem>()
        for (i in 0 until mine.size) {
            iconedList.add(IconSpinnerItem(text = mine[i]))
        }
        return iconedList
    }

    private fun setupSpinnerLang() {
        psvogLang.apply {
            setSpinnerAdapter(IconSpinnerAdapter(this))
            setItems(
                arrayListOf(
                    IconSpinnerItem(text = "ENGLISH"),
                    IconSpinnerItem(text = "ARABIC"),
                    IconSpinnerItem(text = "HINDI"),
                    IconSpinnerItem(text = "INDONESIAN"),
                    IconSpinnerItem(text = "URDU"),

                    )
            )
            setOnSpinnerItemSelectedListener<IconSpinnerItem> { _, _, pos, item ->
                langarrayindex = pos
                setUpEditions(item.text.toString())
            }
            getSpinnerRecyclerView().layoutManager = GridLayoutManager(baseContext, 1)
            // selectItemByIndex(4)
            preferenceName = "language"
        }


    }

    private fun setupSpinnerEd(iconic: ArrayList<IconSpinnerItem>) {
        psvEd.apply {
            setSpinnerAdapter(IconSpinnerAdapter(this))
            setItems(
                iconic
            )
            setOnSpinnerItemSelectedListener<IconSpinnerItem> { _, _, pos, item ->
                par3 = item.text.toString()
            }
            getSpinnerRecyclerView().layoutManager = GridLayoutManager(baseContext, 2)
            // selectItemByIndex(4)
            // preferenceName = "edition"
        }

    }


    private fun setUpEditions(ed: String): ArrayList<String> {
        var editionList = ArrayList<String>()
        when (ed) {
            "ENGLISH" -> editionList = arrayListOf(
                "en",
                "en.ahmed",
                "en.asad",
                "en.pickthall",
                "en.sahih",
                "en.maududi",
                "en.yusufali"
            )
            "ARABIC" -> editionList = arrayListOf(
                "ar",
                "quran-buck"
            )
            "HINDI" -> editionList = arrayListOf(
                "hi",
                "hi.hindi",
                "hi.farooq"
            )
            "INDONESIAN" -> editionList = arrayListOf(
                "id",
                "id.indonesian"
            )
            "URDU" -> editionList = arrayListOf(
                "ur",
                "ur.ahmedali",
                "ur.jalandhr",
                "ur.qadri",
                "ur.junagarhi",
                "ur.maududi"

            )
        }
        setupSpinnerEd(setupIconspinnerItemEditions(editionList))
        return editionList
    }

    private fun editTranslate(p: Int) {
        val standardLangs = arrayOf(
            Language.ENGLISH,
            Language.ARABIC,
            Language.HINDI,
            Language.INDONESIAN,
            Language.URDU
        )
        if (etsrchkey.text.isEmpty()) {
            etsrchkey.error = "Please enter something first!"
            etsrchkey.requestFocus()
        } else {
            val translateAP =
                TranslateAPI(Language.AUTO_DETECT, standardLangs[p], etsrchkey.text.toString())
            translateAP.setTranslateListener(object : TranslateAPI.TranslateListener {
                override fun onSuccess(translatedText: String?) {
                    etsrchkey.setOnEditorActionListener { textView, i, _ ->

                        if (i == EditorInfo.IME_ACTION_DONE) {
                            textView.text = translatedText
                            val imm: InputMethodManager =
                                textView.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                            imm.hideSoftInputFromWindow(textView.windowToken, 0)
                            true
                        } else {
                            false
                        }

                    }
                    par1 = translatedText!!
                    displayResults(
                        translatedText,
                        par2,
                        par3,
                        1
                    )
                    ll786.visibility = View.VISIBLE
                    Log.d("par", "$translatedText $par3")
                }

                override fun onFailure(ErrorText: String?) {
                    etsrchkey.text.clear()
                    Toast.makeText(
                        this@SrchByTxtActivity,
                        "Couldn't Translate, Please Try Again",
                        Toast.LENGTH_SHORT
                    )
                        .show()

                }

            })
        }
    }

    private fun displayResults(par1: String, par2: String, par3: String, pgno: Int) {
        /*
        par1=edition
        par2=surah name
        par3=search word
        */
        val queue = Volley.newRequestQueue(this)
        val url = "http://api.alquran.cloud/v1/search/$par1/$par2/$par3"

        if (ConnectionManager().checkConnection(this)) {

//creating a json object
            val jsonObjectRequest = object : JsonObjectRequest(
                Method.GET, url, null, Response.Listener {
                    noLayout.visibility = View.GONE
                    progressLayout.visibility = View.GONE
                    try {
                        val data = it.getJSONObject("data")
                        val count = data.getInt("count")
                        gc = count
                        txtct.text = "Count : $count"
                        if (count > 10) {
                            if (count % 10 == 0) txtCountFixed.text = "${(count / 10)}"
                            else txtCountFixed.text = "${(count / 10 + 1)}"
                        } else txtCountFixed.text = "${1}"

                        txtCount.text = "$pgno"
                        val k = (pgno * 10) - 10
                        val j: Int = if (count > 10) {
                            if (count > (pgno * 10 - 1))
                                pgno * 10 - 1
                            else
                                count - 1
                        } else count - 1

                        val matches = data.getJSONArray("matches")
                        for (i in k..j) {
                            val jsonObject = matches.getJSONObject(i)
                            val searchTextResults = SearchTextResults(
                                jsonObject.getString("text"),
                                jsonObject.getJSONObject("edition").getString("name"),
                                jsonObject.getJSONObject("edition").getString("englishName"),
                                jsonObject.getJSONObject("surah").getInt("number"),
                                jsonObject.getJSONObject("surah").getString("name"),
                                jsonObject.getJSONObject("surah").getString("englishName"),
                                jsonObject.getJSONObject("surah")
                                    .getString("englishNameTranslation"),
                                jsonObject.getJSONObject("surah").getString("revelationType"),
                                jsonObject.getInt("numberInSurah")
                            )
                            sarList.add(searchTextResults)
                        }
                        searchTextResultsAdapter =
                            SearchTextResultsAdapter(
                                this,
                                sarList, par1
                            )

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            scrollView.isNestedScrollingEnabled = false
                        }

                        val mLayoutManager: LinearLayoutManager =
                            LinearLayoutManager(this)
                        recyclesearchresults.layoutManager = mLayoutManager
                        recyclesearchresults.itemAnimator = DefaultItemAnimator()
                        recyclesearchresults.adapter = searchTextResultsAdapter
                        recyclesearchresults.setHasFixedSize(true)

                    } catch (e: JSONException) {
                        e.printStackTrace()
                        Toast.makeText(
                            this,
                            e.message,
                            Toast.LENGTH_LONG
                        )
                            .show()
                        noLayout.visibility = View.VISIBLE
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

            openInternetDialog()
        }
    }

    private fun setUpSmartSurahSearchSpinner() {
        provinceList = setUpSurahAdapter()
        search_surah.item = provinceList
        search_surah.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                par2 = (position + 1).toString()

            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {}
        }

    }

    private fun setUpSurahAdapter(): ArrayList<String> {

        val queue = Volley.newRequestQueue(this)
        val url = "http://api.alquran.cloud/v1/surah"

        if (ConnectionManager().checkConnection(this)) {

//creating a json object
            val jsonObjectRequest = object : JsonObjectRequest(
                Method.GET, url, null, Response.Listener {

                    progressLayout.visibility = View.GONE

                    try {

                        val data = it.getJSONArray("data")
                        for (i in 0 until data.length()) {
                            slList.add(
                                data.getJSONObject(i).getString("englishName").replace("-", " ")
                            )
                        }

                        //attach dashboard fragment to adapter(bridge between data and view)
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
            openInternetDialog()


        }
        return slList
    }

    private fun openInternetDialog() {
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
