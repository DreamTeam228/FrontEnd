package msc.fooxer.studplaces

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.content.ContextCompat.startActivity
import android.support.v4.view.GravityCompat
import android.support.v4.view.MenuItemCompat.getActionView
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.util.*
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.processors.PublishProcessor
import io.reactivex.schedulers.Schedulers
import msc.fooxer.studplaces.MainActivity.Storage.ELEMENTS
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

lateinit var adapter : CustomAdapter
lateinit var recyclerView : RecyclerView

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val compositeDisposable = CompositeDisposable()
    private val paginator = PublishProcessor.create<Int>()
    private var layoutManager: LinearLayoutManager = LinearLayoutManager(this)
    private var loading = true
    private var isSearchActive = false
    private var pageNumber = 1
    private val VISIBLE_THRESHOLD = 1
    private var lastVisibleItem: Int = 0
    private var totalItemCount:Int = 0

    companion object Storage { // Массивы Констант (метро, категории) - в файле Constants
        // Здесь начинается эмулятор базы данных


        // Это глобальный массив объектов, отображающихся на экране
        lateinit var dbh : DBHelper
        lateinit var db : SQLiteDatabase
        var ELEMENTS:ArrayList<Place> = ArrayList()
        var FAVORITES: MutableList<Place> = ArrayList()
        var RANDOM_WEEK: MutableList<Place> = ArrayList()
        var FAV_INDEXES: ArrayList<Int> = ArrayList()
        var REMOVE_FLAG: Boolean = false
        var pla: DataPlaces = DataPlaces()
        //var dateFormat: DateFormat = SimpleDateFormat("EEE, dd.MM.yyyy, HH:mm:ss", Locale.getDefault())
        fun changeFav (place: Place) {
            REMOVE_FLAG = false
            place.isFavorite = !place.isFavorite
           //if (ELEMENTS.isNotEmpty())
               ELEMENTS.find {
                it.id == place.id
            }?.isFavorite = place.isFavorite
            if (RANDOM_WEEK.isNotEmpty()) {
                RANDOM_WEEK.find{
                    it.id == place.id
                }?.isFavorite = place.isFavorite
            }
           if (place.isFavorite) {
                FAVORITES.add(0,place)
                FAV_INDEXES.add(place.id)
                addToTable(FAV_TABLE_NAME,place)

            } else {
                FAVORITES.remove (
                    FAVORITES.find {
                        it.id == place.id
                    }
                )
                FAV_INDEXES.remove(place.id)
                deleteFromTable(FAV_TABLE_NAME, place)

            }


        }
        fun addToTable (table: String, place: Place) {
            val cv = ContentValues()
            cv.put(KEY_INDEX, place.id)
            cv.put(KEY_NAME, place.name)
            cv.put(KEY_DESCR, place.description)
            cv.put(KEY_CATEGORY, place.category)
            cv.put(KEY_METRO, place.metro)
            cv.put(KEY_ADDRESS, place.address)
            cv.put(KEY_PRICE, place.price)
            cv.put(KEY_PHONE, place.phoneNumbers)
            cv.put(KEY_PIC, place.picture)
            cv.put(KEY_FAV, if(place.isFavorite) 1 else 0)
            cv.put(KEY_DATE,Date().time)
            cv.put(KEY_DISCOUNT,place.discount)
            cv.put(KEY_URL,place.url)
            db.insert(table,null,cv)
            Log.d("mLog", "New note was added into $table")
        }
        fun deleteFromTable (table: String, place:Place) {
            db.delete(table, "$KEY_INDEX = ${place.id}", null)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ELEMENTS = intent.getParcelableArrayListExtra<Place>("dp_ELEMENTS")

        recyclerView = findViewById<RecyclerView>(R.id.list)
        adapter = CustomAdapter(this, ELEMENTS)
        //adapter.notifyDataSetChanged()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = layoutManager

        setSupportActionBar(toolbar)
        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        search_button.setOnClickListener {
            val search = Intent(this, Search::class.java)
            startActivity(search)
        }
         //наверняка нужно расположить это в другом месте
        nav_view.setNavigationItemSelectedListener(this)

        setUpLoadMoreListener()     // Это скроллЛистенер для ресайклера
        subscribeForData()          // Это подгрузка данных
    }
    // Сюда будут приходить разные линки(рандом или фулл или поиск)



    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()

            //System.exit(0)
        }
    }

    @SuppressLint("CheckResult")
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        lateinit var mainSearchAsync : MainSearch_AsyncTask
        menuInflater.inflate(R.menu.main, menu)
        val searchItem = menu.findItem(R.id.app_bar_search)
        val searchLine = getActionView(searchItem) as SearchView

        Observable.create(ObservableOnSubscribe<String> { subscriber ->
            searchLine.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener,
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
                RANDOM_WEEK.clear()
                isSearchActive = true
                Log.d("SEARCH LINE", "RECYCLER IS ACTIVE?: ${!isSearchActive}")
                var showEmptyFlag = false
                if (ELEMENTS.isNotEmpty()) {
                    //for (i in 0 until ELEMENTS.size) {
                      //  if (text in ELEMENTS[i].name.toLowerCase()) {
                            //RANDOM_WEEK.add(ELEMENTS[i])
                            mainSearchAsync = MainSearch_AsyncTask(this, text)
                            mainSearchAsync.execute()
                            //adapter.setData(RANDOM_WEEK)
                            showEmptyFlag = true
                     //   }
                   // }
                    if (!showEmptyFlag) adapter.setData(RANDOM_WEEK)
                    if (text.isNullOrBlank()) {
                        adapter.setData(ELEMENTS)
                        isSearchActive = false
                        Log.d("SEARCH LINE", "RECYCLER IS ACTIVE?: ${!isSearchActive}")

                    }
                }

            }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.app_bar_search -> {
                //startActivity(Intent(this, Random::class.java))
                return true}
            R.id.filter -> {
                val filter = Intent(this, Search::class.java)
                startActivity(filter)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }


    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.Favorite -> {
                var fav = Intent (this, Favorites::class.java)
                startActivity(fav)
            }
            R.id.Random -> {
                var rand = Intent (this, Information::class.java)
                val element = Math.random()*10
                rand.putExtra("POSITION", element.toInt()%ELEMENTS.size)
                rand.putExtra("element", ELEMENTS[element.toInt()%ELEMENTS.size])
                // заполнить одним элементом
                startActivity(rand)
            }
            R.id.Random_week -> {
                var asynkTaskRandomWeek = Random_AsyncTask(this)
                asynkTaskRandomWeek.execute()
            }
        }


        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onDestroy() {

        ELEMENTS.clear()
        super.onDestroy()
    }

    /**
     * Тут скроллЛисенер для ресайклера,
     * который сообщает, что мы посмотрели все элементы
     * и пора грузить новые
     */
    private fun setUpLoadMoreListener() {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(
                recyclerView: RecyclerView,
                dx: Int, dy: Int
            ) {
                super.onScrolled(recyclerView, dx, dy)
                totalItemCount = layoutManager.itemCount
                lastVisibleItem = layoutManager.findLastVisibleItemPosition()
                if (!isSearchActive
                    && !loading
                    && totalItemCount <= lastVisibleItem + VISIBLE_THRESHOLD) {    // Выясняем, что пора подгрузить очередные данные
                    pageNumber++
                    paginator.onNext(pageNumber)                                            // В этом месте запускается сама загрузка данных
                    loading = true
                }
            }
        })
    }

    // Запускаем загрузку данных, прогрессБар, управляем потоками при загрузке
    private fun subscribeForData() {
        val disposable = paginator
            .onBackpressureDrop()
            .doOnNext { page ->
                loading = true
                progressBarMainActivity.visibility = View.VISIBLE
            }
            .concatMapSingle<ArrayList<Place>> { page ->
                dataFromNetwork(page)
                    .subscribeOn(Schedulers.io())
                    .doOnError { throwable ->
                        Log.e("ERROR", "$throwable.stackTrace")
                    }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { items ->
                adapter.setData(items)
                loading = false
                progressBarMainActivity.visibility = View.INVISIBLE
            }

        compositeDisposable.add(disposable)

        paginator.onNext(pageNumber)
    }

    // Получение очередных данных с сервака
    private fun dataFromNetwork(page: Int): Single<ArrayList<Place>> {
        return Single.just(true)
            .delay(1, TimeUnit.SECONDS)
            .map { value ->
                if (ELEMENTS.isNotEmpty()) {
                    //val items = java.util.ArrayList<Place>()
                    ELEMENTS.add(ELEMENTS[0])
                    ELEMENTS.removeAt(0)
                    // У нас здесь будет подгрузка с сервака
                    // Типо ELEMENTS.add(getDataFromDB())

                    /*for (i in 1..10) {
                    items.add("Item " + (page * 10 + i))
                }*/
                }
                ELEMENTS
            }
    }
        }
//}

