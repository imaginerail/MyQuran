package com.aneeq.myquran.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aneeq.myquran.R
import com.aneeq.myquran.models.SearchAyahResults

import kotlin.collections.ArrayList

class SearchAyahResultsAdapter(val context: Context, var sarList: ArrayList<SearchAyahResults>) :
    RecyclerView.Adapter<SearchAyahResultsAdapter.SLViewHolder>() {


    class SLViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val txt1: TextView = view.findViewById(R.id.txt1)
        val txt2: TextView = view.findViewById(R.id.txt2)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SLViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.search_ayah_single_row, parent, false)
        return SLViewHolder(view)
    }

    override fun getItemCount(): Int {
        return sarList.size
    }

    override fun onBindViewHolder(holder: SLViewHolder, position: Int) {

        val sar = sarList[position]
        holder.txt1.text = "Edition:${sar.ed}"
        holder.txt2.text = "${sar.num} ${sar.text}."
    }
    }