package com.aneeq.myquran


import android.app.Dialog
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Button
import android.widget.TextView
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
import com.tonyodev.fetch2.*
import com.tonyodev.fetch2core.DownloadBlock


class TestMusicGithubActivity : AppCompatActivity() {
    lateinit var mProgressDialog: Dialog
    lateinit var txtresult: TextView
    lateinit var btn: Button
    lateinit var btnd: Button
    private lateinit var jcplayer: JcPlayerView
    lateinit var argmusicplayer: ArgPlayerSmallView
    lateinit var psvLang: PowerSpinnerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_music_github)
        btn = findViewById(R.id.btn)
        txtresult = findViewById(R.id.txtresult)
        btnd = findViewById(R.id.btnd)
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

            checkPermissions()
            downloadFetch()

        }
        btnd.setOnClickListener {

        }

    }

    private fun checkPermissions() {
        var isAvailable = false
        var isWritable = false
        var isReadable = false
        val state = Environment.getExternalStorageState()

        if (Environment.MEDIA_MOUNTED == state) {
            // Operation possible - Read and Write
            isAvailable = true
            isWritable = true
            isReadable = true
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY == state) {
            // Operation possible - Read Only
            isAvailable = true
            isWritable = false
            isReadable = true
        } else {
            // SD card not available
            isAvailable = false
            isWritable = false
            isReadable = false
        }


    }

    private fun downloadFetch() {
        val fetchConfiguration = FetchConfiguration.Builder(this)
            .setDownloadConcurrentLimit(3)
            .build()

        val fetch = Fetch.Impl.getInstance(fetchConfiguration)

        val url = "https://everyayah.com/data/Abdul_Basit_Murattal_64kbps/001002.mp3"
        val file = "/downloads/test001002.mp3"

        val request = Request(url, file)
        request.priority = Priority.HIGH
        request.networkType = NetworkType.ALL
        //request.addHeader("clientKey", "SD78DF93_3947&MVNGHE1WONG");

        fetch.enqueue(request, { updatedRequest ->

            Toast.makeText(
                this,
                "Request was successfully enqueued for download",
                Toast.LENGTH_SHORT
            ).show()
        }, { error ->
            Toast.makeText(
                this,
                "${error.name} An error occurred enqueuing the request.",
                Toast.LENGTH_SHORT
            )
                .show()
        })

        val fetchListener = (object : FetchListener {
            override fun onAdded(download: Download) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(download: Download) {
                TODO("Not yet implemented")
            }

            override fun onCompleted(download: Download) {
                Log.d("cdd", "${download.file}")
                fetch.getDownloads {
                    val s = "file: ${it[0].file}\n" +
                            "namespace: ${it[0].namespace}"
                    txtresult.text = s
                }
            }

            override fun onDeleted(download: Download) {
                TODO("Not yet implemented")
            }

            override fun onDownloadBlockUpdated(
                download: Download,
                downloadBlock: DownloadBlock,
                totalBlocks: Int
            ) {
                TODO("Not yet implemented")
            }

            override fun onError(download: Download, error: Error, throwable: Throwable?) {
                TODO("Not yet implemented")
            }

            override fun onPaused(download: Download) {
                TODO("Not yet implemented")
            }

            override fun onProgress(
                download: Download,
                etaInMilliSeconds: Long,
                downloadedBytesPerSecond: Long
            ) {
                TODO("Not yet implemented")
            }

            override fun onQueued(download: Download, waitingOnNetwork: Boolean) {
                TODO("Not yet implemented")
            }

            override fun onRemoved(download: Download) {
                TODO("Not yet implemented")
            }

            override fun onResumed(download: Download) {
                TODO("Not yet implemented")
            }

            override fun onStarted(
                download: Download,
                downloadBlocks: List<DownloadBlock>,
                totalBlocks: Int
            ) {
                TODO("Not yet implemented")
            }

            override fun onWaitingNetwork(download: Download) {
                TODO("Not yet implemented")
            }

        })

        fetch.addListener(fetchListener)
        fetch.close()
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
                Toast.makeText(applicationContext, " $pos ${item.text}", Toast.LENGTH_SHORT)
                    .show()
            }
            getSpinnerRecyclerView().layoutManager = GridLayoutManager(baseContext, 2)
            // selectItemByIndex(4)
            preferenceName = "language"
        }
    }
}



