package msc.fooxer.studplaces

import io.reactivex.Single
import retrofit2.http.GET

interface MetroApi {
    @GET("1")
    fun getStations(): Single<ArrayList<Line>>
}


class Station(val name : String, val lat : Double, val lng : Double, val order: Int)

class Line (val name : String, val color : String, val stations : ArrayList<Station>) {
}