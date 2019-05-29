package msc.fooxer.studplaces

import com.google.gson.annotations.SerializedName
import io.reactivex.Single
import retrofit2.http.GET

interface MetroApi {
    @GET("1")
    fun getStations(): Single<Array<Line>>
}

class Station(val id : Double, val name : String, val lat : Double, val lng : Double, val order: Int)


class Line (val id : Int, val name : String, val hex_color : String, val stations : ArrayList<Station>)