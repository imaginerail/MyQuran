package com.aneeq.myquran


import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.arges.sepan.argmusicplayer.Enums.AudioType
import com.arges.sepan.argmusicplayer.Models.ArgAudio
import com.arges.sepan.argmusicplayer.Models.ArgAudioList
import com.arges.sepan.argmusicplayer.PlayerViews.ArgPlayerSmallView
import com.example.jean.jcplayer.model.JcAudio
import com.example.jean.jcplayer.view.JcPlayerView
import com.skydoves.powerspinner.IconSpinnerAdapter
import com.skydoves.powerspinner.IconSpinnerItem
import com.skydoves.powerspinner.PowerSpinnerView


class TestMusicGithubActivity : AppCompatActivity() {
    lateinit var btn: Button
    private lateinit var jcplayer: JcPlayerView
    lateinit var argmusicplayer: ArgPlayerSmallView
    lateinit var psvLang: PowerSpinnerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_music_github)
        btn = findViewById(R.id.btn)
        psvLang = findViewById(R.id.psvLang)
        jcplayer = findViewById(R.id.jcplayer)
        argmusicplayer = findViewById(R.id.argmusicplayer)
        val ayah = arrayListOf(
            "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
            "الْحَمْدُ لِلَّهِ رَبِّ الْعَالَمِين",
            "الرَّحْمَٰنِ الرَّحِيمِ",
            "مَالِكِ يَوْمِ الدِّينِ",
            "إِيَّاكَ نَعْبُدُ وَإِيَّاكَ نَسْتَعِينُ",
            "اهْدِنَا الصِّرَاطَ الْمُسْتَقِيمَ",
            "صِرَاطَ الَّذِينَ أَنْعَمْتَ عَلَيْهِمْ غَيْرِ الْمَغْضُوبِ عَلَيْهِمْ وَلَا الضَّالِّينَ"
        )
        playJC(ayah)

        setuppsvLang()
        btn.setOnClickListener {
            testArgPl()
        }

    }


    private fun playJC(ayah: ArrayList<String>) {
        val jcAudios: ArrayList<JcAudio> = ArrayList()
        for (i in 1..7) {
            jcAudios.add(
                JcAudio.createFromURL(
                    ayah[i - 1],
                    "https://everyayah.com/data/ahmed_ibn_ali_al_ajamy_128kbps/00100$i.mp3"
                )
            )
            //Log.d("play", jcAudios[].toString())
        }
        jcplayer.initPlaylist(jcAudios, null)
    }

    private fun playAM() {

        var fl: ArgAudioList
        val list1 = ArgAudioList(true).add(
            ArgAudio(
                "",
                "",
                "https://everyayah.com/data/ahmed_ibn_ali_al_ajamy_128kbps/001001.mp3",
                AudioType.URL
            )
        )
        for (i in 2..7) {
            fl = recallAdd(
                list1,
                "https://everyayah.com/data/ahmed_ibn_ali_al_ajamy_128kbps/00100$i.mp3"
            )
            Log.d("fl", fl.currentAudio.toString())
        }
        //  argmusicplayer.playPlaylist(fl);

    }

    private fun recallAdd(list1: ArgAudioList, url: String): ArgAudioList {
        return list1.add(ArgAudio("", "", url, AudioType.URL))
    }

    private fun setuppsvLang() {
        psvLang.apply {
            setSpinnerAdapter(IconSpinnerAdapter(this))
            setItems(
                arrayListOf(
                    IconSpinnerItem(text = "ENGLISH"),
                    IconSpinnerItem(text = "ARABIC"),
                    IconSpinnerItem(text = "HINDI"),
                    IconSpinnerItem(text = "INDONESIAN"),
                    IconSpinnerItem(text = "URDU"),

                    )
            )
            setOnSpinnerItemSelectedListener<IconSpinnerItem> { _, _, pos, item ->
                Toast.makeText(applicationContext, " $pos ${item.text}", Toast.LENGTH_SHORT).show()
            }
            getSpinnerRecyclerView().layoutManager = GridLayoutManager(baseContext, 2)
            // selectItemByIndex(4)
            preferenceName = "language"
        }
    }

    private fun testArgPl() {
        val singleAudioList = ArrayList<ArgAudio>()

        for (i in 1 until 8) {
            singleAudioList.add(
                ArgAudio(
                    "",
                    "",
                    "https://everyayah.com/data/ahmed_ibn_ali_al_ajamy_128kbps/00100$i.mp3",
                    AudioType.URL
                )
            )
        }


        val c: Collection<ArgAudio> = singleAudioList
        val playlist = ArgAudioList(true).addAll(c)
        Log.d("coll", c.toString())
//        argmusicplayer.playPlaylist(playlist)
    }

}