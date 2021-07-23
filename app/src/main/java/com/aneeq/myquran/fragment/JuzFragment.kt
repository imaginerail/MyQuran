package com.aneeq.myquran.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aneeq.myquran.R
import com.aneeq.myquran.adapter.JuzListAdapter
import com.aneeq.myquran.adapter.SurahListAdapter
import com.aneeq.myquran.models.JuzList
import com.aneeq.myquran.models.SurahList

class JuzFragment : Fragment() {
    lateinit var searchEditText: EditText
    lateinit var search: SearchView
    lateinit var rl7: RelativeLayout
    lateinit var recycleJUZList: RecyclerView
    private lateinit var juzListAdapter: JuzListAdapter
    lateinit var progressLayout: RelativeLayout
    lateinit var progressBar: ProgressBar


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_juz, container, false)
        setHasOptionsMenu(true)
        rl7 = view.findViewById(R.id.rl7)
        search = view.findViewById(R.id.search1)
        progressLayout = view.findViewById(R.id.progressLayout)
        progressBar = view.findViewById(R.id.progressBar)
        progressLayout.visibility = View.VISIBLE
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

        setUpRecycler(view, juzList)
///////////////////////////////////
        search.queryHint = "Type the name/number of Juz here"
        searchEditText = view.findViewById(androidx.appcompat.R.id.search_src_text)
        searchEditText.setTextColor(ContextCompat.getColor(context!!, R.color.colorAccent))
        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                juzListAdapter.filter.filter(newText)
                return false
            }

        })
        //////////////////////////////////
        return view
    }

    private fun setUpRecycler(view: View, jList: ArrayList<JuzList>) {
        recycleJUZList = view.findViewById(R.id.recycleJUZList) as RecyclerView
        progressLayout.visibility = View.GONE

        if (activity != null) {
            juzListAdapter =
                JuzListAdapter(activity as Context, jList)
            val mLayoutManager: LinearLayoutManager =
                LinearLayoutManager(activity)
            recycleJUZList.layoutManager = mLayoutManager
            recycleJUZList.itemAnimator = DefaultItemAnimator()
            recycleJUZList.adapter = juzListAdapter
            recycleJUZList.setHasFixedSize(true)
        }
    }


}