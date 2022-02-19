package com.aneeq.myquran.adapter.audio

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
import com.aneeq.myquran.models.Reciters


class AudioDisplayAdapter(val context: Context, var adList: ArrayList<Reciters>) :
    RecyclerView.Adapter<AudioDisplayAdapter.ADViewHolder>() {
    lateinit var sharedPreferences: SharedPreferences

    class ADViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val llContent: LinearLayout = view.findViewById(R.id.llContent)
        val txthname: TextView = view.findViewById(R.id.txthname)
        val imgsrc: ImageView = view.findViewById(R.id.imgsrc)
        val txtbitrate: TextView = view.findViewById(R.id.txtbitrate)
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
        sharedPreferences =
            context.getSharedPreferences("Messenger Preferences", Context.MODE_PRIVATE)
        val sq = adList[position]
        holder.txthname.text = sq.name
        holder.txtbitrate.text = sq.bitRate
        holder.llContent.setOnClickListener {
            sharedPreferences.edit().putString("audio", sq.apiName).apply()
            sharedPreferences.edit().putString("imam", sq.name).apply()
            Toast.makeText(
                context,
                "The reciter name ${sq.name} has been saved",
                Toast.LENGTH_SHORT
            ).show()

            val intent = Intent(context, NavigActivity::class.java)
            context.startActivity(intent)
            (context as Activity).finishAffinity()

        }

    }
}