package msc.fooxer.studplaces

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.POST

interface ServerApi {
    @GET("")
    fun places(): Single<List<Place>>

    @POST("search.php")
    fun createRequest(request : List<String>): Single<List<Place>>
}