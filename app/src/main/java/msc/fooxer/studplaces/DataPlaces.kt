package msc.fooxer.studplaces

import android.os.AsyncTask
import android.provider.ContactsContract
import android.widget.TextView
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.ArrayList

class DataPlaces {
    val places = ArrayList<Place>()
    var jsonobject: JSONObject = JSONObject()
    var jsonArray: JSONArray = JSONArray()


    fun getInfo(Json_string: String): ArrayList<Place> {
        try {
            jsonobject = JSONObject(Json_string)
            jsonArray = jsonobject.getJSONArray("places")
            var count = 0
            var id : Int
            var name = ""
            var category = ""
            var description = ""
            var metro = ""
            var phoneNumbers = ""
            var price : Int
            var place1 = ""
            var picture = ""
            // Присвоение полям объекта класса

            places.clear()
            while (count < jsonArray.length()) {
                val Jsonchik = jsonArray.getJSONObject(count)
                id = Jsonchik.getInt("id")
                name = Jsonchik.getString("name")
                category = Jsonchik.getString("category")
                description = Jsonchik.getString("description")
                metro = Jsonchik.getString("metro")
                phoneNumbers = Jsonchik.getString("phoneNumbers")
                place1 = Jsonchik.getString("place")
                price = Jsonchik.getInt("price")
                picture = Jsonchik.getString("picture")
                count++
                val place = Place(id, name, category, description, metro, phoneNumbers, price, place1, picture)
                place.isFavorite = place.id in MainActivity.FAV_INDEXES
                places.add(place)
            }

        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return places
    }
}
