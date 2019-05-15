package msc.fooxer.studplaces

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.content.ContextCompat.startActivity
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.ArrayList
import kotlin.collections.MutableMap


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    // Переменная на массив мест


    companion object Storage { // Массивы Констант (метро, категории) - в файле Constants
        // Здесь начинается эмулятор базы данных


        // Это глобальный массив объектов, отображающихся на экране
        //var ELEMENTS: MutableList<DataElement> = ArrayList()
        var ELEMENTS:ArrayList<Place> = ArrayList()
        var FAVORITES: MutableList<Place> = ArrayList()
        var RANDOM_WEEK: MutableList<Place> = ArrayList()
        var REMOVE_FLAG: Boolean = false
        var pla: DataPlaces = DataPlaces()
    }

    /* private fun setElements() {
         for (i in 0 until NAMES.size) {
             ELEMENTS.add(DataElement(IMAGES[i], NAMES[i], DESCRIPTIONS[i], PRICES[i], IS_FAVORITES[i]))
         }
     }*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //setSupportActionBar(toolbar)
        // val bar = supportActionBar
        //setElements()
        ELEMENTS = intent.getParcelableArrayListExtra<Place>("dp_ELEMENTS")
        val recyclerView = findViewById <RecyclerView> (R.id.list)
        val adapter: CustomAdapter = CustomAdapter(this, ELEMENTS)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        search_button.setOnClickListener {
            val search = Intent(this, Search::class.java)
            startActivity(search)
        }

        nav_view.setNavigationItemSelectedListener(this)
    }
    // Сюда будут приходить разные линки(рандом или фулл или поиск)



    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
            finish()
            ELEMENTS.clear()
            //System.exit(0)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.app_bar_search -> return true
            else -> return super.onOptionsItemSelected(item)
        }

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.Favorite -> {
                Information.FROM_WHERE = "FAVORITE"
                var fav = Intent (this, Favorites::class.java)
                startActivity(fav)
            }
            R.id.Random -> {
                var rand = Intent (this, Information::class.java)
                val element = Math.random()*10
                rand.putExtra("POSITION", element.toInt()%ELEMENTS.size)
                rand.putExtra("element", ELEMENTS[element.toInt()%ELEMENTS.size])
                // заполнить одним элементом
                startActivity(rand)
            }
            R.id.Random_week -> {
                Information.FROM_WHERE = "RANDOM"
                var asynkTaskRandomWeek = Random_AsyncTask(this)
                asynkTaskRandomWeek.execute()
            }
        }


        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
    override fun onResume() {
        super.onResume()
        Information.FROM_WHERE = "MAIN"
    }
}

