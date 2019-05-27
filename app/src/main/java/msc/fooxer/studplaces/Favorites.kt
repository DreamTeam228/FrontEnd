package msc.fooxer.studplaces

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import msc.fooxer.studplaces.MainActivity.Storage.FAVORITES

class Favorites : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: CustomAdapter
    // заполнение и очистка favorites производится при нажатии на кнопку в Information

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)
        //setSupportActionBar(toolbar)
         recyclerView = findViewById <RecyclerView> (R.id.list)
         adapter = CustomAdapter(this, FAVORITES)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        val bar = supportActionBar
        bar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onResume() {
        super.onResume()
        adapter.notifyDataSetChanged()

       /* if (MainActivity.REMOVE_FLAG) {
            finish()
            startActivity(Intent( this, this.javaClass))
            MainActivity.REMOVE_FLAG = !MainActivity.REMOVE_FLAG
        }*/

    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        //MainActivity.FAVORITES.clear()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
