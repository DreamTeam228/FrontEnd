package msc.fooxer.studplaces

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.ListView

class Search : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        val category_recycler = findViewById <RecyclerView> (R.id.category_view) // Recycler для категорий
        val adapter_category = SearchAdapter(this, CATEGORY)
        adapter_category.notifyDataSetChanged()        //true, если данные изменились
        category_recycler.adapter = adapter_category
        category_recycler.layoutManager = LinearLayoutManager(this)
        val metro_recycler = findViewById<RecyclerView>(R.id.metro_view) // Ресайклер для метро
        val adapter_metro = SearchAdapter(this, METRO)
        //adapter_metro.notifyDataSetChanged()
        metro_recycler.adapter = adapter_metro
        metro_recycler.layoutManager = LinearLayoutManager(this)

    }
}
