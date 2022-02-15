package com.aneeq.myquran.adapter.settings

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.aneeq.myquran.R
import com.aneeq.myquran.activity.NavigActivity
import com.aneeq.myquran.models.Editions

class TNEAdapter(
    val context: Context,
    var lnList: ArrayList<Editions>,
    var type: String
) :
    RecyclerView.Adapter<TNEAdapter.SLViewHolder>() {
    lateinit var sharedPreferences: SharedPreferences
    var llList = ArrayList<Editions>()


    class SLViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val llContent: RelativeLayout = view.findViewById(R.id.llContent)
        val txtedname: TextView = view.findViewById(R.id.txtedname)
        val txedengname: TextView = view.findViewById(R.id.txedengname)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SLViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.tne_recycler_single_row, parent, false)
        return SLViewHolder(view)
    }

    override fun getItemCount(): Int {
        for (i in 0 until lnList.size) {
            if (lnList[i].type == type) {
                llList.add(lnList[i])
            }
        }
        val set = LinkedHashSet<Editions>()
        set.addAll(llList)
        llList.clear()
        llList.addAll(set)
        Log.d("tne", "${llList.size}")
        return llList.size

    }

    override fun onBindViewHolder(holder: SLViewHolder, position: Int) {
        sharedPreferences =
            context.getSharedPreferences("Messenger Preferences", Context.MODE_PRIVATE)
        val sq = llList[position]

        holder.txtedname.text = sq.name
        holder.txedengname.text = sq.englishName

        holder.llContent.setOnClickListener {
            sharedPreferences.edit().putBoolean("isLoggedIn", true).apply()
            sharedPreferences.edit().putString("identifier", sq.identifier).apply()
            sharedPreferences.edit().putString("language", sq.language).apply()
            sharedPreferences.edit().putString("name", sq.name).apply()
            sharedPreferences.edit().putString("englishName", sq.englishName).apply()
            sharedPreferences.edit().putString("format", sq.format).apply()
            sharedPreferences.edit().putString("type", sq.type).apply()

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


