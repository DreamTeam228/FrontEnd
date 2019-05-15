package msc.fooxer.studplaces

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import java.net.HttpURLConnection
import java.net.URL
import java.util.ArrayList

class SplashScreen : AppCompatActivity() {
    var dp : ArrayList<Place> = ArrayList()

    //var bp: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        AsynkJson(this).execute()
    }


    inner class AsynkJson(private val context: Context): AsyncTask<Void, Void, String>()
    {
        var json_url: String = ""
        override fun onPreExecute() {
            json_url = "http://trportal.ru/nekit/get_places.php"
            if (context != null && !isNetworkAvailable(context)) {
                Toast.makeText(context, "The connection is lost", Toast.LENGTH_LONG).show() //вывод сообщения о соединении с интернетом
            } else Toast.makeText(context, "The connection is ok", Toast.LENGTH_LONG).show()
        }

        override fun doInBackground(vararg voids: Void): String {
            var jsonstring: String
            val url = URL(json_url)
            val httpURLConnection = url.openConnection() as HttpURLConnection
            try
            {
                httpURLConnection.connect()     // Здесь происходит ошибка в эмуляторе
                jsonstring = httpURLConnection.inputStream.use{it.reader().use{bufferedReader-> bufferedReader.readText()}}

            } finally {
                httpURLConnection.disconnect()
            }
            return jsonstring

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
}

private fun isNetworkAvailable(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkInfo = connectivityManager.activeNetworkInfo
    return networkInfo != null && networkInfo.isConnectedOrConnecting
}
