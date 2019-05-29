package msc.fooxer.studplaces

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.AsyncTask
import android.support.v4.content.ContextCompat.startActivity
import android.util.Log
import android.widget.Toast
import org.apache.http.NameValuePair
import org.apache.http.client.ClientProtocolException
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.BasicResponseHandler
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.message.BasicNameValuePair
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.UnsupportedEncodingException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import android.os.AsyncTask.execute
import org.apache.http.protocol.HTTP
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


class Search_AsyncTask(private val context : Context)  : AsyncTask <Void, Void, String>() {

    override fun onPreExecute() {
        super.onPreExecute()
        if(!isNetworkAvailable(context)) {
            Toast.makeText(context, "The connection is lost", Toast.LENGTH_LONG).show() //вывод сообщения о соединении с интернетом
        } else Toast.makeText(context, "The connection is ok", Toast.LENGTH_LONG).show()
    }

    override fun doInBackground(vararg params: Void?): String {
        lateinit var url: URL
        var response = StringBuilder() // стринг не пересоздается, а изменяется

        val httpClient = DefaultHttpClient()
        // Creating HTTP Post
        val httpPost = HttpPost(
            "http://trportal.ru/nekit/search.php"
        )

        // Building post parameters
        // key and value pair
        //val nameValuePair = ArrayList<NameValuePair>(2)
        //nameValuePair.add(BasicNameValuePair("metro", "Марьино"))
        //nameValuePair.add(BasicNameValuePair("price", "101"))

        // Url Encoding the POST parameters
        /*try {
            httpPost.entity = UrlEncodedFormEntity(nameValuePair)
        } catch (e: UnsupportedEncodingException) {
            // writing error to Log
            e.printStackTrace()
        }*/


        val httpclient = DefaultHttpClient()
        val http = HttpPost("http://www.trportal.ru/nekit/search.php")
        val nameValuePairs = ArrayList<NameValuePair>(2)

        val jsonMetro  = JSONArray()
        for (i in 0 until checkedMetro.size) {
            jsonMetro.put(checkedMetro[i])
        }

        val jsonCategory = JSONArray()
        for (i in 0 until checkedCategory.size) {
            jsonMetro.put(checkedCategory[i])
        }

        nameValuePairs.add(BasicNameValuePair("metro",jsonMetro.toString()))
        nameValuePairs.add(BasicNameValuePair("category", jsonCategory.toString()))
        nameValuePairs.add(BasicNameValuePair("price","1000" ))
        httpPost.setHeader("Content-Type", "application/json; charset=utf-8")
        http.entity = UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8)
        //получаем ответ от сервера


//        setEntity(UrlEncodedFormEntity(params, HTTP.UTF_8))

        // Making HTTP Request
        try {
            val responSse = httpclient.execute(http, BasicResponseHandler()) as String
            httpPost
//            val response = httpClient.execute(httpPost, BasicResponseHandler())

            // writing response to log
            Log.d("Http Response:", responSse.toString())
            var s : String = responSse.toString()
        } catch (e: ClientProtocolException) {
            // writing exception to log
            e.printStackTrace()
        } catch (e: IOException) {
            // writing exception to log
            e.printStackTrace()

        }

        try { // попытка создать юрл, хз почему нельзя просто взять и открыть
            url =
                URL("http://trportal.ru/nekit/search.php")
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
        if(MainActivity.RANDOM_WEEK.isNotEmpty())
            Toast.makeText(context, "Data is downloaded", Toast.LENGTH_LONG).show() //ok
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