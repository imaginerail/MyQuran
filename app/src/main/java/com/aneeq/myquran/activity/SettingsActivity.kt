package com.aneeq.myquran.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.appcompat.widget.Toolbar
import com.aneeq.myquran.R
import java.util.*

class SettingsActivity : AppCompatActivity() {
    lateinit var toolbar: Toolbar
    lateinit var llContent: LinearLayout
    lateinit var txtTrans: TextView
    lateinit var tgb: SwitchCompat
    lateinit var txtYSTI: TextView
    lateinit var txtActualTrans: TextView
    lateinit var btnChangeEdition: Button
    lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        sharedPreferences = getSharedPreferences("Messenger Preferences", Context.MODE_PRIVATE)
        toolbar = findViewById(R.id.toolbar)
        llContent = findViewById(R.id.llContent)
        txtTrans = findViewById(R.id.txtTrans)
        tgb = findViewById(R.id.tgb)
        txtYSTI = findViewById(R.id.txtYSTI)
        txtActualTrans = findViewById(R.id.txtActualTrans)
        btnChangeEdition = findViewById(R.id.btnChangeEdition)

        setUpToolbar()


        if (sharedPreferences.getBoolean("transstate", true)) {
            tgb.isChecked = true
            setUpTextTranslate()
        } else {
            tgb.isChecked = false
            txtActualTrans.text = ""}


        tgb.setOnCheckedChangeListener { _, isChecked ->
            if (!isChecked) {
                txtActualTrans.text = ""
                sharedPreferences.edit().putBoolean("transstate", false).apply()
                Toast.makeText(this, "Translation Turned Off", Toast.LENGTH_SHORT).show()
            } else {
                setUpTextTranslate()
                sharedPreferences.edit().putBoolean("transstate", true).apply()
                Toast.makeText(this, "Translation Turned Back On", Toast.LENGTH_SHORT).show()
            }

        }

        btnChangeEdition.setOnClickListener {
            openConfirmDialog()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onBackPressed()
        return super.onOptionsItemSelected(item)
    }

    private fun setUpToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Settings"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
    }
//Locale(sq.language)
    private fun setUpTextTranslate() {
    txtActualTrans.text = "${getString(R.string.Identifier)} : ${
        sharedPreferences.getString(
            "identifier",
            "en.maududi"
        )
    }\n" +
            "${"Language"} : ${
                Locale(
                    sharedPreferences.getString(
                        "language",
                        "English"
                    )
                ).displayLanguage
            }\n" +
            "${getString(R.string.Name)} : ${sharedPreferences.getString("name", "Maududi")}\n" +
            "${getString(R.string.EnglishName)} : ${
                sharedPreferences.getString(
                    "englishName",
                    "Abul Ala Maududi"
                )
            }\n" +
            "${getString(R.string.Format)} : ${sharedPreferences.getString("format", "text")}\n" +
            "${getString(R.string.Type)} : ${sharedPreferences.getString("type", "translation")}\n"

}

    private fun openConfirmDialog() {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Message")
        dialog.setMessage("Are you sure you want to change the Edition?")
        dialog.setPositiveButton("Yes") { _, _ ->
            sharedPreferences.edit().clear().apply()
            startActivity(Intent(this, LanguagesActivity::class.java))
        }
        dialog.setNegativeButton("No") { _, _ ->
            dialog.show().cancel()
        }
        dialog.create()
        dialog.show()

    }
}