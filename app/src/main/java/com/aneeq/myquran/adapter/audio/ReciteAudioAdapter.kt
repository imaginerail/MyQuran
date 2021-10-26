package com.aneeq.myquran.adapter.audio

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aneeq.myquran.R
import com.aneeq.myquran.models.AudioClass
import com.arges.sepan.argmusicplayer.Enums.AudioType
import com.arges.sepan.argmusicplayer.IndependentClasses.ArgAudio
import com.arges.sepan.argmusicplayer.PlayerViews.ArgPlayerSmallView
import com.example.jean.jcplayer.model.JcAudio
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ReciteAudioAdapter(
    val context: Context,
    var slList: ArrayList<AudioClass>,
    var argmusicplayer: ArgPlayerSmallView,
    var fabplay: FloatingActionButton
) :
    RecyclerView.Adapter<ReciteAudioAdapter.SLViewHolder>() {

    val jcAudios: ArrayList<JcAudio> = ArrayList()
    lateinit var sharedPreferences: SharedPreferences

    class SLViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ll1: LinearLayout = view.findViewById(R.id.ll1)
        val txtquran: TextView = view.findViewById(R.id.txtquran)
        val imgnp: ImageView = view.findViewById(R.id.imgnp)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SLViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.juz_surah_audio_single_row, parent, false)
        return SLViewHolder(view)
    }

    override fun getItemCount(): Int {
        return slList.size
    }

    override fun onBindViewHolder(holder: SLViewHolder, position: Int) {
        sharedPreferences =
            context.getSharedPreferences("Messenger Preferences", Context.MODE_PRIVATE)
        val sq = slList[position]
        holder.imgnp.visibility = View.GONE
        holder.txtquran.text = "${sq.numberInSurah}:${sq.number} ${sq.text} \u06DD"
        holder.ll1.setOnClickListener {
            argmusicplayer.play(
                ArgAudio(
                    sharedPreferences.getString(
                        "imam",
                        "Abdul Basit Murattal"
                    ), "", sq.audio, AudioType.URL
                )
            )
            argmusicplayer.loadSingleAudio(
                ArgAudio(
                    sharedPreferences.getString(
                        "imam",
                        "Abdul Basit Murattal"
                    ), "", sq.audio, AudioType.URL
                )
            )
        }
        // argmusicplayer.playLoadedSingleAudio()
        fabplay.setOnClickListener {
//            val audios = ArrayList<ArgAudio>()
//            for (i in 0..slList.size) {
//                audios.add(
//                    ArgAudio(
//                        sharedPreferences.getString(
//                            "imam",
//                            "Abdul Basit Murattal"
//                        ), "", slList[i].audio, AudioType.URL
//                    )
//                )
//            }
//            val playlist=audios[0].makePlaylist()
////            argmusicplayer.playPlaylist(playlist)
        }

    }
}







