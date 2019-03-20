package msc.fooxer.studplaces

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView

import kotlinx.android.synthetic.main.activity_information.*
import kotlinx.android.synthetic.main.content_information.*

class Information : AppCompatActivity() {
    companion object {
        const val NAME = "Place"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_information)
        setSupportActionBar(toolbar)
        takeFromIntent()

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Added to Favorites", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
            fab.setImageResource(R.drawable.delfav)
        }
    }
    fun takeFromIntent () {
        val inf : String = intent.getStringExtra(NAME)
        name.text = inf
    }
}
