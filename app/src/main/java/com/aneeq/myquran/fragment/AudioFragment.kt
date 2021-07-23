package com.aneeq.myquran.fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.aneeq.myquran.R
import com.aneeq.myquran.adapter.*
import com.aneeq.myquran.models.JuzList
import com.aneeq.myquran.models.SurahList
import com.aneeq.myquran.util.ConnectionManager
import com.google.android.material.tabs.TabLayout
import com.mig35.carousellayoutmanager.CarouselLayoutManager
import com.mig35.carousellayoutmanager.CenterScrollListener
import org.json.JSONException

class AudioFragment : Fragment() {
    lateinit var txtSelectSurah: TextView
    lateinit var recycleSurahList: RecyclerView
    lateinit var txtSelectJuz: TextView
    lateinit var recycleJuzList: RecyclerView
    private lateinit var surahListAudioAdapter: SurahListAudioAdapter
    private lateinit var juzListAudioAdapter: JuzListAudioAdapter
    val surahList = arrayListOf<SurahList>()
    val juzList = arrayListOf<JuzList>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_audio, container, false)
        txtSelectSurah = view.findViewById(R.id.txtSelectSurah)
        txtSelectJuz = view.findViewById(R.id.txtSelectJuz)
        val juzList = arrayListOf(
            JuzList("1", "Alif Laam Meem", 1),
            JuzList("2", "Sayaqoolu", 22),
            JuzList("3", "Tilkar-Rusul", 42),
            JuzList("4", "Lan Tana Loo", 62),
            JuzList("5", "Wal Mohsanaat", 82),
            JuzList("6", "La Yuhibbullah", 102),
            JuzList("7", "Wa Iza Samiu", 122),
            JuzList("8", "Wa Lau Annana", 142),
            JuzList("9", "Qaulal Malaoo", 162),
            JuzList("10", "Wa'lamu", 182),
            JuzList("11", "YaTazeeroon", 202),
            JuzList("12", "Wa Ma Min Daabbah", 222),
            JuzList("13", "Wa ma Ubarri-oo", 242),
            JuzList("14", "Rubama", 262),
            JuzList("15", "Subhanallazi", 282),
            JuzList("16", "Qaula Alam", 302),
            JuzList("17", "IqTaraba", 322),
            JuzList("18", "Qad Aflaha", 342),
            JuzList("19", "Wa Qaulallazina", 362),
            JuzList("20", "Amman Qalaq", 382),
            JuzList("21", "Utlu Maa Oohiya", 402),
            JuzList("22", "Wa Maii-Yaqnut", 422),
            JuzList("23", "WaMa-Liya", 442),
            JuzList("24", "Faman Azlamu", 462),
            JuzList("25", "Ilaihi Yuraddu", 482),
            JuzList("26", "Haa Meem", 502),
            JuzList("27", "Qaula Fa Ma khatbukum", 522),
            JuzList("28", "Qad Sami-Ullah", 542),
            JuzList("29", "Tabara-kallazi", 562),
            JuzList("30", "Amma", 582)
        )
        setUpSurahRV(view)
        setUpJuzRV(view,juzList)
        return view
    }


    private fun setUpJuzRV(view: View,jList: ArrayList<JuzList>){
        recycleJuzList = view.findViewById(R.id.recycleJuzList) as RecyclerView

            if (activity != null) {
                juzListAudioAdapter =
                    JuzListAudioAdapter(activity as Context, jList)
                val cLayoutManager =
                    CarouselLayoutManager(CarouselLayoutManager.VERTICAL,true)
                recycleJuzList.layoutManager = cLayoutManager
                recycleJuzList.adapter = juzListAudioAdapter
                recycleJuzList.setHasFixedSize(true)
                recycleJuzList.addOnScrollListener(CenterScrollListener())
            }
        }


    private fun setUpSurahRV(view: View) {
        recycleSurahList = view.findViewById(R.id.recycleSurahList) as RecyclerView
        val queue = Volley.newRequestQueue(activity as Context)
        val url = "http://api.alquran.cloud/v1/surah"

        if (ConnectionManager().checkConnection(activity as Context)) {

//creating a json object
            val jsonObjectRequest = object : JsonObjectRequest(
                Method.GET, url, null, Response.Listener {


                    try {

                        val resArray = it.getJSONArray("data")
                        for (i in 0 until resArray.length()) {
                            val resJsonObject = resArray.getJSONObject(i)
                            val sl = SurahList(
                                resJsonObject.getInt("number"),
                                resJsonObject.getString("englishName"),
                                resJsonObject.getInt("numberOfAyahs"),
                                resJsonObject.getString("revelationType"),
                                0
                            )
                            surahList.add(sl)
                        }

                        //attach dashboard fragment to adapter(bridge between data and view)
                        if (activity != null) {
                            surahListAudioAdapter =
                                SurahListAudioAdapter(activity as Context, surahList)
                            val cLayoutManager =
                                CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL,false)
                            recycleSurahList.layoutManager = cLayoutManager
                            recycleSurahList.adapter = surahListAudioAdapter
                            recycleSurahList.setHasFixedSize(true)
                            recycleSurahList.addOnScrollListener(CenterScrollListener())

                            }





                    } catch (e: JSONException) {
                        e.printStackTrace()
                        Toast.makeText(
                            activity as Context,
                            "Some UnExpected Error Occurred",
                            Toast.LENGTH_LONG
                        )
                            .show()
                    }


                },
                Response.ErrorListener {
                    //println("Error is $it")
                    if (activity != null) {
                        Toast.makeText(
                            activity as Context,
                            "Volley Error Occurred",
                            Toast.LENGTH_LONG
                        )
                            .show()
                    }
                }) {

            }
            queue.add(jsonObjectRequest)
        } else {
            showAlert()

        }

    }

    private fun showAlert() {
        val dialog = AlertDialog.Builder(activity as Context)
        dialog.setTitle("Failure")
        dialog.setMessage("Internet Connection NOT Found")
        dialog.setPositiveButton("Open Settings") { _, _ ->
            val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
            startActivity(settingsIntent)
            activity?.finish()
        }
        dialog.setNegativeButton("Exit") { _, _ ->
            ActivityCompat.finishAffinity(activity as Activity)
        }
        dialog.create()
        dialog.show()
    }
}