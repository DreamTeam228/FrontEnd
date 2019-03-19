package msc.fooxer.studplaces

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView

import kotlinx.android.synthetic.main.activity_information.*

class Information : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_information)
        setSupportActionBar(toolbar)

        /*val name = findViewById<TextView>(R.id.name)
        val arguments = intent.extras
        name.text = arguments!!.get("NAME")!!.toString()*/

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Added to Favorites", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

}
