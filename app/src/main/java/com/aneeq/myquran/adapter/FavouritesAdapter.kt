package com.aneeq.myquran.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.aneeq.myquran.R
import com.aneeq.myquran.activity.ReciteJuzActivity
import com.aneeq.myquran.database.favouritesdatabase.FavouritesEntity
import com.aneeq.myquran.fragment.FavouritesFragment


class FavouritesAdapter(val context: Context, var fList: ArrayList<FavouritesEntity>) :
    RecyclerView.Adapter<FavouritesAdapter.SLViewHolder>() {


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
        return fList.size
    }

    override fun onBindViewHolder(holder: SLViewHolder, position: Int) {
        val fq = fList[position]
        holder.txtnumber.text = "${fq.number.subSequence(1, fq.number.length)}"
        holder.txtsurahname.text = fq.englishNamePara
        if (fq.revelationType != "") {
            holder.txrevanum.text = "(${fq.revelationType} Total Ayats: ${fq.numberOfAyahsPage})"
        } else {
            holder.txrevanum.text = ""
            holder.llContent.setBackgroundColor(Color.parseColor("#9C27B0"))
        }

        holder.llContent.setOnClickListener {
            val intent = Intent(context, ReciteJuzActivity::class.java)
            if (fq.revelationType != "") {
                intent.putExtra("isSurah", true)
            }
            intent.putExtra("page", fq.page)
            context.startActivity(intent)
        }

        holder.btnAddFav.setImageResource(R.drawable.ic_staryellow)

        holder.btnAddFav.setOnClickListener {

            val result = DBAsyncTask(context, fq, 3).execute().get()
            if (result) {
                Toast.makeText(
                    context,
                    "${fq.englishNamePara} Removed from Favourites",
                    Toast.LENGTH_SHORT
                )
                    .show()
                holder.btnAddFav.setImageResource(R.drawable.ic_fav)
                refresh()

            }
        }
    }

    private fun refresh() {
        (context as AppCompatActivity)
            .supportFragmentManager
            .beginTransaction()
            .replace(R.id.frame, FavouritesFragment())
            .addToBackStack(null)
            .commit()


    }
}