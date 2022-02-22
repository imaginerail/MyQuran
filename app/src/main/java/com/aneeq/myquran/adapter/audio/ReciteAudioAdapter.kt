package com.aneeq.myquran.adapter.audio

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
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
    var fabplay: FloatingActionButton, val mRecyclerView: RecyclerView
) :
    RecyclerView.Adapter<ReciteAudioAdapter.SLViewHolder>() {

    lateinit var sharedPreferences: SharedPreferences
    var url = ""
    val playlist = ArgAudioList(true)

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

        checkIfPlaying(holder, position, slList)

        holder.ll1.setOnClickListener {
            argmusicplayer.play(
                ArgAudio(
                    "", "", sq.audioUrl, AudioType.URL
                )
            )
            sq.isPlaying = true
            checkIfPlaying(holder, position, slList)
            argmusicplayer.setOnCompletedListener {
                sq.isPlaying = false
                checkIfPlaying(holder, position, slList)
            }

        }

        fabplay.setOnClickListener {

            playlist.clear()

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

            argmusicplayer.setOnPlayingListener {
                slList[argmusicplayer.currentAudio.id].isPlaying = true
                checkIfPlaying(holder, position, slList)

                (mRecyclerView.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(
                    argmusicplayer.currentAudio.id,
                    0
                )



                Log.d("cp", "${argmusicplayer.currentAudio.id}")
            }

            argmusicplayer.setOnCompletedListener {
                slList[argmusicplayer.currentAudio.id].isPlaying = false
                checkIfPlaying(holder, position, slList)
            }
            argmusicplayer.continuePlaylistWhenError()
        }

    }

    fun checkIfPlaying(holder: SLViewHolder, position: Int, slList: ArrayList<AudioClass>) {
        if (slList[position].isPlaying) {
            holder.txtquran.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_np, 0, 0, 0)
            holder.ll1.setBackgroundColor(Color.parseColor("#50F157"))
        } else {
            holder.txtquran.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
            holder.ll1.setBackgroundColor(Color.parseColor("#CDDC39"))
        }
    }

}







