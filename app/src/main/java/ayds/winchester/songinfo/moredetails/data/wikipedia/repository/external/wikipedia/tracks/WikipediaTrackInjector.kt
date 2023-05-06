package ayds.winchester.songinfo.moredetails.data.wikipedia.repository.external.wikipedia.tracks

import ayds.winchester.songinfo.moredetails.data.wikipedia.repository.external.wikipedia.WikipediaTrackService
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

object WikipediaTrackInjector {
    private const val WIKIPEDIA_URL = "https://en.wikipedia.org/w/"
    private val wikipediaAPIRetrofit = Retrofit.Builder()
        .baseUrl(WIKIPEDIA_URL)
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()
    private val wikipediaTrackAPI = wikipediaAPIRetrofit.create(WikipediaTrackAPI::class.java)
    private val wikipediaToInfoResolver: WikipediaToInfoResolver = JsonToInfoResolver()

    val wikipediaTrackService: WikipediaTrackService = WikipediaTrackServiceImpl(
        WikipediaTrackInjector.wikipediaTrackAPI,
        WikipediaTrackInjector.wikipediaToInfoResolver
    )
}