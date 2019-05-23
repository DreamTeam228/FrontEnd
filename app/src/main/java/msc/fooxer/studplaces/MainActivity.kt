package msc.fooxer.studplaces

import android.content.ContentValues
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {



    companion object Storage { // Массивы Констант (метро, категории) - в файле Constants
        // Здесь начинается эмулятор базы данных


        // Это глобальный массив объектов, отображающихся на экране
        lateinit var dbh : DBHelper
        lateinit var db : SQLiteDatabase
        var ELEMENTS:ArrayList<Place> = ArrayList()
        var FAVORITES: MutableList<Place> = ArrayList()
        var RANDOM_WEEK: MutableList<Place> = ArrayList()
        var FAV_INDEXES: ArrayList<Int> = ArrayList()
        var REMOVE_FLAG: Boolean = false
        var pla: DataPlaces = DataPlaces()
        //var dateFormat: DateFormat = SimpleDateFormat("EEE, dd.MM.yyyy, HH:mm:ss", Locale.getDefault())
        fun changeFav (place: Place) {
            REMOVE_FLAG = false
            place.isFavorite = !place.isFavorite
           //if (ELEMENTS.isNotEmpty())
               ELEMENTS.find {
                it.id == place.id
            }?.isFavorite = place.isFavorite
            if (RANDOM_WEEK.isNotEmpty()) {
                RANDOM_WEEK.find{
                    it.id == place.id
                }?.isFavorite = place.isFavorite
            }
            REMOVE_FLAG = if (place.isFavorite) {
                FAVORITES.add(place)
                FAV_INDEXES.add(place.id)
                addToTable(FAV_TABLE_NAME,place)
                false
            } else {
                FAVORITES.remove (
                    FAVORITES.find {
                        it.id == place.id
                    }
                )
                FAV_INDEXES.remove(place.id)
                deleteFromTable(FAV_TABLE_NAME, place)
                true
            }


        }
        fun addToTable (table: String, place: Place) {
            val cv = ContentValues()
            cv.put(KEY_INDEX, place.id)
            cv.put(KEY_NAME, place.name)
            cv.put(KEY_DESCR, place.description)
            cv.put(KEY_CATEGORY, place.Сategory)
            cv.put(KEY_METRO, place.metro)
            cv.put(KEY_ADDRESS, place.address)
            cv.put(KEY_PRICE, place.price)
            cv.put(KEY_PHONE, place.phoneNumbers)
            cv.put(KEY_PIC, place.picture)
            cv.put(KEY_FAV, if(place.isFavorite) 1 else 0)
            cv.put(KEY_DATE,Date().time)
            db.insert(table,null,cv)
            Log.d("mLog", "New note was added into $table")
        }
        fun deleteFromTable (table: String, place:Place) {
            db.delete(table, "$KEY_INDEX = ${place.id}", null)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ELEMENTS = intent.getParcelableArrayListExtra<Place>("dp_ELEMENTS")

            val recyclerView = findViewById<RecyclerView>(R.id.list)
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
         //наверняка нужно расположить это в другом месте
        nav_view.setNavigationItemSelectedListener(this)

    }
    // Сюда будут приходить разные линки(рандом или фулл или поиск)



    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()

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
                var asynkTaskRandomWeek = Random_AsyncTask(this)
                asynkTaskRandomWeek.execute()
            }
        }


        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onStop() {

        super.onStop()
    }

    override fun onDestroy() {

        ELEMENTS.clear()
        super.onDestroy()
    }



}

