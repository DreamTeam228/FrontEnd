package msc.fooxer.studplaces

import android.content.ContentValues
import android.content.Context
import android.content.Intent
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

class SplashScreen : AppCompatActivity() {
    var dp : ArrayList<Place> = ArrayList()

    //var bp: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        MainActivity.dbh = DBHelper(this)
        MainActivity.db = MainActivity.dbh.writableDatabase
        fillingFromTable(FAV_TABLE_NAME)
        if (!isNetworkAvailable(this)) {
            Toast.makeText(this, "The connection is lost", Toast.LENGTH_LONG).show() //вывод сообщения о соединении с интернетом
            fillingFromTable(CASH_TABLE_NAME)

            Toast.makeText(this, "Cashe data is downloaded", Toast.LENGTH_LONG).show()
            val i = Intent(baseContext, MainActivity::class.java)
            i.putParcelableArrayListExtra("dp_ELEMENTS", dp)
            startActivity(i)
            finish()
        } else
            //Toast.makeText(this, "The connection is ok", Toast.LENGTH_LONG).show()

            AsynkJson(this).execute()

    }


    inner class AsynkJson(private val context: Context): AsyncTask<Void, Void, String>()
    {
        var json_url: String = ""
        override fun onPreExecute() {
            json_url = "http://trportal.ru/nekit/get_places.php"

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
            if(dp.isNotEmpty())
                Toast.makeText(context, "Data is downloaded", Toast.LENGTH_LONG).show() //ok

            val i = Intent(baseContext, MainActivity::class.java)
            i.putParcelableArrayListExtra("dp_ELEMENTS", dp)
            startActivity(i)
            finish()
        }

    }
    fun fillingFromTable(table: String) {
        val cursor: Cursor = MainActivity.db.query(table,null,null,null,null,null,null)
        if(cursor.moveToFirst()) {
            val idIndex = cursor.getColumnIndex(KEY_INDEX)
            val nameIndex = cursor.getColumnIndex(KEY_NAME)
            val descIndex = cursor.getColumnIndex(KEY_DESCR)
            val priceIndex = cursor.getColumnIndex(KEY_PRICE)
            val phoneIndex = cursor.getColumnIndex(KEY_PHONE)
            val addressIndex = cursor.getColumnIndex(KEY_ADDRESS)
            val metroIndex = cursor.getColumnIndex(KEY_METRO)
            val catIndex = cursor.getColumnIndex(KEY_CATEGORY)
            val picIndex = cursor.getColumnIndex(KEY_PIC)
            val favIndex = cursor.getColumnIndex(KEY_FAV)
            if (table == FAV_TABLE_NAME) do {
                val place = Place(cursor.getInt(idIndex), cursor.getString(nameIndex), cursor.getString(catIndex), cursor.getString(descIndex),
                    cursor.getString(metroIndex),cursor.getString(phoneIndex),cursor.getInt(priceIndex), cursor.getString(addressIndex),
                    cursor.getString(picIndex), true)
                MainActivity.FAVORITES.add(place)
                MainActivity.FAV_INDEXES.add(cursor.getInt(idIndex))
            } while (cursor.moveToNext())
            else do {
                val place = Place(cursor.getInt(idIndex), cursor.getString(nameIndex), cursor.getString(catIndex), cursor.getString(descIndex),
                    cursor.getString(metroIndex),cursor.getString(phoneIndex),cursor.getInt(priceIndex), cursor.getString(addressIndex),
                    cursor.getString(picIndex), cursor.getInt(favIndex) == 1)
                dp.add(place)
            } while (cursor.moveToNext())
        }
        cursor.close()
    }

}

private fun isNetworkAvailable(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkInfo = connectivityManager.activeNetworkInfo
    return networkInfo != null && networkInfo.isConnectedOrConnecting
}

