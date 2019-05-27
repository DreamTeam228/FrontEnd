package msc.fooxer.studplaces

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_search.*
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

        find_button.setOnClickListener{
            // здесь грабим всю инфу из полей активити
            // и как-то пихаем её в джсон
            // и ещё отправляем её на сервак
            // получаем в ответ джсон, распаковываем его в массив
            // и запускаем новое активити, отфильтрованное

            // можно попробовать сделать через Рандом_АсинкТаск, заменив там ссылку и интент
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
}
