package msc.fooxer.studplaces

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
//import kotlinx.android.synthetic.main.activity_main2.*
import java.util.ArrayList

class Main2Activity : AppCompatActivity() {
    companion object Storage { // Массивы Констант (метро, категории) - в файле Constants
        // Здесь начинается эмулятор базы данных
        var IMAGES : Array<Int> = arrayOf(R.drawable.zoo, R.drawable.cinema, R.drawable.yard)
        val NAMES: Array <String> = arrayOf("Зоопарк", "Кинотеатр \"Люксор\"", "Антикафе \"12 ярдов\"")
        val DESCRIPTIONS: Array <String> = arrayOf("Зоопарк - описание", "Кинотеатр \"Люксор\" - описание",
            "Антикафе \"12 ярдов\" - описание")
        var IS_FAVORITES : Array<Boolean> = arrayOf(false, false, false)
        val PRICES: Array <Int> = arrayOf(0, 2000, 124000)
        // Здесь заканчивается эмулятор базы данных

        // Это глобальный массив объектов, отображающихся на экране
        var ELEMENTS: MutableList<DataElement> = ArrayList()
        var FAVORITES: MutableList<DataElement> = ArrayList()
        var RANDOM_WEEK: MutableList<DataElement> = ArrayList()
    }

    private fun setElements() {
        for (i in 0 until NAMES.size) {
            ELEMENTS.add(DataElement(IMAGES[i], NAMES[i], DESCRIPTIONS[i], PRICES[i], IS_FAVORITES[i]))
        }
    }
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
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
                // заполнить одним элементом
                startActivity(rand)
            }
            R.id.Random_week -> {
                Information.FROM_WHERE = "RANDOM"
                for (i in 0..2) {
                    RANDOM_WEEK.add(ELEMENTS[(Math.random()*10).toInt()% ELEMENTS.size])
                }
                var rand = Intent (this, Random::class.java)
                // заполнить массивом
                startActivity(rand)
            }
        }

        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setElements()
        val recyclerView = findViewById <RecyclerView> (R.id.list)
        val adapter: CustomAdapter = CustomAdapter(this, ELEMENTS)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }
    override fun onBackPressed() {

            super.onBackPressed()
            finish()
            ELEMENTS.clear()
            //System.exit(0)
    }

}
