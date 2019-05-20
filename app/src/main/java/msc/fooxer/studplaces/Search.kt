package msc.fooxer.studplaces

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.util.Log
import android.widget.SeekBar
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_search.*
import java.util.concurrent.TimeUnit
import android.text.Editable
import android.text.TextWatcher



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
        adapter_metro.notifyDataSetChanged()
        metro_recycler.adapter = adapter_metro
        metro_recycler.layoutManager = LinearLayoutManager(this)


        // Здесь выполняется обработка строки введённой в сёрчЛайн с RxJava2
        Observable.create(ObservableOnSubscribe<String> { subscriber ->
            searchLine.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextChange(newText: String?): Boolean {
                    subscriber.onNext(newText!!)
                    return false
                }

                override fun onQueryTextSubmit(query: String?): Boolean {
                    subscriber.onNext(query!!)
                    return false
                }
            })
        })
            .map { text -> text/*.toLowerCase()*/.trim() }
            .debounce(300, TimeUnit.MILLISECONDS)
            .distinct()
            .filter { text -> text.isNotBlank() }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { text ->
                Log.d("SEARCH LINE REQUEST", "subscriber: $text")
                textView.text = text
            }



        price_bar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {}

            override fun onStartTrackingTouch(p0: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                priceValue.setText(seekBar!!.progress.toString())
            }
        })

        priceValue.setText("1000")
        priceValue.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                if (!s.toString().contains('.') && !s.isBlank()) {
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
}
