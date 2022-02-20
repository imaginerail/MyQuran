package com.aneeq.myquran.adapter.audio

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aneeq.myquran.R
import com.aneeq.myquran.models.AudioClass
import com.arges.sepan.argmusicplayer.Enums.AudioType
import com.arges.sepan.argmusicplayer.Models.ArgAudio
import com.arges.sepan.argmusicplayer.Models.ArgAudioList
import com.arges.sepan.argmusicplayer.PlayerViews.ArgPlayerSmallView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ReciteAudioAdapter(
    val context: Context,
    var slList: ArrayList<AudioClass>,
    var argmusicplayer: ArgPlayerSmallView,
    var fabplay: FloatingActionButton
) :
    RecyclerView.Adapter<ReciteAudioAdapter.SLViewHolder>() {

    lateinit var sharedPreferences: SharedPreferences
    var url = ""

    class SLViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ll1: LinearLayout = view.findViewById(R.id.ll1)
        val txtquran: TextView = view.findViewById(R.id.txtquran)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SLViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.juz_surah_audio_single_row, parent, false)
        return SLViewHolder(view)
    }

    override fun getItemCount(): Int {
        Log.d("size", slList.size.toString())
        return slList.size

    }

    override fun onBindViewHolder(holder: SLViewHolder, position: Int) {
        fabplay.visibility = View.VISIBLE
        argmusicplayer.visibility = View.VISIBLE
        sharedPreferences =
            context.getSharedPreferences("Messenger Preferences", Context.MODE_PRIVATE)
        val sq = slList[position]
        holder.txtquran.text = "${sq.surahNum}:${sq.ayahNum} ${sq.text} \u06DD"
        holder.ll1.setOnClickListener {
            argmusicplayer.play(
                ArgAudio(
                    "", "", sq.audioUrl, AudioType.URL
                )
            )

        }

        fabplay.setOnClickListener {

            val playlist = ArgAudioList(true)
            for (i in 0 until slList.size) {
                playlist.add(
                    ArgAudio(
                        "",
                        "",
                        slList[i].audioUrl,
                        AudioType.URL
                    )
                )
            }
            argmusicplayer.playPlaylist(playlist)
            argmusicplayer.continuePlaylistWhenError()
        }

    }
}







