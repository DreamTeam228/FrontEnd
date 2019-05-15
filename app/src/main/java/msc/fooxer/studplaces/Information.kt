package msc.fooxer.studplaces

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import android.util.Log
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_information.*
import kotlinx.android.synthetic.main.activity_information_edited.*
import kotlinx.android.synthetic.main.content_information.*
import kotlinx.android.synthetic.main.content_information.description
import kotlinx.android.synthetic.main.content_information.informationImage
import kotlinx.android.synthetic.main.content_information.name
import kotlinx.android.synthetic.main.content_information.price
import msc.fooxer.studplaces.MainActivity.Storage.ELEMENTS
import msc.fooxer.studplaces.MainActivity.Storage.FAVORITES
import msc.fooxer.studplaces.MainActivity.Storage.RANDOM_WEEK

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

       val place: Place = intent.getParcelableExtra("element")
        if (place.isFavorite) fab.setImageResource(R.drawable.delfav) else fab.setImageResource(R.drawable.favs)

        name.text = place.name
        price.text = place.price
        description.text = place.description
        Picasso.get()
            .load(place.picture)
            .error(R.drawable.samurai)
            .placeholder(R.color.Metro_Line9)
            .into(informationImage)



        val bar = supportActionBar
        bar!!.setDisplayHomeAsUpEnabled(true)


        fab.setOnClickListener { view ->
            // проблема в том, что позиции объектов в избранном и в мэин активити не совпадают
            // поэтому возникает конфликт
            // в идеале бы сделать это все через указатели, которых в java нет
            if (FROM_FAV) { // Заупск из избранного
                REMOVE_FLAG = false

                ELEMENTS[ELEMENTS_INDEX].isFavorite = !ELEMENTS[ELEMENTS_INDEX].isFavorite
                //MainActivity.FAVORITES[POSITION].isFavorite = MainActivity.ELEMENTS[ELEMENTS_INDEX].isFavorite
                if (ELEMENTS[ELEMENTS_INDEX].isFavorite) {
                    Snackbar.make(view, R.string.added_to_favorite, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show()
                    fab.setImageResource(R.drawable.delfav)
                    MainActivity.FAVORITES.add(ELEMENTS[ELEMENTS_INDEX])
                    REMOVE_FLAG = false
                } else {
                    Snackbar.make(view, R.string.removed_from_favorite, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show()
                    fab.setImageResource(R.drawable.favs)
                    MainActivity.FAVORITES.remove(ELEMENTS[ELEMENTS_INDEX])
                    REMOVE_FLAG = true
                }
            }
            else { //запуск из главного меню
                ELEMENTS[POSITION].isFavorite = !ELEMENTS[POSITION].isFavorite
                Log.d(
                    "===FAVORITE_CHANGED===", "FLAG IS CHANGED TO " +
                            "${ELEMENTS[POSITION].isFavorite}"
                )

                if (ELEMENTS[POSITION].isFavorite) {
                    Snackbar.make(view, R.string.added_to_favorite, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show()
                    fab.setImageResource(R.drawable.delfav)
                    FAVORITES.add(ELEMENTS[POSITION])
                } else {
                    Snackbar.make(view, R.string.removed_from_favorite, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show()
                    fab.setImageResource(R.drawable.favs)
                    FAVORITES.remove(ELEMENTS[POSITION])
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun takeFromIntent () {
        POSITION = intent.getIntExtra("POSITION", 0)
        //метод выяснения, откуда запущена активити
        when (FROM_WHERE) {
            "MAIN" -> {
                FROM_FAV = false
               /* name.text = ELEMENTS[POSITION].name
                if (ELEMENTS[POSITION].price == "0") {
                    price.text = "Бесплатно"
                } else {
                    val str: String = "${ELEMENTS[POSITION].price} рублей"
                    price.text = str
                }
                description.text = ELEMENTS[POSITION].description
                if (ELEMENTS[POSITION].isFavorite) {
                    fab.setImageResource(R.drawable.delfav)
                } else {
                    fab.setImageResource(R.drawable.favs)
                }
                // !!!!!
                // Можно попробовать перенести в асинкТаск
                // !!!!!
                Picasso.get()
                    .load(ELEMENTS[POSITION].picture)
                    .error(R.drawable.samurai)
                    .placeholder(R.color.Metro_Line9)
                    .into(informationImage)*/
            }
            "FAVORITE" -> {
                FROM_FAV = true
               /* name.text = FAVORITES[POSITION].name
                if (FAVORITES[POSITION].price == "0") {
                    price.text = "Бесплатно"
                } else {
                    val str: String = "${FAVORITES[POSITION].price} рублей"
                    price.text = str
                }
                description.text = FAVORITES[POSITION].description*/
                ELEMENTS_INDEX = MainActivity.ELEMENTS.indexOf(FAVORITES[POSITION])
               /* if (FAVORITES[POSITION].isFavorite) {
                    fab.setImageResource(R.drawable.delfav)
                } else {
                    fab.setImageResource(R.drawable.favs)
                }
                //
                Picasso.get()
                    .load(FAVORITES[POSITION].picture)
                    .error(R.drawable.samurai)
                    .placeholder(R.color.Metro_Line9)
                    .into(informationImage)*/
            }
            "RANDOM" -> {
                FROM_FAV = true
                /*name.text = RANDOM_WEEK[POSITION].name
                description.text = RANDOM_WEEK[POSITION].description
                if (RANDOM_WEEK[POSITION].price == "0") {
                    price.text = "Бесплатно"
                } else {
                    val str: String = "${RANDOM_WEEK[POSITION].price} рублей"
                    price.text = str
                }*/
                ELEMENTS_INDEX = MainActivity.ELEMENTS.indexOf(RANDOM_WEEK[POSITION])
                /*if (RANDOM_WEEK[POSITION].isFavorite) {
                    fab.setImageResource(R.drawable.delfav)
                } else {
                    fab.setImageResource(R.drawable.favs)
                }
                Picasso.get()
                    .load(RANDOM_WEEK[POSITION].picture)
                    .error(R.drawable.samurai)
                    .placeholder(R.color.Metro_Line9)
                    .into(informationImage)*/
            }
        }

        /*Log.d("===ELEMENT_TEXT===", "NAME OF ELEMENT IS ${name.text}")
        Log.d("===ELEMENT_POSITION===", "POSITION OF ELEMENT IS ${POSITION}")
        Log.d("===ELEMENT_FAVORITE===", "FLAG OF ELEMENT IS ${ELEMENTS[POSITION].isFavorite}")*/
    }


}
