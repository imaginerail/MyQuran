package com.aneeq.myquran.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aneeq.myquran.R
import com.aneeq.myquran.models.OriginalJuz

class ReciteJuzAdapter(
    val context: Context,
    var ojList: List<OriginalJuz>,
    var rv: RecyclerView,
    var isSurah: Boolean
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class ORViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val txtquran: TextView = view.findViewById(R.id.txtquran)
        val ll1: LinearLayout = view.findViewById(R.id.ll1)
        fun setOriginal(org: OriginalJuz) {
            txtquran.text = "${org.surahNum}:${org.ayahNum} ${org.text} \u06DD"
            if (org.text.contains("بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ")) { //D18973
                ll1.setBackgroundColor(Color.parseColor("#F43E07"))
            }
        }
    }

    class DPViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val txttranslate: TextView = view.findViewById(R.id.txt2)

        fun setDuplicate(dup: OriginalJuz) {
            txttranslate.text = dup.text
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        //0 original 1 duplicate
        return if (viewType == 0) {
            ORViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.juz_arabic_single_row, parent, false)
            )

        } else {
            DPViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.juz_translate_single_row, parent, false)
            )

        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val original = ojList[position]



        if (getItemViewType(position) == 0) {
            (holder as ORViewHolder).setOriginal(original)
//            if(original.text.contains("\uFEFFبِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ") && isSurah){
//                rv.scrollToPosition(position)
//            }
        } else {
            (holder as DPViewHolder).setDuplicate(original)


        }


    }


    override fun getItemCount(): Int {
        return ojList.size
    }

    override fun getItemViewType(position: Int): Int {
        return ojList[position].type
    }

}