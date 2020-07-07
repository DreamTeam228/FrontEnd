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
import okhttp3.*
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
            Toast.makeText(context, R.string.connection_lost, Toast.LENGTH_LONG).show() //вывод сообщения о соединении с интернетом
        }
    }

    override fun doInBackground(vararg params: Void?): String {
        lateinit var url: URL
        var response = StringBuilder() // стринг не пересоздается, а изменяется

        //val httpClient = DefaultHttpClient()
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


        // Тут начинается новый код с OkHttp3, в перспективе с переездом на Ретрофит
        //--------------------------------------------------------------------------
        val client = OkHttpClient()

        val jsonMetro  = JSONArray()
        for (i in 0 until checkedMetro.size) {
            jsonMetro.put(checkedMetro[i])
        }

        val jsonCategory = JSONArray()
        for (i in 0 until checkedCategory.size) {
            jsonCategory.put(checkedCategory[i])
        }

        lateinit var responsse: Response
        val formBody: RequestBody = FormBody.Builder()
            .add("metro", jsonMetro.toString())
            .add("category", jsonCategory.toString())
            .add("price", "$maxPrice")
            .build()

        val request: Request = Request.Builder()
            .url("http://www.trportal.ru/nekit/search.php")
            .post(formBody)
            .build()



        try {
            val call: Call = client.newCall(request);
            responsse = call.execute()

            // Do something with the response.
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return responsse.body()!!.string()
        //-------------------------------------------------------
        // Конец нового кода



        /* Здесь был старый код с использованием DefaultHttpClient
        val httpclient = DefaultHttpClient()
        val http = HttpPost("http://www.trportal.ru/nekit/search.php")
        val nameValuePairs = ArrayList<NameValuePair>(2)

        val jsonMetro  = JSONArray()
        for (i in 0 until checkedMetro.size) {
            jsonMetro.put(checkedMetro[i])
        }

        val jsonCategory = JSONArray()
        for (i in 0 until checkedCategory.size) {
            jsonCategory.put(checkedCategory[i])
        }

        nameValuePairs.add(BasicNameValuePair("metro",jsonMetro.toString()))
        nameValuePairs.add(BasicNameValuePair("category", jsonCategory.toString()))
        nameValuePairs.add(BasicNameValuePair("price", "$maxPrice" ))
        httpPost.setHeader("Content-Type", "application/json; charset=utf-8")
        http.entity = UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8)
        //получаем ответ от сервера


//        setEntity(UrlEncodedFormEntity(params, HTTP.UTF_8))
        lateinit var s: String
        // Making HTTP Request
        try {
            val responSse = httpclient.execute(http, BasicResponseHandler()) as String
//            val response = httpClient.execute(httpPost, BasicResponseHandler())

            // writing response to log
            Log.d("Http Response:", responSse.toString())
             s = responSse
        } catch (e: ClientProtocolException) {
            // writing exception to log
            e.printStackTrace()
        } catch (e: IOException) {
            // writing exception to log
            e.printStackTrace()

        }
        return s
        // Конец старого кода
         */
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