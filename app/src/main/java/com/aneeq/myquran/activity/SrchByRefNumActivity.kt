package com.aneeq.myquran.activity

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

import com.aneeq.myquran.R
import com.aneeq.myquran.adapter.*
import com.aneeq.myquran.database.favouritesdatabase.FavouritesDatabase
import com.aneeq.myquran.database.favouritesdatabase.FavouritesEntity
import com.aneeq.myquran.database.seleddatabase.SelDatabase
import com.aneeq.myquran.database.seleddatabase.SelEntity
import com.aneeq.myquran.fragment.RetrieveFavourites
import com.aneeq.myquran.models.Editions
import com.aneeq.myquran.models.SearchAyahResults
import com.aneeq.myquran.util.ConnectionManager
import org.json.JSONException

class SrchByRefNumActivity : AppCompatActivity() {
    lateinit var recycleEditions: RecyclerView
    lateinit var txtNotAvail: TextView
    lateinit var selectMultEditionAdapter: SelectMultEditionAdapter

    lateinit var recycleSSE: RecyclerView
    lateinit var showSelectedEditionAdapter: ShowSelectedEditionAdapter

    lateinit var recycleAyahs: RecyclerView
    lateinit var searchAyahResultsAdapter: SearchAyahResultsAdapter

    lateinit var txtClr: TextView
    lateinit var txtSSE: TextView
    lateinit var scrollView: ScrollView
    lateinit var llfilter: LinearLayout
    lateinit var toolbar: Toolbar
    lateinit var txtTRBF: TextView
    lateinit var txtSME: TextView
    lateinit var etAyahNum: EditText
    lateinit var etSurahNum: EditText
    lateinit var txtLanguage: TextView
    lateinit var txtFormat: TextView
    lateinit var txtSelectType: TextView
    lateinit var txtIsto: TextView
    lateinit var btnfilter: Button
    lateinit var spinner1: Spinner
    lateinit var spinner2: Spinner
    lateinit var spinner3: Spinner
    lateinit var btnsearch: Button
    lateinit var txtayahdetails: TextView
    var sarList = arrayListOf<SearchAyahResults>()
    val tneList = arrayListOf<Editions>()
    var seled = arrayListOf<SelEntity>()
    var langCodes = arrayOf<String>()
    var lan = "ar"
    var form = "text"
    var type = "translation"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_srch_by_ref_num)
        txtayahdetails = findViewById(R.id.txtayahdetails)
        txtClr = findViewById(R.id.txtClr)
        txtSSE = findViewById(R.id.txtSSE)
        btnfilter = findViewById(R.id.btnfilter)
        scrollView = findViewById(R.id.scrollView)
        llfilter = findViewById(R.id.llfilter)
        toolbar = findViewById(R.id.toolbar)
        txtSME = findViewById(R.id.txtSME)
        txtTRBF = findViewById(R.id.txtTRBF)
        txtIsto = findViewById(R.id.txtIsto)
        txtLanguage = findViewById(R.id.txtLanguage)
        txtFormat = findViewById(R.id.txtFormat)
        txtSelectType = findViewById(R.id.txtSelectType)
        etAyahNum = findViewById(R.id.etAyahNum)
        etSurahNum = findViewById(R.id.etSurahNum)
        txtNotAvail = findViewById(R.id.txtNotAvail)
        btnsearch = findViewById(R.id.btnsearch)
        spinner1 = findViewById(R.id.spinner1)
        spinner2 = findViewById(R.id.spinner2)
        spinner3 = findViewById(R.id.spinner3)
        recycleAyahs = findViewById(R.id.recycleAyahs)
        recycleSSE = findViewById(R.id.recycleSSE)
        txtNotAvail.visibility = View.GONE
        llfilter.visibility = View.GONE
        btnfilter.visibility = View.GONE
        seled.clear()
        seled = RetrieveSelEds(this).execute().get() as ArrayList<SelEntity>
        txtSME.setOnClickListener {

            if (llfilter.visibility != View.VISIBLE && btnfilter.visibility != View.VISIBLE) {
                llfilter.visibility = View.VISIBLE
                btnfilter.visibility = View.VISIBLE
            } else {
                llfilter.visibility = View.GONE
                btnfilter.visibility = View.GONE
            }

        }

        txtClr.setOnClickListener {
            ClearAll(this).execute()
            setUpShowSelectedRecycler()
        }

        txtSSE.setOnClickListener {
            setUpShowSelectedRecycler()
        }

        setupSpinner1()
        setupSpinner2()
        setupSpinner3()
        setupToolBar()

        btnfilter.setOnClickListener {
            setUpEditionRecycler(lan, form, type)
        }

        btnsearch.setOnClickListener {
            if (etSurahNum.text.isEmpty() && etAyahNum.text.isEmpty()) {
                Toast.makeText(
                    this,
                    "Enter the Reference first",
                    Toast.LENGTH_LONG
                )
                    .show()
                etSurahNum.error = "Surah No. is required"
                etSurahNum.requestFocus()
                etAyahNum.error = "Ayah No. is required"
                etAyahNum.requestFocus()
            } else {
                sarList.clear()
                if (seled.isEmpty()) singleSearch()
                else multipleSearch()

            }
        }

    }

    private fun singleSearch() {
        DisplayDetails()

        val queue = Volley.newRequestQueue(this)
        val url = "http://api.alquran.cloud/v1/ayah/${etSurahNum.text}:${etAyahNum.text}"

        if (ConnectionManager().checkConnection(this)) {

//creating a json object
            val jsonObjectRequest = object : JsonObjectRequest(
                Method.GET, url, null, Response.Listener {


                    try {
                        val data = it.getJSONObject("data")
                        val searchAyahResults = SearchAyahResults(
                            data.getInt("number"),
                            data.getJSONObject("edition").getString("identifier"),
                            data.getString("text")
                        )
                        sarList.add(searchAyahResults)


                        if (this != null) {
                            searchAyahResultsAdapter =
                                SearchAyahResultsAdapter(
                                    this,
                                    sarList
                                )
                            val mLayoutManager =
                                LinearLayoutManager(this)
                            recycleAyahs.layoutManager = mLayoutManager
                            recycleAyahs.itemAnimator = DefaultItemAnimator()
                            recycleAyahs.adapter = searchAyahResultsAdapter
                            recycleAyahs.setHasFixedSize(true)
                            recycleAyahs.isNestedScrollingEnabled = false
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

            openInternetDialog()
        }


    }

    private fun multipleSearch() {
        DisplayDetails()

        var multiple: String = seled[0].id

        for (i in 1 until seled.size) {
            multiple += ",${seled[i].id}"
        }

        val queue = Volley.newRequestQueue(this)
        val url =
            "http://api.alquran.cloud/v1/ayah/${etSurahNum.text}:${etAyahNum.text}/editions/$multiple"
        Log.d("srn", multiple)
        if (ConnectionManager().checkConnection(this)) {

//creating a json object
            val jsonObjectRequest = object : JsonObjectRequest(
                Method.GET, url, null, Response.Listener {


                    try {
                        val data = it.getJSONArray("data")
                        for (i in 0 until data.length()) {
                            val jsonObject = data.getJSONObject(i)
                            val searchAyahResults = SearchAyahResults(
                                jsonObject.getInt("number"),
                                jsonObject.getJSONObject("edition").getString("identifier"),
                                jsonObject.getString("text")
                            )
                            sarList.add(searchAyahResults)
                        }



                        if (this != null) {
                            searchAyahResultsAdapter =
                                SearchAyahResultsAdapter(
                                    this,
                                    sarList
                                )
                            val mLayoutManager =
                                LinearLayoutManager(this)
                            recycleAyahs.layoutManager = mLayoutManager
                            recycleAyahs.itemAnimator = DefaultItemAnimator()
                            recycleAyahs.adapter = searchAyahResultsAdapter
                            recycleAyahs.setHasFixedSize(true)
                            recycleAyahs.isNestedScrollingEnabled = false
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

            openInternetDialog()
        }


    }

    private fun DisplayDetails() {
        val queue = Volley.newRequestQueue(this)
        val url = "http://api.alquran.cloud/v1/ayah/${etSurahNum.text}:${etAyahNum.text}"

        if (ConnectionManager().checkConnection(this)) {

//creating a json object
            val jsonObjectRequest = object : JsonObjectRequest(
                Method.GET, url, null, Response.Listener {


                    try {
                        if (it.getString("status") == "Not Found") Toast.makeText(
                            this,
                            it.getString("data"),
                            Toast.LENGTH_LONG
                        ).show()
                        else {
                            val data = it.getJSONObject("data")
                            val surah = data.getJSONObject("surah")
                            var sajda = ""
                            sajda = if (data.getBoolean("sajda")) "Yes"
                            else "No"

                            txtayahdetails.text = "Details:\n" +
                                    "It is the Ayah no.${data.getInt("numberInSurah")} of Surah no. ${surah.getInt(
                                        "number"
                                    )}" +
                                    " which is Surah ${surah.getString("name")}(${surah.getString("englishName")}) translated as '${surah.getString(
                                        "englishNameTranslation"
                                    )}'" +
                                    "This is a ${surah.getString("revelationType")} surah having a total of ${surah.getInt(
                                        "numberOfAyahs"
                                    )} Ayahs." +
                                    "This Surah is taken from Juz no. ${data.getInt("juz")} and Manzil no. ${data.getInt(
                                        "manzil"
                                    )}." +
                                    "Is this the Ayah of Sajdah? $sajda"


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

            openInternetDialog()
        }
    }

    private fun setUpShowSelectedRecycler() {
        seled = RetrieveSelEds(this).execute().get() as ArrayList<SelEntity>

        if (seled.isEmpty()) Toast.makeText(
            this,
            "Please Select a Edition First",
            Toast.LENGTH_SHORT
        ).show()

        if (this != null) {
            showSelectedEditionAdapter =
                ShowSelectedEditionAdapter(
                    this,
                    seled

                )
            val mLayoutManager =
                GridLayoutManager(this, 4)
            recycleSSE.layoutManager = mLayoutManager
            recycleSSE.itemAnimator = DefaultItemAnimator()
            recycleSSE.adapter = showSelectedEditionAdapter
            recycleSSE.setHasFixedSize(true)
            recycleSSE.isNestedScrollingEnabled = false
        }

    }


    private fun setUpEditionRecycler(lan: String, form: String, type: String) {
        tneList.clear()
        recycleEditions = findViewById(R.id.recycleEditions)
        val queue = Volley.newRequestQueue(this)
        val url = "http://api.alquran.cloud/v1/edition?format=$form&language=$lan&type=$type"

        if (ConnectionManager().checkConnection(this)) {

//creating a json object
            val jsonObjectRequest = object : JsonObjectRequest(
                Method.GET, url, null, Response.Listener {


                    try {
                        if (it.getString("data") == "Invalid format") txtNotAvail.visibility =
                            View.VISIBLE
                        else txtNotAvail.visibility = View.GONE


                        val jsonArray = it.getJSONArray("data")
                        for (i in 0 until jsonArray.length()) {
                            val jsonObject = jsonArray.getJSONObject(i)
                            val editions = Editions(
                                jsonObject.getString("identifier"),
                                jsonObject.getString("language"),
                                jsonObject.getString("name"),
                                jsonObject.getString("englishName"),
                                jsonObject.getString("format"),
                                jsonObject.getString("type")
                            )
                            tneList.add(editions)
                        }
                        if (this != null) {
                            selectMultEditionAdapter =
                                SelectMultEditionAdapter(
                                    this,
                                    tneList
                                )
                            val mLayoutManager =
                                GridLayoutManager(this, 3)
                            recycleEditions.layoutManager = mLayoutManager
                            recycleEditions.itemAnimator = DefaultItemAnimator()
                            recycleEditions.adapter = selectMultEditionAdapter
                            recycleEditions.setHasFixedSize(true)
                            recycleEditions.isNestedScrollingEnabled = false
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

            openInternetDialog()
        }

    }

    private fun setupSpinner3() {
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.type,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner3.adapter = adapter
        spinner3.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                //
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                type = p0?.getItemAtPosition(p2).toString()


            }

        }
    }

    private fun setupSpinner2() {
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.format,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner2.adapter = adapter
        spinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                //
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                form = p0?.getItemAtPosition(p2).toString()


            }

        }

    }

    private fun setupSpinner1() {
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.language,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner1.adapter = adapter
        spinner1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                //
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                langCodes = arrayOf("ar", "en", "hi", "fa", "id", "ja", "pl", "ru", "tr", "ur")
                lan = langCodes[p2]


            }

        }


    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onBackPressed()
        return super.onOptionsItemSelected(item)
    }


    private fun setupToolBar() {
        toolbar.title = "Search By Reference Number"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

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

class RetrieveSelEds(val context: Context) : AsyncTask<Void, Void, List<SelEntity>>() {
    override fun doInBackground(vararg p0: Void?): List<SelEntity> {
        val db = Room.databaseBuilder(context, SelDatabase::class.java, "sel-db").build()
        return db.restaurantDao().getAllSelEdns()
    }

}

class ClearAll(val context: Context) : AsyncTask<Void, Void, Int>() {
    override fun doInBackground(vararg p0: Void?): Int {
        val db = Room.databaseBuilder(context, SelDatabase::class.java, "sel-db").build()
        return db.restaurantDao().clearAll()
    }

}