package msc.fooxer.studplaces

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.content_information.*

class InformationEdited : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_information_edited)

        val place: Place = intent.getParcelableExtra("elements")

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
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
