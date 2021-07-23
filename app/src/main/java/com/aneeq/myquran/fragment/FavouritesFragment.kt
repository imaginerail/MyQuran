package com.aneeq.myquran.fragment

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.aneeq.myquran.R
import com.aneeq.myquran.adapter.FavouritesAdapter
import com.aneeq.myquran.database.favouritesdatabase.FavouritesDatabase
import com.aneeq.myquran.database.favouritesdatabase.FavouritesEntity
import com.trendyol.bubblescrollbarlib.BubbleScrollBar

class FavouritesFragment : Fragment() {
    lateinit var txtempty: TextView
    lateinit var txturfav: TextView
    lateinit var noLayout: RelativeLayout
    lateinit var imgEmptyFavourites: ImageView
    lateinit var recycleFavList: RecyclerView
    lateinit var bubbleScroll: BubbleScrollBar
    lateinit var favouritesAdapter: FavouritesAdapter
    var favList = arrayListOf<FavouritesEntity>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_favourites, container, false)
        noLayout = view.findViewById(R.id.noLayout2)
        txturfav = view.findViewById(R.id.txturfav)
        imgEmptyFavourites = view.findViewById(R.id.imgEmptyFavourites)
        txtempty = view.findViewById(R.id.txtempty)
        recycleFavList = view.findViewById(R.id.recycleFavList)
        bubbleScroll = view.findViewById(R.id.bubbleScroll)
        setUpRecycler()
        return view
    }

    private fun setUpRecycler() {
        favList =
            RetrieveFavourites(activity as Context).execute().get() as ArrayList<FavouritesEntity>

        if (favList.isEmpty()) noLayout.visibility = View.VISIBLE
        else noLayout.visibility = View.GONE

        if (activity != null) {
            favouritesAdapter =
                FavouritesAdapter(
                    activity as Context,
                    favList

                )
            val mLayoutManager =
                LinearLayoutManager(activity as Context)
            recycleFavList.layoutManager = mLayoutManager
            recycleFavList.itemAnimator = DefaultItemAnimator()
            recycleFavList.adapter = favouritesAdapter
            recycleFavList.setHasFixedSize(true)
        }

    }
}

class RetrieveFavourites(val context: Context) : AsyncTask<Void, Void, List<FavouritesEntity>>() {
    override fun doInBackground(vararg p0: Void?): List<FavouritesEntity> {
        val db = Room.databaseBuilder(context, FavouritesDatabase::class.java, "res-db").build()
        return db.restaurantDao().getAllRestaurants()
    }

}