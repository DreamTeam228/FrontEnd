package msc.fooxer.studplaces

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

class Random : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_random)
        val recyclerView = findViewById <RecyclerView> (R.id.list)
        val adapter: CustomAdapter = CustomAdapter(this, MainActivity.RANDOM_WEEK)
        //adapter.notifyDataSetChanged()        //true, если данные изменились
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }
    override fun onBackPressed (){
        super.onBackPressed()
        MainActivity.RANDOM_WEEK.clear()
    }
}


