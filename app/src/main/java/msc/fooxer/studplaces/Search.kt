package msc.fooxer.studplaces

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_search.*
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import msc.fooxer.studplaces.MainActivity.Storage.ELEMENTS
import msc.fooxer.studplaces.MainActivity.Storage.RANDOM_WEEK
import java.util.concurrent.TimeUnit

var checkedCategory : MutableList<String> = ArrayList()
var checkedMetro : MutableList<String> = ArrayList()
var METRO_TMP : ArrayList<SearchOption> = ArrayList()
var maxPrice: Int = 1000

class Search : AppCompatActivity() {

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        checkedCategory.clear()
        checkedMetro.clear()
        val category_recycler = findViewById <RecyclerView> (R.id.category_view) // Recycler для категорий
        val adapter_category = SearchAdapter(this, CATEGORY, CATEGORY_FILTER)
        adapter_category.notifyDataSetChanged()        //true, если данные изменились
        category_recycler.adapter = adapter_category
        category_recycler.layoutManager = LinearLayoutManager(this)
        val metro_recycler = findViewById<RecyclerView>(R.id.metro_view) // Ресайклер для метро
        val adapter_metro = SearchAdapter(this, STATIONS, STATION_FILTER)
        adapter_metro.notifyDataSetChanged()
        metro_recycler.adapter = adapter_metro
        metro_recycler.layoutManager = LinearLayoutManager(this)

        Observable.create(ObservableOnSubscribe<String> { subscriber ->
            metroSearch.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener,
                SearchView.OnQueryTextListener {

                override fun onQueryTextChange(newText: String): Boolean {
                    subscriber.onNext(newText)
                    return false
                }
                override fun onQueryTextSubmit(query: String): Boolean {
                    subscriber.onNext(query)
                    return false
                }
            })
        })
            .map { text -> text.toLowerCase().trim() }
            .debounce(750, TimeUnit.MILLISECONDS)
            //.distinct()
            //.filter { text -> text.isNotBlank() }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { text ->
                Log.d("SEARCH LINE REQUEST", "subscriber: $text")
                METRO_TMP.clear()
                var showEmptyFlag = false
                if (METRO_NEW.isNotEmpty()) {
                    for (i in 0 until STATIONS.size) {
                        if (text in STATIONS[i].text.toLowerCase()) {
                            METRO_TMP.add(STATIONS[i])
                            adapter_metro.setData(METRO_TMP)
                            showEmptyFlag = true
                        }
                    }
                    if (!showEmptyFlag) adapter_metro.setData(METRO_TMP)
                    if (text.isNullOrBlank()) {
                        adapter_metro.setData(STATIONS)
                    }
                }
            }

        find_button.setOnClickListener{
            maxPrice = price_bar.progress
            val async = Search_AsyncTask(this)
            async.execute()
        }

        price_bar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {}

            override fun onStartTrackingTouch(p0: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                priceValue.setText(seekBar!!.progress.toString())
            }
        })

        priceValue.setText(R.string.max_price)
        priceValue.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                if (!s.toString().contains('.') && s.isNotBlank()) {
                    when (val value = Integer.parseInt(s.toString())) {
                        in 1..1000 -> price_bar.progress = value
                        in 1000..Int.MAX_VALUE -> price_bar.progress = 1000
                        in Int.MIN_VALUE..0 -> price_bar.progress = 0
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })

        val bar = supportActionBar
        bar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        for (i in 0 until STATIONS.size)
            STATIONS[i].isChecked = false
        for (i in 0 until CATEGORY.size)
            CATEGORY[i].isChecked = false

    }
}
