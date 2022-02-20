package com.aneeq.myquran


import android.app.Dialog
import android.content.Context
import android.os.AsyncTask
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
import java.io.BufferedInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.net.URL
import java.net.URLConnection


class TestMusicGithubActivity : AppCompatActivity() {
    lateinit var mProgressDialog: Dialog
    lateinit var btn: Button
    lateinit var btnd: Button
    private lateinit var jcplayer: JcPlayerView
    lateinit var argmusicplayer: ArgPlayerSmallView
    lateinit var psvLang: PowerSpinnerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_music_github)
        btn = findViewById(R.id.btn)
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
            testArgPl()

        }
        btnd.setOnClickListener {
            val urlString =
                "https://file-examples.com/wp-content/uploads/2017/11/file_example_MP3_5MG.mp3"
//            DownloadAudioFromUrl(this).execute(urlString)
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
        val playlist = ArgAudioList(true)
        for (i in 1 until 8) {
            playlist.add(
                ArgAudio(
                    "",
                    "",
                    "https://everyayah.com/data/Abdul_Basit_Murattal_64kbps/00100$i.mp3",
                    AudioType.URL
                )
            )
        }
        argmusicplayer.playPlaylist(playlist)
        argmusicplayer.continuePlaylistWhenError()

    }

//    override fun onCreateDialog(id: Int): Dialog {
//        return when (id) {
//            DIALOG_DOWNLOAD_PROGRESS -> {
//                mProgressDialog = Dialog(this)
//                mProgressDialog.setCancelMessage("Downloading file..")
//                mProgressDialog.setCancelable(false)
//                mProgressDialog.show()
//                mProgressDialog
//            }
//            else -> null
//        }
//    }
}

class DownloadFileAsync(val context: Context) : AsyncTask<String?, String?, String?>() {
//    override fun onPreExecute() {
//        super.onPreExecute()
////        showDialog(DIALOG_DOWNLOAD_PROGRESS)
//    }
//
//    override fun onProgressUpdate(vararg values: String?) {
//        Log.d("ANDRO_ASYNC", progress[0])
//        mProgressDialog.setProgress(progress[0].toInt())
//    }
//
//    override fun onPostExecute(unused: String?) {
//        dismissDialog(DIALOG_DOWNLOAD_PROGRESS)
//    }

    override fun doInBackground(vararg aurl: String?): String? {
        var count: Int
        try {
            val url = URL(aurl[0])
            val conexion: URLConnection = url.openConnection()
            conexion.connect()
            val lenghtOfFile: Int = conexion.contentLength
            Log.d("ANDRO_ASYNC", "Lenght of file: $lenghtOfFile")
            val input: InputStream = BufferedInputStream(url.openStream())
            val output: OutputStream = FileOutputStream("/sdcard/some_photo_from_gdansk_poland.jpg")
            val data = ByteArray(1024)
            var total: Long = 0
            while (input.read(data).also { count = it } != -1) {
                total += count.toLong()
                publishProgress("" + (total * 100 / lenghtOfFile).toInt())
                output.write(data, 0, count)
            }
            output.flush()
            output.close()
            input.close()
        } catch (e: Exception) {
        }
        return null
    }
}