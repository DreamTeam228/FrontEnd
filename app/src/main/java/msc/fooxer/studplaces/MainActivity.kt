package msc.fooxer.studplaces

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import java.util.ArrayList

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    companion object Storage {
        // Здесь начинается эмулятор базы данных
        var IMAGES : Array<Int> = arrayOf(R.drawable.zoo, R.drawable.cinema, R.drawable.yard)
        val NAMES: Array <String> = arrayOf("Зоопарк", "Кинотеатр \"Люксор\"", "Антикафе \"12 ярдов\"")
        var IS_FAVORITES : Array<Boolean> = arrayOf(false, false, false)
        val PRICES: Array <Int> = arrayOf(150, 2000, 124000)
        // Здесь заканчивается эмулятор базы данных

        // Это глобальный массив объектов, отображающихся на экране
        var ELEMENTS: MutableList<DataModel> = ArrayList()
    }

    private fun setElements() {
        for (i in 0 until NAMES.size) {
            ELEMENTS.add(DataModel(IMAGES[i], NAMES[i], PRICES[i], IS_FAVORITES[i]))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        setElements()
        val recyclerView = findViewById <RecyclerView> (R.id.list)
        val adapter: CustomAdapter = CustomAdapter(this, ELEMENTS)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
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
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.Favorite -> {
                // Handle the camera action
            }
            R.id.Random -> {

            }
            R.id.Random_week -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
