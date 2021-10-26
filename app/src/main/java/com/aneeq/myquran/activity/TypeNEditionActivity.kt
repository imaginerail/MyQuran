package com.aneeq.myquran.activity

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.aneeq.myquran.R
import com.aneeq.myquran.adapter.settings.TNEAdapter
import com.aneeq.myquran.models.Editions
import com.aneeq.myquran.util.ConnectionManager
import org.json.JSONException

class TypeNEditionActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    lateinit var recycleTNE: RecyclerView
    lateinit var progressLayout: RelativeLayout
    lateinit var tneRecyclerAdapter: TNEAdapter
    lateinit var progressBar: ProgressBar
    lateinit var txtSelectType: TextView
    lateinit var txtSelectEd: TextView
    lateinit var spinner: Spinner
    lateinit var txtNotice: TextView
    lateinit var txtNotAvail: TextView
    var type: String = ""
    val tneList = arrayListOf<Editions>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_type_n_edition)
        txtSelectType = findViewById(R.id.txtSelectType)
        txtSelectEd = findViewById(R.id.txtSelectEd)
        txtNotice = findViewById(R.id.txtNotice)
        txtNotAvail = findViewById(R.id.txtNotAvail)
        progressLayout = findViewById(R.id.progressLayout)
        progressBar = findViewById(R.id.progressBar)
        progressLayout.visibility = View.VISIBLE
        txtNotAvail.visibility = View.GONE
        spinner = findViewById(R.id.spinner)

        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.type,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = this


    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        //
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        progressLayout.visibility = View.VISIBLE
        type = p0?.getItemAtPosition(p2).toString()
        setUpRecycler(type)
    }

    private fun setUpRecycler(mytype: String) {
        recycleTNE = findViewById(R.id.recycleTNE)
        val queue = Volley.newRequestQueue(this)
        val getLan = intent.getStringExtra("selectLang")
        val url = "http://api.alquran.cloud/v1/edition/language/$getLan"

        if (ConnectionManager().checkConnection(this)) {

//creating a json object
            val jsonObjectRequest = object : JsonObjectRequest(
                Method.GET, url, null, Response.Listener {

                    progressLayout.visibility = View.GONE

                    try {
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
                            tneRecyclerAdapter =
                                TNEAdapter(
                                    this,
                                    tneList,
                                    mytype
                                )
                            val mLayoutManager: LinearLayoutManager =
                                LinearLayoutManager(this)
                            recycleTNE.layoutManager = mLayoutManager
                            recycleTNE.itemAnimator = DefaultItemAnimator()
                            recycleTNE.adapter = tneRecyclerAdapter
                            recycleTNE.setHasFixedSize(true)
                        }
                        if (tneRecyclerAdapter.itemCount == 0) txtNotAvail.visibility = View.VISIBLE
                        else txtNotAvail.visibility = View.GONE

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
    private fun openInternetDialog(){
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