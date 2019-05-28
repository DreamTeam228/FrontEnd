package msc.fooxer.studplaces

import com.google.gson.annotations.SerializedName
import io.reactivex.Single
import retrofit2.http.GET

interface MetroApi {
    @GET("1")
    fun getStations(): Single<Array<Lines>>
}


class Stations(val id : String, val name : String, val lat : String, val lng : String, val order : String)

class Lines (val id : String, val name : String, val hex_color : String, val stations : Array<Stations>)