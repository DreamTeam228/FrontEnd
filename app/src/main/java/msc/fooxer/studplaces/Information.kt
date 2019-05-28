package msc.fooxer.studplaces

import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_information.*
import kotlinx.android.synthetic.main.content_information.*
import kotlinx.android.synthetic.main.content_information.description
import kotlinx.android.synthetic.main.content_information.informationImage
import kotlinx.android.synthetic.main.content_information.name
import kotlinx.android.synthetic.main.content_information.price
import msc.fooxer.studplaces.MainActivity.Storage.changeFav

class Information : AppCompatActivity() {


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_information)

       val place: Place = intent.getParcelableExtra("element")
        if (place.isFavorite) fab.setImageResource(R.drawable.delfav) else fab.setImageResource(R.drawable.addfav)

        name.text = place.name
        price.text = if (place.price == 0) {
            if (place.discount == 0) {
                "Бесплатно"
            } else {
                "Скидка ${place.discount}%"
            }
        }
        else
            "${place.price} рублей"
        description.text = place.description
        Picasso.get()
            .load(place.picture)
            .error(R.drawable.samurai)
            .placeholder(R.color.Metro_Line9)
            .into(informationImage)
        address.text = "Метро: ${place.metro}\n${place.address}"
        phone.text = place.phoneNumbers
        phone.setOnClickListener{
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:${place.phoneNumbers}")
            startActivity(intent)
        }
        category.text = place.category
        url_clickable.text = place.url
        url_clickable.setOnClickListener{
            val intent = Intent (Intent.ACTION_VIEW)
            intent.data = Uri.parse(place.url)
            startActivity(Intent.createChooser(intent,getText(R.string.chooser))) // Почему chooser не открывается?((
        }



        val bar = supportActionBar
        bar!!.setDisplayHomeAsUpEnabled(true)


        fab.setOnClickListener { view ->
            changeFav(place)
            if (place.isFavorite) {
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

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }



}
