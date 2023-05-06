package ayds.winchester.songinfo.moredetails.data.wikipedia.repository.external.wikipedia.tracks

import ayds.winchester.songinfo.moredetails.data.wikipedia.entity.Info.ArtistInfo
import ayds.winchester.songinfo.moredetails.data.wikipedia.repository.external.wikipedia.WikipediaTrackService
import retrofit2.Response

internal class WikipediaTrackServiceImpl(
    private val wikipediaTrackAPI: WikipediaTrackAPI,
    private val wikipediaToInfoResolver: WikipediaToInfoResolver,
) : WikipediaTrackService {

    override fun getInfo(artistName: String): ArtistInfo? {
        val callResponse = getInfoFromService(artistName)
        return wikipediaToInfoResolver.getInfoFromExternalData(callResponse.body())
    }

    private fun getInfoFromService(artistName: String): Response<String> {
        return wikipediaTrackAPI.getArtistInfo(artistName)
            .execute()
    }
}