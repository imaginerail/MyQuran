package com.aneeq.myquran.activity


import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.drawerlayout.widget.DrawerLayout
import com.aneeq.myquran.R
import com.aneeq.myquran.fragment.*
import com.google.android.material.bottomnavigation.BottomNavigationView

class NavigActivity : AppCompatActivity() {

    lateinit var sharedPreferences: SharedPreferences
    lateinit var bnv: BottomNavigationView
    lateinit var frame: FrameLayout
    lateinit var drawerLayout: DrawerLayout
    lateinit var coordinatorLayout: CoordinatorLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = getSharedPreferences("Messenger Preferences", Context.MODE_PRIVATE)

        setContentView(R.layout.activity_navig)

        drawerLayout = findViewById(R.id.drawerLayout)
        coordinatorLayout = findViewById(R.id.coordinatorLayout)
        bnv = findViewById(R.id.bnv)
        frame = findViewById(R.id.frame)
        setUpBNV()
        openHome()


    }

    private fun setUpBNV() {
        bnv.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.browse -> {
                    openHome()
                }
                R.id.favourites -> {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frame,
                            FavouritesFragment()
                        ).commit()
                }
                R.id.search -> {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frame,
                            SearchFragment()
                        ).commit()
                }
                R.id.audio -> {

                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frame,
                            AudioFragment()
                        ).commit()


                }
                R.id.about -> {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frame,
                            AboutFragment()
                        ).commit()
                }
            }
            return@setOnNavigationItemSelectedListener true
        }

    }

    private fun openHome() {
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.frame,
                BrowseFragment()
            ).commit()

    }

    override fun onBackPressed() {
        val selectedItemId = bnv.selectedItemId
        if (R.id.browse != selectedItemId) {
            openHome()
            bnv.menu.getItem(0).isChecked = true

        } else
            super.onBackPressed()
    }


}