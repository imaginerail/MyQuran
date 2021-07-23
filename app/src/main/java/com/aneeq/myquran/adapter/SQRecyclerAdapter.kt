package com.aneeq.myquran.adapter

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aneeq.myquran.R
import com.aneeq.myquran.activity.StartReadingActivity
import com.aneeq.myquran.models.SelectQuran

class SQRecyclerAdapter (val context: Context, var sqList: ArrayList<SelectQuran>) :
    RecyclerView.Adapter<SQRecyclerAdapter.SQViewHolder>() {
    lateinit var sharedPreferences: SharedPreferences


    class SQViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtsqname: TextView = view.findViewById(R.id.txtsqname)
        val imgsq: ImageView = view.findViewById(R.id.imgsq)
        val txtsqenglishname: TextView = view.findViewById(R.id.txtsqenglishname)
        val rlsq:RelativeLayout=view.findViewById(R.id.rlsq)



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SQViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.sq_recycler_single_row, parent, false)
        return SQViewHolder(view)
    }

    override fun getItemCount(): Int {
        return sqList.size
    }

    override fun onBindViewHolder(holder: SQViewHolder, position: Int) {
        sharedPreferences=context.getSharedPreferences("Messenger Preferences", Context.MODE_PRIVATE)
        val sq = sqList[position]
        holder.txtsqname.text = sq.name
        holder.txtsqenglishname.text = sq.englishName


        holder.rlsq.setOnClickListener(View.OnClickListener {
            sharedPreferences.edit().putBoolean("isLoggedIn", true).apply()
            sharedPreferences.edit().putString("identifier", sq.identifier).apply()
            sharedPreferences.edit().putString("name", sq.name).apply()
            holder.rlsq.setBackgroundColor(Color.RED)
            val intent = Intent(context, StartReadingActivity::class.java)
            context.startActivity(intent)

        })
    }
}