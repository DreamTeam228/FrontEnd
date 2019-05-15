package msc.fooxer.studplaces

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.View
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_information.*
import kotlinx.android.synthetic.main.activity_information_edited.*
import kotlinx.android.synthetic.main.content_information.*
import kotlinx.android.synthetic.main.content_information.description
import kotlinx.android.synthetic.main.content_information.informationImage
import kotlinx.android.synthetic.main.content_information.name
import kotlinx.android.synthetic.main.content_information.price

class InformationEdited : AppCompatActivity(){

    lateinit var place: Place
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_information_edited)

        place = intent.getParcelableExtra("elements")

        name.text = place.name
        price.text = place.price.toString()
        description.text = place.description
        Picasso.get()
            .load(place.picture)
            .error(R.drawable.samurai)
            .placeholder(R.color.Metro_Line9)
            .into(informationImage)
            if (place.isFavorite) fab3.setImageResource(R.drawable.delfav) else fab3.setImageResource(R.drawable.favs)
        fab3.setOnClickListener{ view ->
            //MainActivity.changeFavorite(place)
            var tmp = MainActivity.ELEMENTS.indexOf(place)

            MainActivity.REMOVE_FLAG = if (!place.isFavorite) {
                place.isFavorite = !place.isFavorite
                MainActivity.FAVORITES.add(place)
                false
            } else {
                MainActivity.FAVORITES.remove(place)
                place.isFavorite = !place.isFavorite
                true
            }
            MainActivity.ELEMENTS[tmp].isFavorite = place.isFavorite
            if (place.isFavorite) {
                Snackbar.make(view, R.string.added_to_favorite, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
                fab3.setImageResource(R.drawable.delfav)
            } else {
                Snackbar.make(view, R.string.removed_from_favorite, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
                fab3.setImageResource(R.drawable.favs)
            }
        }
        val bar = supportActionBar
        bar!!.setDisplayHomeAsUpEnabled(true)
    }

   /* override fun onClick(view: View) {
        MainActivity.changeFavorite(place)
        if (place.isFavorite) {
            Snackbar.make(view, R.string.added_to_favorite, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
            fab3.setImageResource(R.drawable.delfav)
        } else {
            Snackbar.make(view, R.string.removed_from_favorite, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
            fab.setImageResource(R.drawable.addfav)
        }
    }*/

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
