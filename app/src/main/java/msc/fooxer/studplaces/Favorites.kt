package msc.fooxer.studplaces

import android.content.Intent
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

    // заполнение и очистка favorites производится при нажатии на кнопку в Information

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)
        setSupportActionBar(toolbar)
        val recyclerView = findViewById <RecyclerView> (R.id.list)
        val adapter: FavAdapter = FavAdapter(this, FAVORITES)
        //adapter.notifyDataSetChanged()        //true, если данные изменились
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    // НЕ РАБОТАЕТ ОБНОВЛЕНИЕ АКТИВИТИ, ПОСЛЕ ВЫХОДА ИЗ ФАВ (9((
    override fun onResume() {
        super.onResume()
        if (Information.REMOVE_FLAG) {
            finish()
            startActivity(Intent(this, this.javaClass))
            Information.REMOVE_FLAG = !Information.REMOVE_FLAG
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        //MainActivity.FAVORITES.clear()
    }
}
