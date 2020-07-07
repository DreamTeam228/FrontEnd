package msc.fooxer.studplaces

import android.content.Context
import android.net.ConnectivityManager
import android.os.AsyncTask
import android.util.Log
import android.widget.Toast
import okhttp3.*
import org.apache.http.NameValuePair
import org.apache.http.client.ClientProtocolException
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.BasicResponseHandler
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.message.BasicNameValuePair
import org.apache.http.protocol.HTTP
import java.io.IOException
import java.net.URL


class MainSearch_AsyncTask(private val context : Context, private val text : String)  : AsyncTask <Void, Void, String>() {

    override fun onPreExecute() {
        super.onPreExecute()
    }

    override fun doInBackground(vararg params: Void?): String {



        lateinit var url: URL
        var response = StringBuilder() // стринг не пересоздается, а изменяется

        // Отселе новый код с использованием OkHttp3 простирается
        //-------------------------------------------------------
        val client = OkHttpClient()

        lateinit var responsse: Response
            val formBody: RequestBody = FormBody.Builder()
                .add("search", text)
                .build()

            val request: Request  = Request.Builder()
                .url("http://www.trportal.ru/nekit/get_place_sql.php")
                .post(formBody)
                .build()



            try {
                val call: Call  = client.newCall(request);
                responsse = call.execute()

                // Do something with the response.
            } catch (e: IOException) {
                e.printStackTrace()
            }

        return responsse.body()!!.string()
        //--------------------------------------------------
        // Покуда здесь не заканчивается




    /* Отселе начинается код ныне почивший в летах и не работающий на гаджетах новомодных
        //val httpClient = DefaultHttpClient()
        // Creating HTTP Post
        val httpPost = HttpPost(
            "http://www.trportal.ru/nekit/search.php"
        )


        val httpclient = DefaultHttpClient()
        val http = HttpPost("http://www.trportal.ru/nekit/get_place_sql.php")
        val nameValuePairs = ArrayList<NameValuePair>(2)
        nameValuePairs.add(BasicNameValuePair("search", text))
        httpPost.setHeader("Content-Type", "application/json; charset=utf-8")
        http.entity = UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8)
        //получаем ответ от сервера


//        setEntity(UrlEncodedFormEntity(params, HTTP.UTF_8))

        // Making HTTP Request
        lateinit var s : String
        try {
            val responSse = httpclient.execute(http, BasicResponseHandler()) as String

//            val response = httpClient.execute(httpPost, BasicResponseHandler())

            // writing response to log
            Log.d("Http Response:", responSse.toString())
            s  = responSse.toString()
        } catch (e: ClientProtocolException) {
            // writing exception to log
            e.printStackTrace()
        } catch (e: IOException) {
            // writing exception to log
            e.printStackTrace()
        }
        return s
        // И доколе сей код продолжается
    */
    }




    override fun onPostExecute(result: String) {
        super.onPostExecute(result)
        try {
            MainActivity.RANDOM_WEEK = MainActivity.pla.getInfo(result)
            adapter.setData(MainActivity.RANDOM_WEEK)
        } catch (e: Exception) {
            Toast.makeText(context, print(e.message).toString(), Toast.LENGTH_LONG).show()
        }
        if(MainActivity.RANDOM_WEEK.isNotEmpty()) {
            for (i in 0 until MainActivity.RANDOM_WEEK.size)
                if (MainActivity.RANDOM_WEEK[i].id in MainActivity.FAV_INDEXES) MainActivity.RANDOM_WEEK[i].isFavorite =
                    true
        }
    }
}

private fun isNetworkAvailable(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkInfo = connectivityManager.activeNetworkInfo
    return networkInfo != null && networkInfo.isConnectedOrConnecting
}