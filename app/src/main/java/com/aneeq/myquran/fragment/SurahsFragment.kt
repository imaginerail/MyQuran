package com.aneeq.myquran.fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.aneeq.myquran.R
import com.aneeq.myquran.adapter.SurahListAdapter
import com.aneeq.myquran.models.SurahList
import com.aneeq.myquran.util.ConnectionManager
import com.trendyol.bubblescrollbarlib.BubbleScrollBar
import com.trendyol.bubblescrollbarlib.BubbleTextProvider
import org.json.JSONException


class SurahsFragment : Fragment() {
    lateinit var searchEditText: EditText
    lateinit var search: SearchView
    lateinit var rl7: RelativeLayout
    lateinit var recycleSurahList: RecyclerView
    lateinit var bubbleScroll: BubbleScrollBar
    private lateinit var surahListAdapter: SurahListAdapter
    lateinit var progressLayout: RelativeLayout
    lateinit var progressBar: ProgressBar
    val surahList = arrayListOf<SurahList>()
    val pgNm = arrayOf(
        1, 2, 50, 77, 106, 128, 151, 177, 187,
        208, 221, 235, 249, 255, 262, 267, 282, 293, 305,
        312, 322, 332, 342, 350, 359, 367, 377, 385, 396,
        404, 411, 415, 418, 428, 434, 440, 446, 453, 458, 467,
        477, 483, 489, 496, 499, 502, 507, 511, 515, 518, 520,
        523, 526, 528, 531, 534, 537, 542, 545, 549, 551, 553,
        554, 556, 558, 560, 562, 564, 566, 568, 570, 572, 574, 575,
        577, 578, 580, 582, 583, 585, 586, 587, 587, 589, 590,
        591, 591, 592, 593, 594, 595, 595, 596, 596, 597, 597,
        598, 598, 599, 599, 600, 600, 601, 601, 601, 602, 602,
        602, 603, 603, 603, 604, 604, 604
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_surahs, container, false)
        setHasOptionsMenu(true)
        rl7 = view.findViewById(R.id.rl7)
        search = view.findViewById(R.id.search1)
        progressLayout = view.findViewById(R.id.progressLayout)
        progressBar = view.findViewById(R.id.progressBar)
        progressLayout.visibility = View.VISIBLE
        bubbleScroll = view.findViewById(R.id.bubbleScroll)
        setUpRecycler(view)
        ///////////////////////////////////
        search.queryHint = "Type the name/number of Surah here"
        searchEditText = view.findViewById(androidx.appcompat.R.id.search_src_text)
        searchEditText.setTextColor(ContextCompat.getColor(context!!, R.color.colorAccent))
        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                surahListAdapter.filter.filter(newText)
                return false
            }

        })
        //////////////////////////////////
        return view
    }

    private fun setUpRecycler(view: View) {
        recycleSurahList = view.findViewById(R.id.recycleSurahList) as RecyclerView
        val queue = Volley.newRequestQueue(activity as Context)
        val url = "http://api.alquran.cloud/v1/surah"

        if (ConnectionManager().checkConnection(activity as Context)) {

//creating a json object
            val jsonObjectRequest = object : JsonObjectRequest(
                Method.GET, url, null, Response.Listener {

                    progressLayout.visibility = View.GONE

                    try {

                        val resArray = it.getJSONArray("data")
                        for (i in 0 until resArray.length()) {
                            val resJsonObject = resArray.getJSONObject(i)
                            val sl = SurahList(
                                resJsonObject.getInt("number"),
                                resJsonObject.getString("englishName"),
                                resJsonObject.getInt("numberOfAyahs"),
                                resJsonObject.getString("revelationType"),
                                pgNm[i]
                            )
                            surahList.add(sl)
                        }

                        //attach dashboard fragment to adapter(bridge between data and view)
                        if (activity != null) {
                            surahListAdapter =
                                SurahListAdapter(activity as Context, surahList)
                            val mLayoutManager: LinearLayoutManager =
                                LinearLayoutManager(activity)
                            recycleSurahList.layoutManager = mLayoutManager
                            recycleSurahList.itemAnimator = DefaultItemAnimator()
                            recycleSurahList.adapter = surahListAdapter
                            recycleSurahList.setHasFixedSize(true)
                            bubbleScroll.attachToRecyclerView(recycleSurahList)
                            bubbleScroll.bubbleTextProvider = BubbleTextProvider { position ->
                                surahList[position].number.toString()
                            }


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
}
