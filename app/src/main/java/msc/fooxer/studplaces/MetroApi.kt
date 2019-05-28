package msc.fooxer.studplaces

import io.reactivex.Single
import retrofit2.http.GET

interface MetroApi {
    @GET("1")
    fun getStations(): Single<ArrayList<Line>>
}


class Station(val name : String, val lat : Float, val lng : Float)

class Line (val name : String, val color : String, val stations : ArrayList<Station>) {
}