package com.aneeq.myquran.adapter.browse

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.aneeq.myquran.R
import com.aneeq.myquran.activity.ReciteJuzActivity
import com.aneeq.myquran.adapter.DBAsyncTask
import com.aneeq.myquran.adapter.GetAllFavAsyncTask
import com.aneeq.myquran.database.favouritesdatabase.FavouritesEntity
import com.aneeq.myquran.models.JuzList
import java.util.*
import kotlin.collections.ArrayList

class JuzListAdapter(val context: Context, var jlList: ArrayList<JuzList>) :
    RecyclerView.Adapter<JuzListAdapter.JLViewHolder>(),Filterable {
    var searchFilterList = ArrayList<JuzList>()

    init {
        searchFilterList = jlList
    }

    class JLViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val llContent: LinearLayout = view.findViewById(R.id.llContent)
        val txtnumber: TextView = view.findViewById(R.id.txtnumber)
        val txtjuzname: TextView = view.findViewById(R.id.txtjuzname)
        val btnAddFav: ImageView = view.findViewById(R.id.btnAddFav)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JLViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.jlist_recycler_single_row, parent, false)
        return JLViewHolder(view)
    }

    override fun getItemCount(): Int {
        return searchFilterList.size
    }

    override fun onBindViewHolder(holder: JLViewHolder, position: Int) {
        val jq = jlList[position]
        holder.txtnumber.text = "${position+1}."
        holder.txtjuzname.text = jq.name
        holder.btnAddFav.setImageResource(R.drawable.ic_fav)
        holder.llContent.setOnClickListener(View.OnClickListener {
            val intent = Intent(context, ReciteJuzActivity::class.java)
            intent.putExtra("page",jq.page)
//            intent.putExtra("jname",jq.name)
            context.startActivity(intent)


        })

        val listOfFavourites = GetAllFavAsyncTask(context).execute().get()
        if (listOfFavourites.isNotEmpty() && listOfFavourites.contains(jq.number.toString())) {
            holder.btnAddFav.setImageResource(R.drawable.ic_staryellow)
        } else {
            holder.btnAddFav.setImageResource(R.drawable.ic_fav)
        }

        holder.btnAddFav.setOnClickListener {
            val restaurantEntity =
                FavouritesEntity(
                    jq.number,
                    jq.name,
                    jq.page,
                    "",
                    jq.page
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
                val charString = p0.toString()
                if (charString.isEmpty()) {
                    searchFilterList = jlList
                } else {
                    val filteredList=ArrayList<JuzList>()

                    for (row in jlList) {
                        if (row.name.lowercase(Locale.getDefault())
                                .contains(charString) || row.number.toString()
                                .contains(charString)
                        ) {
                            filteredList.add(row)
                        }
                    }

                    searchFilterList = filteredList
                }
                val filterResults = FilterResults()
                filterResults.values = searchFilterList
                return filterResults
            }
            @Suppress("UNCHECKED_CAST")
            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                searchFilterList = p1?.values as ArrayList<JuzList>
                notifyDataSetChanged()
            }

        }
    }
}