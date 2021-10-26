package com.aneeq.myquran.adapter.search

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.aneeq.myquran.R
import com.aneeq.myquran.models.SearchTextResults
import com.xeoh.android.texthighlighter.TextHighlighter

class SearchTextResultsAdapter(
    val context: Context,
    var sarList: ArrayList<SearchTextResults>,
    var par1: String
) :
    RecyclerView.Adapter<SearchTextResultsAdapter.SLViewHolder>() {


    class SLViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val txtayah: TextView = view.findViewById(R.id.txtayah)
        val txtdet: TextView = view.findViewById(R.id.txtdet)

        val cd1: CardView = view.findViewById(R.id.cd1)
        val txtet: TextView = view.findViewById(R.id.txtet)
        val txtedname: TextView = view.findViewById(R.id.txtedname)
        val txtedengname: TextView = view.findViewById(R.id.txtedengname)

        val txtst: TextView = view.findViewById(R.id.txtst)
        val txtstname: TextView = view.findViewById(R.id.txtstname)
        val txtsttransname: TextView = view.findViewById(R.id.txtsttransname)
        val txtstrev: TextView = view.findViewById(R.id.txtstrev)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SLViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.search_text_results_recycler_single_row, parent, false)
        return SLViewHolder(view)
    }

    override fun getItemCount(): Int {
        return sarList.size
    }

    override fun onBindViewHolder(holder: SLViewHolder, position: Int) {

        val sar = sarList[position]
        holder.txtayah.text = "${sar.snumber}:${sar.numberInSurah} ${sar.text}"
        holder.cd1.visibility = View.GONE
        holder.txtdet.text = "View Details"

        holder.txtdet.setOnClickListener {
            if (holder.cd1.visibility == View.GONE) {
                holder.cd1.visibility = View.VISIBLE
                holder.txtdet.text = "Hide Details"
            } else {
                holder.cd1.visibility = View.GONE
                holder.txtdet.text = "View Details"
            }
        }

        holder.txtedname.text = sar.edname
        holder.txtedengname.text = sar.edEngname


        holder.txtstname.text = "${sar.sname} (${sar.senglishName})"
        holder.txtsttransname.text = sar.senglishNameTranslation
        holder.txtstrev.text = "[${sar.srevelationType}]"

        TextHighlighter()
            .setBackgroundColor(Color.parseColor("#FFFF00"))
            .setForegroundColor(Color.GREEN)
            .addTarget(holder.txtayah)
            .highlight(par1, TextHighlighter.BASE_MATCHER)
    }


}
