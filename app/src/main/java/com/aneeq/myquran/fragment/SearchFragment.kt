package com.aneeq.myquran.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.aneeq.myquran.R
import com.aneeq.myquran.activity.SrchByRefNumActivity
import com.aneeq.myquran.activity.SrchByTxtActivity

class SearchFragment : Fragment() {

    lateinit var btnSBRN: Button
    lateinit var btnSRT: Button
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        btnSBRN = view.findViewById(R.id.btnSBRN)
        btnSRT = view.findViewById(R.id.btnSRT)

        btnSBRN.setOnClickListener {
            startActivity(Intent(context, SrchByRefNumActivity::class.java))
        }
        btnSRT.setOnClickListener {
            startActivity(Intent(context, SrchByTxtActivity::class.java))
        }

        return view
    }


}