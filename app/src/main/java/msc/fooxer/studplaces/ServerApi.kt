package msc.fooxer.studplaces

import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ServerApi {
    @GET("")
    fun places(): Single<List<Place>>

    @POST("search.php")
    fun createRequest(@Body request : Array<String>): Call<MutableList<Place>>
}