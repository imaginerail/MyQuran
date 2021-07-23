package com.aneeq.myquran.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.aneeq.myquran.R
import com.aneeq.myquran.activity.NavigActivity
import com.aneeq.myquran.models.Editions


class AudioDisplayAdapter(val context: Context, var adList: ArrayList<Editions>) :
    RecyclerView.Adapter<AudioDisplayAdapter.ADViewHolder>() {
    lateinit var sharedPreferences: SharedPreferences

    class ADViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val llContent: LinearLayout = view.findViewById(R.id.llContent)
        val txtenglishname: TextView = view.findViewById(R.id.txtenglishname)
        val txthname: TextView = view.findViewById(R.id.txthname)
        val btnSelect: ImageView = view.findViewById(R.id.btnSelect)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ADViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.audio_display_recyclerview_single_row, parent, false)
        return ADViewHolder(view)
    }

    override fun getItemCount(): Int {
        return adList.size
    }

    override fun onBindViewHolder(holder: ADViewHolder, position: Int) {
        val sq = adList[position]
        holder.txtenglishname.text = sq.englishName
        holder.txthname.text = sq.name
        holder.btnSelect.setOnClickListener {
            sharedPreferences.edit().putString("audio", sq.identifier).apply()

            Toast.makeText(
                context,
                "Your Edition ${sq.identifier} has been saved",
                Toast.LENGTH_SHORT
            ).show()

            val intent = Intent(context, NavigActivity::class.java)
            context.startActivity(intent)
            (context as Activity).finishAffinity()
        }

    }
}