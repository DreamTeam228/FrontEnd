package msc.fooxer.studplaces

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_favorites.*
import kotlinx.android.synthetic.main.activity_main.*
import msc.fooxer.studplaces.MainActivity.Storage.ELEMENTS
import msc.fooxer.studplaces.MainActivity.Storage.FAVORITES

class Favorites : AppCompatActivity() {
   /* fun setFavorites ()
    {
    for (item in ELEMENTS)
        if (item.isFavorite) FAVORITES.add(item)
    }*/
    // заполнение и очистка favorites производится при нажатии на кнопку в Information

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)
        setSupportActionBar(toolbar)
        //setFavorites()
        val recyclerView = findViewById <RecyclerView> (R.id.list)
        val adapter: FavAdapter = FavAdapter(this, FAVORITES)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        //MainActivity.FAVORITES.clear()
    }
}
