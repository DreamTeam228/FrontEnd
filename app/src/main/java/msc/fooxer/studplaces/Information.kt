package msc.fooxer.studplaces

import android.icu.text.IDNA
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import android.util.Log
import android.widget.TextView

import kotlinx.android.synthetic.main.activity_information.*
import kotlinx.android.synthetic.main.content_information.*

class Information : AppCompatActivity() {
    companion object {
        /*const val NAME = "Place"
        const val PRICE = "150"
        const val IMG = R.drawable.samurai
        var IS_FAVORITE = false*/

        // Для полноценной обработки объекта, нам необходим
        // массив объектов и позиция обрабатываемого объекта
        var POSITION = 0

        //пока костыльный способ отображения информации объектов избранного
        //через intent boolean extra
        var FROM_FAV = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_information)
        setSupportActionBar(toolbar)
        takeFromIntent()

        if (MainActivity.ELEMENTS[POSITION].isFavorite) {
            fab.setImageResource(R.drawable.delfav)
        } else {
            fab.setImageResource(R.drawable.addfav)
        }

        fab.setOnClickListener { view ->
            MainActivity.ELEMENTS[POSITION].isFavorite = !MainActivity.ELEMENTS[POSITION].isFavorite
            Log.d("===FAVORITE_CHANGED===", "FLAG IS CHANGED TO " +
                    "${MainActivity.ELEMENTS[POSITION].isFavorite}")

            if (MainActivity.ELEMENTS[POSITION].isFavorite) {
                Snackbar.make(view, R.string.added_to_favorite, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
                fab.setImageResource(R.drawable.delfav)
            } else {
                Snackbar.make(view, R.string.removed_from_favorite, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
                fab.setImageResource(R.drawable.addfav)
            }
        }
    }
    fun takeFromIntent () {
        //val inf : String = intent.getStringExtra(NAME)
        Information.POSITION = intent.getIntExtra("POSITION", 0)
        // костыльный метод выяснения, откуда запущена активити
        Information.FROM_FAV = intent.getBooleanExtra("FROM_FAV", false)
        if (FROM_FAV)
            name.text = MainActivity.FAVORITES[POSITION].text
        else
            name.text = MainActivity.ELEMENTS[POSITION].text
        //Information.IS_FAVORITE = intent.getBooleanExtra("IS_FAVORITE", false)
        Log.d("===ELEMENT_TEXT===", "NAME OF ELEMENT IS ${name.text}")
        Log.d("===ELEMENT_POSITION===", "POSITION OF ELEMENT IS ${Information.POSITION}")
        Log.d("===ELEMENT_FAVORITE===", "FLAG OF ELEMENT IS ${MainActivity.ELEMENTS[POSITION].isFavorite}")
    }

    /*override fun onDestroy() {
        super.onDestroy()
        MainActivity.ELEMENTS[Information.POSITION.toInt()].isFavorite  = Information.IS_FAVORITE
        Log.d("===ELEMENT_FAVORITE===", "(ON_DESTROY) FLAG OF ELEMENT IS " +
                "${MainActivity.ELEMENTS[Information.POSITION.toInt()].isFavorite}")
    }*/
}
