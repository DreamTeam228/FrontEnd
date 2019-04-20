package msc.fooxer.studplaces

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import android.util.Log
import android.app.ActionBar
import android.content.Intent
import android.view.MenuItem

import kotlinx.android.synthetic.main.activity_information.*
import kotlinx.android.synthetic.main.content_information.*

class Information : AppCompatActivity() {
    companion object {

        // Для полноценной обработки объекта, нам необходим
        // массив объектов и позиция обрабатываемого объекта
        var POSITION = 0

        //Пока костыльный способ отображения информации объектов избранного
        //Выяснение, откуда запущена активити
        var FROM_FAV = false
        var FROM_WHERE = "NAME"

        //
        var REMOVE_FLAG = false

        //
        var ELEMENTS_INDEX = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_information)
        //setSupportActionBar(toolbar)
        takeFromIntent()
        val bar = supportActionBar
        bar!!.setDisplayHomeAsUpEnabled(true)


        fab.setOnClickListener { view ->
                // проблема в том, что позиции объектов в избранном и в мэин активити не совпадают
                // поэтому возникает конфликт
                // в идеале бы сделать это все через указатели, которых в java нет
            if (FROM_FAV) { // Заупск из избранного
                REMOVE_FLAG = false

                //Log.d(
                //   "==POS_IN_ELEM==" ,"POS IN ELEMS IS $ELEMENTS_INDEX IN FAV $POSITION AND NAME IS ${MainActivity.FAVORITES[POSITION].text}"
                //) // все работает
                MainActivity.ELEMENTS[ELEMENTS_INDEX].isFavorite = !MainActivity.ELEMENTS[ELEMENTS_INDEX].isFavorite
                //MainActivity.FAVORITES[POSITION].isFavorite = MainActivity.ELEMENTS[ELEMENTS_INDEX].isFavorite
                if (MainActivity.ELEMENTS[ELEMENTS_INDEX].isFavorite) {
                    Snackbar.make(view, R.string.added_to_favorite, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show()
                    fab.setImageResource(R.drawable.delfav)
                    MainActivity.FAVORITES.add(MainActivity.ELEMENTS[ELEMENTS_INDEX])
                    REMOVE_FLAG = false
                } else {
                    Snackbar.make(view, R.string.removed_from_favorite, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show()
                        fab.setImageResource(R.drawable.addfav)
                    MainActivity.FAVORITES.remove(MainActivity.ELEMENTS[ELEMENTS_INDEX])
                    REMOVE_FLAG = true
                }
            }
            else { //запуск из главного меню
                MainActivity.ELEMENTS[POSITION].isFavorite = !MainActivity.ELEMENTS[POSITION].isFavorite
                Log.d(
                    "===FAVORITE_CHANGED===", "FLAG IS CHANGED TO " +
                            "${MainActivity.ELEMENTS[POSITION].isFavorite}"
                )

                if (MainActivity.ELEMENTS[POSITION].isFavorite) {
                    Snackbar.make(view, R.string.added_to_favorite, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show()
                    fab.setImageResource(R.drawable.delfav)
                    MainActivity.FAVORITES.add(MainActivity.ELEMENTS[POSITION])
                } else {
                    Snackbar.make(view, R.string.removed_from_favorite, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show()
                    fab.setImageResource(R.drawable.addfav)
                    MainActivity.FAVORITES.remove(MainActivity.ELEMENTS[POSITION])
                }
                }
            }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun takeFromIntent () {
        Information.POSITION = intent.getIntExtra("POSITION", 0)
        //метод выяснения, откуда запущена активити
        when (FROM_WHERE) {
            "MAIN" -> {
                FROM_FAV = false
                name.text = MainActivity.ELEMENTS[POSITION].text
                if (MainActivity.ELEMENTS[POSITION].price == 0) {
                    price.text = "Бесплатно"
                } else {
                    val str: String = MainActivity.ELEMENTS[POSITION].price.toString() + " рублей"
                    price.text = str
                }
                description.text = MainActivity.ELEMENTS[POSITION].description
                if (MainActivity.ELEMENTS[POSITION].isFavorite) {
                    fab.setImageResource(R.drawable.delfav)
                } else {
                    fab.setImageResource(R.drawable.addfav)
                }
            }
            "FAVORITE" -> {
                FROM_FAV = true
                name.text = MainActivity.FAVORITES[POSITION].text
                if (MainActivity.FAVORITES[POSITION].price == 0) {
                    price.text = "Бесплатно"
                } else {
                    val str: String = MainActivity.FAVORITES[POSITION].price.toString() + " рублей"
                    price.text = str
                }
                description.text = MainActivity.FAVORITES[POSITION].description
                ELEMENTS_INDEX = MainActivity.ELEMENTS.indexOf(MainActivity.FAVORITES[POSITION])
                if (MainActivity.FAVORITES[POSITION].isFavorite) {
                    fab.setImageResource(R.drawable.delfav)
                } else {
                    fab.setImageResource(R.drawable.addfav)
                }
            }
            "RANDOM" -> {
                FROM_FAV = true
                name.text = MainActivity.RANDOM_WEEK[POSITION].text
                description.text = MainActivity.RANDOM_WEEK[POSITION].description
                if (MainActivity.RANDOM_WEEK[POSITION].price == 0) {
                    price.text = "Бесплатно"
                } else {
                    val str: String = MainActivity.RANDOM_WEEK[POSITION].price.toString() + " рублей"
                    price.text = str
                }
                ELEMENTS_INDEX = MainActivity.ELEMENTS.indexOf(MainActivity.RANDOM_WEEK[POSITION])
                if (MainActivity.RANDOM_WEEK[POSITION].isFavorite) {
                    fab.setImageResource(R.drawable.delfav)
                } else {
                    fab.setImageResource(R.drawable.addfav)
                }
                            }
        }

        Log.d("===ELEMENT_TEXT===", "NAME OF ELEMENT IS ${name.text}")
        Log.d("===ELEMENT_POSITION===", "POSITION OF ELEMENT IS ${Information.POSITION}")
        Log.d("===ELEMENT_FAVORITE===", "FLAG OF ELEMENT IS ${MainActivity.ELEMENTS[POSITION].isFavorite}")
    }


}
