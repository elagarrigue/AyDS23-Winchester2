package ayds.winchester.songinfo.moredetails.data.wikipedia.repository.external.wikipedia.tracks

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


internal interface WikipediaTrackAPI {
    @GET("api.php?action=query&list=search&utf8=&format=json&srlimit=1")
    fun getArtistInfo(@Query("srsearch") artist: String): Call<String>
}