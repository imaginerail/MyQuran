package com.aneeq.myquran.adapter

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.aneeq.myquran.R
import com.aneeq.myquran.activity.ReciteJuzActivity
import com.aneeq.myquran.database.favouritesdatabase.FavouritesDatabase
import com.aneeq.myquran.database.favouritesdatabase.FavouritesEntity
import com.aneeq.myquran.models.SurahList
import java.util.*
import kotlin.collections.ArrayList

class SurahListAdapter(val context: Context, var slList: ArrayList<SurahList>) :
    RecyclerView.Adapter<SurahListAdapter.SLViewHolder>(), Filterable {
    var searchFilterList = ArrayList<SurahList>()

    init {
        searchFilterList = slList
    }

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
        return searchFilterList.size
    }

    override fun onBindViewHolder(holder: SLViewHolder, position: Int) {
        val sq = slList[position]
        holder.txtnumber.text = "${sq.number}"
        holder.txtsurahname.text = sq.englishName
        holder.txrevanum.text = "(${sq.revelationType} Total Ayats: ${sq.numberOfAyahs})"
        holder.btnAddFav.setImageResource(R.drawable.ic_fav)
        holder.llContent.setOnClickListener(View.OnClickListener {
            val intent = Intent(context, ReciteJuzActivity::class.java)
            intent.putExtra("page", sq.page)
            intent.putExtra("isSurah", true)
            intent.putExtra("pos", sq.number)
            context.startActivity(intent)
        })

        val listOfFavourites = GetAllFavAsyncTask(context).execute().get()
        if (listOfFavourites.isNotEmpty() && listOfFavourites.contains(sq.number.toString())) {
            holder.btnAddFav.setImageResource(R.drawable.ic_staryellow)
        } else {
            holder.btnAddFav.setImageResource(R.drawable.ic_fav)
        }

        holder.btnAddFav.setOnClickListener {
            val restaurantEntity =
                FavouritesEntity(
                    sq.number.toString(),
                    sq.englishName,
                    sq.numberOfAyahs,
                    sq.revelationType,
                    sq.page
                )
            if (!DBAsyncTask(context, restaurantEntity, 1).execute().get()) {
                val result = DBAsyncTask(context, restaurantEntity, 2).execute().get()
                //val result = async.get()
                if (result) {
                    Toast.makeText(context, "Surah Added To Favourites", Toast.LENGTH_LONG)
                        .show()
                    holder.btnAddFav.setImageResource(R.drawable.ic_staryellow)
                }
            } else {
                val result = DBAsyncTask(context, restaurantEntity, 3).execute().get()
                if (result) {
                    Toast.makeText(context, "Surah Removed from Favourites", Toast.LENGTH_LONG)
                        .show()
                    holder.btnAddFav.setImageResource(R.drawable.ic_fav)

                }
            }
        }


    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(p0: CharSequence?): FilterResults {
                val charString = p0.toString().lowercase(Locale.getDefault())
                if (charString.isEmpty()) {
                    searchFilterList = slList
                } else {
                    val filteredList = ArrayList<SurahList>()

                    for (row in slList) {
                        if (row.englishName.lowercase(Locale.getDefault())
                                .contains(charString)
                        ) {
                            filteredList.add(row)

                        }
                    }

                    searchFilterList = filteredList

                    for(i in 1..filteredList.size){
                        Log.d("srch", filteredList[i].toString())
                    }
                }
                val filterResults = FilterResults()
                filterResults.values = searchFilterList
                return filterResults
            }


            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                searchFilterList = if(p1?.values==null)
                    ArrayList()
                else
                    p1.values as ArrayList<SurahList>
                notifyDataSetChanged()
            }

        }
    }
}


//Favourites
class DBAsyncTask(
    val context: Context,
    private val restaurantEntity: FavouritesEntity,
    private val mode: Int
) :
    AsyncTask<Void, Void, Boolean>() {

    private val db = Room.databaseBuilder(context, FavouritesDatabase::class.java, "res-db").build()

    override fun doInBackground(vararg p0: Void?): Boolean {
        when (mode) {
            1 -> {
                val res: FavouritesEntity? =
                    db.restaurantDao().getRestaurantById(restaurantEntity.number.toString())
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

class GetAllFavAsyncTask(context: Context) : AsyncTask<Void, Void, List<String>>() {

    private val db = Room.databaseBuilder(context, FavouritesDatabase::class.java, "res-db").build()
    override fun doInBackground(vararg params: Void?): List<String> {

        val list = db.restaurantDao().getAllRestaurants()
        val listOfIds = arrayListOf<String>()
        for (i in list) {
            listOfIds.add(i.number.toString())
        }
        return listOfIds
    }
}

