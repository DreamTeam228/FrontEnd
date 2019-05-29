package msc.fooxer.studplaces

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.database.Cursor
import android.net.ConnectivityManager
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.ArrayList
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.lang.Math.log


class SplashScreen : AppCompatActivity() {
    var dp: ArrayList<Place> = ArrayList()

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MainActivity.dbh = DBHelper(this)
        MainActivity.db = MainActivity.dbh.writableDatabase
        if (MainActivity.FAVORITES.isNullOrEmpty()) fillingFromTable()
        if (!isNetworkAvailable(this)) {
            Toast.makeText(this, getText(R.string.connection_lost), Toast.LENGTH_LONG)
                .show() //вывод сообщения о соединении с интернетом
           } else
            Toast.makeText(this, getText(R.string.connection_ok), Toast.LENGTH_LONG).show()

            AsynkJson(this).execute()
        AsynkMetro(this).execute()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.hh.ru/metro/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

        val messagesApi = retrofit.create(MetroApi::class.java)

        messagesApi.getStations()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : DisposableSingleObserver<Array<Line>>() {
                override fun onSuccess(lines: Array<Line>) {
                    Log.d("onSuccess ", "${lines.size}")
                    for (i in 0 until lines.size) {
                        for(j in 0 until lines[i].stations.size) {
                            Log.d("STATION NAME", lines[i].stations[j].name)
                        }
                    }
                }

                override fun onError(e: Throwable) {
                    Log.e("onError ", "$e")
                }
            })
            /*.subscribe { lines ->
                Log.d("onSuccess ", "${lines.size}")
                for (i in 0 until lines.size) {
                    for(j in 0 until lines[i].stations.size) {
                        Log.d("STATION NAME", lines[i].stations[j].name)
                    }
                }
            }*/
    }


    inner class AsynkJson(private val context: Context) : AsyncTask<Void, Void, String>() {
        lateinit var json_url: String
        override fun onPreExecute() {
            json_url = getText(R.string.json_link).toString()
        }

        override fun doInBackground(vararg voids: Void): String {
            lateinit var url: URL
            var response = StringBuilder() // стринг не пересоздается, а изменяется
            try { // попытка создать юрл, хз почему нельзя просто взять и открыть
                url =
                    URL(json_url)
            } catch (e: MalformedURLException) {
                throw IllegalArgumentException("Invalid URL")
            }
            var conn: HttpURLConnection? = null
            try {
                conn = url.openConnection() as HttpURLConnection // открываем горе-ссылку
                conn.doOutput = false // вводить не можем
                conn.doInput = true // можем выводить
                conn.requestMethod = "GET" // get - получение ресурса

                val status = conn.responseCode // cостояние hhtp - 200 - ok
                if (status != 200) {
                    throw IOException("Post failed with error code $status")
                } else {
                    val instream = BufferedReader(InputStreamReader(conn.inputStream)) // открываем буферный поток ввода

                    var inputLine: String? = instream.readLine()
                    while ((inputLine) != null) { // читаем все строки
                        response.append(inputLine)
                        inputLine = instream.readLine()
                    }
                    instream.close()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                conn?.disconnect()
                val jsonString = response.toString().trim() // переводим в строку и удаляем ненужные пробелы
                Log.i(ContentValues.TAG, "Received JSON: $jsonString")
                return jsonString
            }
        }

        override fun onPostExecute(result: String) {
            super.onPostExecute(result)
            try {
                dp = MainActivity.pla.getInfo(result)
            } catch (e: Exception) {
                Toast.makeText(context, print(e.message).toString(), Toast.LENGTH_LONG).show()
            }
            if (dp.isNotEmpty()) {
                //Toast.makeText(context, "Data is downloaded", Toast.LENGTH_LONG).show() //ok
                            } else {
                Toast.makeText(context, getText(R.string.fav_cache), Toast.LENGTH_LONG).show() //ok
            }
            val i = Intent(baseContext, MainActivity::class.java)
            i.putParcelableArrayListExtra("dp_ELEMENTS", dp)
            startActivity(i)
            finish()

        }
    }

       fun fillingFromTable() {

                val cursor: Cursor = MainActivity.db.query(FAV_TABLE_NAME, null, null, null, null, null, "$KEY_DATE DESC")
                if (cursor.moveToFirst()) {
                    val idIndex = cursor.getColumnIndex(KEY_INDEX)
                    val nameIndex = cursor.getColumnIndex(KEY_NAME)
                    val descIndex = cursor.getColumnIndex(KEY_DESCR)
                    val priceIndex = cursor.getColumnIndex(KEY_PRICE)
                    val phoneIndex = cursor.getColumnIndex(KEY_PHONE)
                    val addressIndex = cursor.getColumnIndex(KEY_ADDRESS)
                    val metroIndex = cursor.getColumnIndex(KEY_METRO)
                    val catIndex = cursor.getColumnIndex(KEY_CATEGORY)
                    val picIndex = cursor.getColumnIndex(KEY_PIC)
                    val discIndex = cursor.getColumnIndex(KEY_DISCOUNT)
                    val urlIndex = cursor.getColumnIndex(KEY_URL)
                    do {
                        val place = Place(
                            cursor.getInt(idIndex),
                            cursor.getString(nameIndex),
                            cursor.getString(catIndex),
                            cursor.getString(descIndex),
                            cursor.getString(metroIndex),
                            cursor.getString(phoneIndex),
                            cursor.getInt(priceIndex),
                            cursor.getString(addressIndex),
                            cursor.getString(picIndex),
                            cursor.getInt(discIndex),
                            cursor.getString(urlIndex),
                            true
                        )
                        MainActivity.FAVORITES.add(place)
                        MainActivity.FAV_INDEXES.add(cursor.getInt(idIndex))
                    } while (cursor.moveToNext())

                    cursor.close()
                }


        }

    inner class AsynkMetro(val context : Context) : AsyncTask<Void, Void, String>() {
        lateinit var json_url: String
        override fun onPreExecute() {
            json_url = getText(R.string.metro_link).toString()
        }

        override fun doInBackground(vararg voids: Void): String {
            lateinit var url: URL
            var response = StringBuilder() // стринг не пересоздается, а изменяется
            try { // попытка создать юрл, хз почему нельзя просто взять и открыть
                url =
                    URL(json_url)
            } catch (e: MalformedURLException) {
                throw IllegalArgumentException("Invalid URL")
            }
            var conn: HttpURLConnection? = null
            try {
                conn = url.openConnection() as HttpURLConnection // открываем горе-ссылку
                conn.doOutput = false // вводить не можем
                conn.doInput = true // можем выводить
                conn.requestMethod = "GET" // get - получение ресурса

                val status = conn.responseCode // cостояние hhtp - 200 - ok
                if (status != 200) {
                    throw IOException("Post failed with error code $status")
                } else {
                    val instream = BufferedReader(InputStreamReader(conn.inputStream)) // открываем буферный поток ввода

                    var inputLine: String? = instream.readLine()
                    while ((inputLine) != null) { // читаем все строки
                        response.append(inputLine)
                        inputLine = instream.readLine()
                    }
                    instream.close()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                conn?.disconnect()
                val jsonString = response.toString().trim() // переводим в строку и удаляем ненужные пробелы
                Log.i(ContentValues.TAG, "Received JSON: $jsonString")
                return jsonString
            }
        }

        override fun onPostExecute(result: String) {
            super.onPostExecute(result)
            try {
                MainActivity.pla.getMetro(result)
            } catch (e: Exception) {
                Toast.makeText(context, print(e.message).toString(), Toast.LENGTH_LONG).show()
            }
            if (METRO_NEW.isNotEmpty()) {
                Toast.makeText(context, "МЕТРО ОК", Toast.LENGTH_LONG).show() //ok
                for(i in 0 until METRO_NEW.size) {
                    for(j in 0 until METRO_NEW[i].stations.size) {
                        STATIONS.add(METRO_NEW[i].stations[j])
                    }
                }
            }
        }
    }

        private fun isNetworkAvailable(context: Context): Boolean {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connectivityManager.activeNetworkInfo
            return networkInfo != null && networkInfo.isConnectedOrConnecting
        }
    }

