package msc.fooxer.studplaces

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.AsyncTask
import android.support.v4.content.ContextCompat.startActivity
import android.util.Log
import android.widget.Toast
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class Random_AsyncTask(private val context : Context)  : AsyncTask <Void, Void, String>() {

    override fun onPreExecute() {
        super.onPreExecute()
        if(!isNetworkAvailable(context)) {
            Toast.makeText(context, R.string.connection_lost, Toast.LENGTH_LONG).show() //вывод сообщения о соединении с интернетом
        }
    }

    override fun doInBackground(vararg params: Void?): String {
        lateinit var url: URL
        var response = StringBuilder() // стринг не пересоздается, а изменяется
        try { // попытка создать юрл, хз почему нельзя просто взять и открыть
            url =
                URL("http://trportal.ru/nekit/random_places.php")
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
            MainActivity.RANDOM_WEEK = MainActivity.pla.getInfo(result)
        } catch (e: Exception) {
            Toast.makeText(context, print(e.message).toString(), Toast.LENGTH_LONG).show()
        }
        for (i in 0 until MainActivity.RANDOM_WEEK.size)
            if (MainActivity.RANDOM_WEEK[i].id in MainActivity.FAV_INDEXES) MainActivity.RANDOM_WEEK[i].isFavorite = true

        val i = Intent(context, Random::class.java)
        startActivity(context, i, null)
    }
}

private fun isNetworkAvailable(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkInfo = connectivityManager.activeNetworkInfo
    return networkInfo != null && networkInfo.isConnectedOrConnecting
}