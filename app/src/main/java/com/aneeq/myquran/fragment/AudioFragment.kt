package com.aneeq.myquran.fragment

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.aneeq.myquran.R
import com.aneeq.myquran.adapter.audio.SurahListAudioAdapter
import com.aneeq.myquran.models.SurahList
import com.aneeq.myquran.util.ConnectionManager
import com.mig35.carousellayoutmanager.CenterScrollListener
import org.json.JSONException


class AudioFragment : Fragment() {
    lateinit var txtSelectSurah: TextView
    lateinit var recycleSurahList: RecyclerView
    lateinit var txtSelectJuz: TextView
    lateinit var recycleJuzList: RecyclerView
    private lateinit var surahListAudioAdapter: SurahListAudioAdapter
    val surahList = arrayListOf<SurahList>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_audio, container, false)
        txtSelectSurah = view.findViewById(R.id.txtSelectSurah)
        recycleSurahList = view.findViewById(R.id.recycleaudioSurahList)


        setUpSurahRV()

        return view
    }


    private fun setUpSurahRV() {

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
                                LinearLayoutManager(activity as Context)
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
        dialog.setNegativeButton("Retry") { _, _ ->
            dialog.show().cancel()
        }
        dialog.create()
        dialog.show()
    }

}
