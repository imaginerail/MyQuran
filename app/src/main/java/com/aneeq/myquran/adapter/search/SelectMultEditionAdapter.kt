package com.aneeq.myquran.adapter

import android.content.Context
import android.graphics.Color
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.aneeq.myquran.R
import com.aneeq.myquran.database.seleddatabase.SelDatabase
import com.aneeq.myquran.database.seleddatabase.SelEntity
import com.aneeq.myquran.models.Editions

class SelectMultEditionAdapter(val context: Context, var lnList: ArrayList<Editions>) :
    RecyclerView.Adapter<SelectMultEditionAdapter.SLViewHolder>() {

    class SLViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val llContent: RelativeLayout = view.findViewById(R.id.llContent)
        val txtedname: TextView = view.findViewById(R.id.txtedname)
        val txedengname: TextView = view.findViewById(R.id.txedengname)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SLViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.selecteded_single_row, parent, false)
        return SLViewHolder(view)
    }

    override fun getItemCount(): Int {

        return lnList.size

    }

    override fun onBindViewHolder(holder: SLViewHolder, position: Int) {
        val sq = lnList[position]
        holder.txtedname.text = sq.name
        holder.txedengname.text = sq.englishName
        holder.llContent.setBackgroundColor(Color.parseColor("#F8ABA5"))

        val listOfFavourites = GetAllSelEdAsyncTask(context).execute().get()
        if (listOfFavourites.isNotEmpty() && listOfFavourites.contains(sq.identifier)) {
            holder.llContent.setBackgroundColor(Color.parseColor("#F45FFF"))
        } else {
            holder.llContent.setBackgroundColor(Color.parseColor("#F8ABA5"))
        }

        holder.llContent.setOnClickListener {
            val restaurantEntity =
                SelEntity(
                    sq.identifier,
                    sq.name,
                    sq.englishName
                )
            if (!DBAsyncTask3(context, restaurantEntity, 1).execute().get()) {
                val result = DBAsyncTask3(context, restaurantEntity, 2).execute().get()
                //val result = async.get()
                if (result) {
                    Toast.makeText(context, "Edition selected", Toast.LENGTH_SHORT)
                        .show()
                    holder.llContent.setBackgroundColor(Color.parseColor("#F45FFF"))
                }
            } else {
                val result = DBAsyncTask3(context, restaurantEntity, 3).execute().get()
                if (result) {
                    Toast.makeText(context, "Edition Removed", Toast.LENGTH_SHORT)
                        .show()
                    holder.llContent.setBackgroundColor(Color.parseColor("#F8ABA5"))

                }
            }
        }








    }
}


class DBAsyncTask3(
    val context: Context,
    private val restaurantEntity: SelEntity,
    private val mode: Int
) :
    AsyncTask<Void, Void, Boolean>() {

    private val db = Room.databaseBuilder(context, SelDatabase::class.java, "sel-db").build()

    override fun doInBackground(vararg p0: Void?): Boolean {
        when (mode) {
            1 -> {
                val res: SelEntity? = db.restaurantDao().getSelEdnById(restaurantEntity.id)
                db.close()
                return res != null
            }
            2 -> {
                db.restaurantDao().insertRes(restaurantEntity)
                db.close()
                return true
            }
            3 -> {
                db.restaurantDao().deleteRes(restaurantEntity)
                db.close()
                return true
            }
        }

        return false
    }


}

class GetAllSelEdAsyncTask(context: Context) : AsyncTask<Void, Void, List<String>>() {

    private val db = Room.databaseBuilder(context, SelDatabase::class.java, "sel-db").build()
    override fun doInBackground(vararg params: Void?): List<String> {

        val list = db.restaurantDao().getAllSelEdns()
        val listOfIds = arrayListOf<String>()
        for (i in list) {
            listOfIds.add(i.id)
        }
        return listOfIds
    }
}