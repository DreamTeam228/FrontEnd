package msc.fooxer.studplaces

import android.os.AsyncTask
import android.provider.ContactsContract
import android.provider.SyncStateContract
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
            var discount : Int
            var url = ""
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
                discount = Jsonchik.getInt("discount")
                url = Jsonchik.getString("url")
                count++
                if (!url.startsWith("http://") && !url.startsWith("https://"))
                    url= "http://$url"
                val place = Place(id, name, category, description, metro, phoneNumbers, price, place1, picture, discount, url)
                place.isFavorite = place.id in MainActivity.FAV_INDEXES
                places.add(place)
            }

        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return places
    }
   fun getMetro(Json_string: String) {
        try {
            var mJsonObj = JSONObject(Json_string)
            var lines = mJsonObj.getJSONArray("lines")
            var count = 0
            // Присвоение полям объекта класса

            for (i in 0 until lines.length()) {
                val Jsonchik = lines.getJSONObject(count)
                val id = Jsonchik.getInt("id")
                val color = Jsonchik.getString("hex_color")
                val name = Jsonchik.getString("name")
                var lineStations = ArrayList<Station>()
                var stations = Jsonchik.optJSONArray("stations")
                if (stations!=null) {
                  for (j in 0 until stations.length()) {
                      val station = stations.getJSONObject(j)
                      val sId = station.getDouble("id")
                      val sName = station.getString("name")
                      val lat = station.getDouble("lat")
                      val lng = station.getDouble("lng")
                      val order = station.getInt("order")
                      val st = Station (sId,sName,lat,lng,order)
                      lineStations.add(st)
                  }
                               }
              val line = Line(id,name,color,lineStations)
              METRO_NEW.add(line)
              count++

            }

        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }
}
