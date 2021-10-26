package com.aneeq.myquran.adapter.browse

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aneeq.myquran.R
import com.aneeq.myquran.models.OriginalAyahs

class ReciteSurahAdapter(val context: Context, var oList: List<OriginalAyahs>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class ORViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val txtquran: TextView = view.findViewById(R.id.txtquran)

        fun setOriginal(org: OriginalAyahs) {
            txtquran.text = "${org.numberInSurah} ${org.text} O"
        }
    }

    class DPViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val txttranslate: TextView = view.findViewById(R.id.txt2)

        fun setDuplicate(dup: OriginalAyahs) {
            txttranslate.text = dup.text
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        //0 original 1 duplicate
        return if (viewType == 0) {
            ORViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.recite_recycler_single_row, parent, false)
            )

        } else {
            DPViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.translate_recycler_single_row, parent, false)
            )

        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val original = oList[position]
        if (getItemViewType(position) == 0) {
            (holder as ORViewHolder).setOriginal(original)

        } else {
            (holder as DPViewHolder).setDuplicate(original)


        }


    }


    override fun getItemCount(): Int {
        return oList.size
    }

    override fun getItemViewType(position: Int): Int {
        return oList[position].type
    }
}

//        val sq = osaList[position]
//        if (position == 0) {
//            sq.text = "${sq.text.substring(0, 37)} \n 1  ${sq.text.substring(38)} "
//            holder.txtquran.text = sq.text
//        }
//        holder.txtquran.text = "${sq.numberInSurah} ${sq.text} O"


