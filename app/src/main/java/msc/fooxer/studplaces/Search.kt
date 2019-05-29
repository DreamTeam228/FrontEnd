package msc.fooxer.studplaces

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_search.*
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import android.os.AsyncTask.execute
import android.support.v4.content.ContextCompat
import android.widget.Toast
import org.apache.http.*
import org.apache.http.client.ClientProtocolException
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.message.BasicNameValuePair
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.io.UnsupportedEncodingException


class Search : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        //val category_recycler = findViewById <RecyclerView> (R.id.category_view) // Recycler для категорий
        /*val adapter_category = SearchAdapter(this, CATEGORY)
        adapter_category.notifyDataSetChanged()        //true, если данные изменились
        category_recycler.adapter = adapter_category
        category_recycler.layoutManager = LinearLayoutManager(this)*/
        val metro_recycler = findViewById<RecyclerView>(R.id.metro_view) // Ресайклер для метро
        val adapter_metro = SearchAdapter(this, STATIONS)
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


            /*val retrofit = Retrofit.Builder()
                .baseUrl("http://trportal.ru/nekit/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

            val serverApi = retrofit.create(ServerApi::class.java)



            val call = serverApi.createRequest(arrayOf("Марьино", "Марьино"))
            call.enqueue(object : Callback<MutableList<Place>> {
                override fun onResponse(call: Call<MutableList<Place>>, response: Response<MutableList<Place>>) {
                    if (response.isSuccessful) {
                        // tasks available
                        MainActivity.RANDOM_WEEK = response.body()!!
                        if(MainActivity.RANDOM_WEEK.isNotEmpty())
                            Toast.makeText(it.context, "Data is downloaded", Toast.LENGTH_LONG).show() //ok
                        for (i in 0 until MainActivity.RANDOM_WEEK.size)
                            if (MainActivity.RANDOM_WEEK[i].id in MainActivity.FAV_INDEXES) MainActivity.RANDOM_WEEK[i].isFavorite = true

                        val i = Intent(it.context, Random::class.java)
                        ContextCompat.startActivity(it.context, i, null)
                    } else {
                        Log.e("ERROR", "response.isSuccessful? - ${response.isSuccessful}")
                        // error response, no access to resource?
                    }
                }

                override fun onFailure(call: Call<MutableList<Place>>, t: Throwable) {
                    Log.e("ERROR", "${t.printStackTrace()}")
                }
            })*/




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
}
