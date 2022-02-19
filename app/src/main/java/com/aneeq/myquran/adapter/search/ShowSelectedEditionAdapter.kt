package com.aneeq.myquran.adapter.search

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.aneeq.myquran.R
import com.aneeq.myquran.adapter.DBAsyncTask3
import com.aneeq.myquran.database.seleddatabase.SelEntity

class ShowSelectedEditionAdapter(val context: Context, var sList: ArrayList<SelEntity>) :
    RecyclerView.Adapter<ShowSelectedEditionAdapter.SLViewHolder>() {


    class SLViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtid: TextView = view.findViewById(R.id.txtid)
        val llContent: LinearLayout = view.findViewById(R.id.llContent)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SLViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.editions_srchayah_single_row, parent, false)
        return SLViewHolder(view)
    }

    override fun getItemCount(): Int {
        return sList.size
    }

    override fun onBindViewHolder(holder: SLViewHolder, position: Int) {
        val sq = sList[position]
        holder.txtid.text = sq.id

        holder.txtid.setOnClickListener {

            val result = DBAsyncTask3(context, sq, 3).execute().get()
            if (result) {
                Toast.makeText(context, "${sq.name} is Removed", Toast.LENGTH_SHORT)
                    .show()
                holder.llContent.visibility = View.GONE


            }
        }
    }
}
