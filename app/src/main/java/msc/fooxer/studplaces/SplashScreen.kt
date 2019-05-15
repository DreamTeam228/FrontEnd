package msc.fooxer.studplaces

import android.content.ContentValues
import android.content.Context
import android.content.Intent
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
    var dp: ArrayList<Place> = ArrayList()

    //var bp: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        AsynkJson(this).execute()
    }


    inner class AsynkJson(private val context: Context) : AsyncTask<Void, Void, String>() {
        var json_url: String = ""
        override fun onPreExecute() {
            json_url = "http://trportal.ru/nekit/get_places.php"
            if (!isNetworkAvailable(context)) {
                Toast.makeText(context, "The connection is lost", Toast.LENGTH_LONG)
                    .show() //вывод сообщения о соединении с интернетом
            } else Toast.makeText(context, "The connection is ok", Toast.LENGTH_LONG).show()
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


        private fun isNetworkAvailable(context: Context): Boolean {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connectivityManager.activeNetworkInfo
            return networkInfo != null && networkInfo.isConnectedOrConnecting
        }
    }
}