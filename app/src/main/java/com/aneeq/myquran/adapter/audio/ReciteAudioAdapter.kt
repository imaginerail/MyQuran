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
import com.aneeq.myquran.models.OriginalJuz
import com.arges.sepan.argmusicplayer.Enums.AudioType
import com.arges.sepan.argmusicplayer.Models.ArgAudio
import com.arges.sepan.argmusicplayer.PlayerViews.ArgPlayerSmallView
import com.example.jean.jcplayer.model.JcAudio
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ReciteAudioAdapter(
    val context: Context,
    var slList: ArrayList<OriginalJuz>, private val apiName: String,
    var argmusicplayer: ArgPlayerSmallView,
    var fabplay: FloatingActionButton
) :
    RecyclerView.Adapter<ReciteAudioAdapter.SLViewHolder>() {

    val jcAudios: ArrayList<JcAudio> = ArrayList()
    lateinit var sharedPreferences: SharedPreferences
    var surahref = ""
    var ayahref = ""
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


            surahref = when {
                sq.surahNum < 10 -> "00${sq.surahNum}"
                sq.surahNum in 10..99 -> "0${sq.surahNum}"
                else -> "${sq.surahNum}"
            }

            ayahref = when {
                sq.ayahNum < 10 -> "00${sq.ayahNum}"
                sq.ayahNum in 10..99 -> "0${sq.ayahNum}"
                else -> "${sq.ayahNum}"
            }
            url = "https://everyayah.com/data/$apiName/$surahref$ayahref.mp3"
            argmusicplayer.play(
                ArgAudio(
                    apiName, "", url, AudioType.URL
                )
            )

        }


        argmusicplayer.loadSingleAudio(
            ArgAudio(
                apiName, "", url, AudioType.URL
            )
        )

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







