package com.aneeq.myquran.adapter.audio

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aneeq.myquran.R
import com.aneeq.myquran.activity.ReciteAudioActivity
import com.aneeq.myquran.models.SurahList

class SurahListAudioAdapter(val context: Context, var slList: ArrayList<SurahList>) :
    RecyclerView.Adapter<SurahListAudioAdapter.SLViewHolder>() {
//    var searchFilterList = ArrayList<SurahList>()
//
//    init {
//        searchFilterList = slList
//    }

    

    class SLViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val llContent: LinearLayout = view.findViewById(R.id.llContent)
        val txtnumber: TextView = view.findViewById(R.id.txtnumber)
        val txrevanum: TextView = view.findViewById(R.id.txrevanum)
        val txtsurahname: TextView = view.findViewById(R.id.txtsurahname)
        val btnAddFav: ImageView = view.findViewById(R.id.btnAddFav)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SLViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.slist_recycler_single_row, parent, false)
        return SLViewHolder(view)
    }

    override fun getItemCount(): Int {
        return slList.size
    }

    override fun onBindViewHolder(holder: SLViewHolder, position: Int) {
        val sq = slList[position]
        holder.txtnumber.text = "${sq.number}"
        holder.txtsurahname.text = sq.englishName
        holder.txrevanum.text = "(${sq.revelationType} Total Ayats: ${sq.numberOfAyahs})"
        holder.btnAddFav.visibility = View.GONE
        holder.llContent.setOnClickListener(View.OnClickListener {
            val intent = Intent(context, ReciteAudioActivity::class.java)
            intent.putExtra("num", sq.number)
            context.startActivity(intent)
        })


    }

//    override fun getFilter(): Filter {
//        return object : Filter() {
//            override fun performFiltering(p0: CharSequence?): FilterResults {
//                val charString = p0.toString().toLowerCase(Locale.ROOT)
//                if (charString.isEmpty()) {
//                    searchFilterList = slList
//                } else {
//                    val filteredList = ArrayList<SurahList>()
//
//                    for (row in slList) {
//                        if (row.englishName.toLowerCase(Locale.ROOT)
//                                .contains(charString)
//                        ) {
//                            filteredList.add(row)
//
//                        }
//                    }
//
//                    searchFilterList = filteredList
//
//                    for(i in 1..filteredList.size){
//                        Log.d("srch", filteredList[i].toString())
//                    }
//                }
//                val filterResults = FilterResults()
//                filterResults.values = searchFilterList
//                return filterResults
//            }
//
//
//            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
//                searchFilterList = if(p1?.values==null)
//                    ArrayList()
//                else
//                    p1.values as ArrayList<SurahList>
//                notifyDataSetChanged()
//            }
//
//        }
//    }


}

