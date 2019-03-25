package msc.fooxer.studplaces

import android.icu.text.IDNA
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import android.util.Log
import android.widget.TextView

import kotlinx.android.synthetic.main.activity_information.*
import kotlinx.android.synthetic.main.content_information.*
import msc.fooxer.studplaces.Information.Companion.FROM_FAV
import msc.fooxer.studplaces.Information.Companion.POSITION

class Information : AppCompatActivity() {
    companion object {
        /*const val NAME = "Place"
        const val PRICE = "150"
        const val IMG = R.drawable.samurai
        var IS_FAVORITE = false*/

        // Для полноценной обработки объекта, нам необходим
        // массив объектов и позиция обрабатываемого объекта
        var POSITION = 0

        //Пока костыльный способ отображения информации объектов избранного
        //Выяснение, откуда запущена активити
        var FROM_FAV = false

        //
        var REMOVE_FLAG = false

        //
        var ELEMENTS_INDEX = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_information)
        setSupportActionBar(toolbar)
        takeFromIntent()
        if (FROM_FAV) { // если запуск из Избранного
            if (MainActivity.FAVORITES[POSITION].isFavorite) {
                fab.setImageResource(R.drawable.delfav)
            } else {
                fab.setImageResource(R.drawable.addfav)
            }
        } else { // Запуск из главного меню

            if (MainActivity.ELEMENTS[POSITION].isFavorite) {
                fab.setImageResource(R.drawable.delfav)
            } else {
                fab.setImageResource(R.drawable.addfav)
            }
        }

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

    fun takeFromIntent () {
        //val inf : String = intent.getStringExtra(NAME)
        Information.POSITION = intent.getIntExtra("POSITION", 0)
        // костыльный метод выяснения, откуда запущена активити
        Information.FROM_FAV = intent.getBooleanExtra("FROM_FAV", false)
        if (FROM_FAV) { // выясняем, откда запущено активити, чтобы верно работать с POSITION
            name.text = MainActivity.FAVORITES[POSITION].text
            ELEMENTS_INDEX = MainActivity.ELEMENTS.indexOf(MainActivity.FAVORITES[POSITION])
        }               // находим позицию выбранного элемента в ELEMENTS, чтобы изменить его флаг
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
