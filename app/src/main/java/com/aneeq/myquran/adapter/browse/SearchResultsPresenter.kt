package com.aneeq.myquran.adapter.browse

import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.recyclerview.widget.RecyclerView
import com.aneeq.myquran.adapter.SurahListAdapter
import com.aneeq.myquran.models.SurahList
import com.otaliastudios.autocomplete.RecyclerViewPresenter
import java.util.*
import kotlin.collections.ArrayList


class SearchResultsPresenter(context: Context, slList: ArrayList<SurahList>) :
    RecyclerViewPresenter<SurahList?>(context) {
    var adapter: SurahListAdapter? = null
    private var srlList = slList
    override fun getPopupDimensions(): PopupDimensions {
        val dims = PopupDimensions()
        dims.width = 600
        dims.height = ViewGroup.LayoutParams.WRAP_CONTENT
        return dims
    }

    override fun instantiateAdapter(): RecyclerView.Adapter<*> {

        adapter = SurahListAdapter(context, srlList)
        return adapter!!
    }

    override fun onQuery(@Nullable query: CharSequence?) {
        var query = query
        val all = ArrayList<SurahList>()
        if (TextUtils.isEmpty(query)) {
            SurahListAdapter(context, srlList)
        } else {
            query = query.toString().lowercase(Locale.getDefault())
            val list: MutableList<SurahList> = ArrayList()
            for (u in all) {
                if (u.englishName.lowercase(Locale.getDefault()).contains(query)) {
                    list.add(u)
                }
            }
            SurahListAdapter(context, list as ArrayList<SurahList>)
            Log.e("UserPresenter", "found " + list.size + " users for query " + query)
        }
        adapter!!.notifyDataSetChanged()

    }


}