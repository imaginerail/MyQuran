package com.aneeq.myquran.adapter

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.aneeq.myquran.R
import com.aneeq.myquran.activity.TypeNEditionActivity
import com.aneeq.myquran.models.Languages
import com.aneeq.myquran.models.SurahList
import java.util.*
import kotlin.collections.ArrayList

class LanguageAdapter(val context: Context, var lnList: ArrayList<Languages>) :
    RecyclerView.Adapter<LanguageAdapter.SLViewHolder>() {
    lateinit var sharedPreferences: SharedPreferences

    class SLViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val llContent: LinearLayout = view.findViewById(R.id.llContent)
        val txtLanguage: TextView = view.findViewById(R.id.txtLanguage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SLViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.lang_recycler_single_row, parent, false)
        return SLViewHolder(view)
    }

    override fun getItemCount(): Int {
        return lnList.size
    }

    override fun onBindViewHolder(holder: SLViewHolder, position: Int) {
        sharedPreferences =
            context.getSharedPreferences("Messenger Preferences", Context.MODE_PRIVATE)
        val sq = lnList[position]
        val loc=Locale(sq.language)
        holder.txtLanguage.text = loc.displayLanguage
        holder.txtLanguage.text=holder.txtLanguage.text.substring(0,1).toUpperCase()+holder.txtLanguage.text.substring(1)

        holder.llContent.setOnClickListener(View.OnClickListener {



            val intent = Intent(context, TypeNEditionActivity::class.java)
            intent.putExtra("selectLang",sq.language)
              context.startActivity(intent)


        })
    }
}