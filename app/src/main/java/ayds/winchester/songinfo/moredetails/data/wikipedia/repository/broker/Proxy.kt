package ayds.winchester.songinfo.moredetails.data.wikipedia.repository.broker

import ayds.aknewyork.external.service.data.NYTimesService
import ayds.aknewyork.external.service.data.entities.ArtistDataExternal
import ayds.aknewyork.external.service.data.entities.ArtistDataExternal.ArtistWithDataExternal
import ayds.winchester.songinfo.moredetails.domain.entity.Card
import ayds.winchester.songinfo.moredetails.domain.entity.Card.ArtistCard
import ayds.winchester.songinfo.moredetails.domain.entity.Source
import ayds.winchester2.wikipediaexternal.data.wikipedia.WikipediaTrackService
import ayds.winchester2.wikipediaexternal.data.wikipedia.entity.ArtistInfo
import lisboa4LastFM.LastFMService
import lisboa4LastFM.ArtistBiography
import lisboa4LastFM.ArtistBiography.Companion.URL_LAST_FM_IMAGE

interface Proxy {
    fun getCard(artistName: String): Card?
}
internal class WikipediaProxy(private val wikipediaTrackService: WikipediaTrackService): Proxy {
    override fun getCard(artistName: String) =
        wikipediaTrackService.getInfo(artistName)?.map()

    private fun ArtistInfo.map() =
        ArtistCard(
            description = this.description,
            infoURL = this.wikipediaURL,
            source = Source.Wikipedia,
            sourceLogoUrl = this.wikipediaLogoURL
        )
}

internal class NYTimesProxy(private val nyTimesService: NYTimesService): Proxy{
    override fun getCard(artistName: String) = nyTimesService.getArtistInfo(artistName).map()

    private fun ArtistDataExternal.map() =
        when(this){
            is ArtistWithDataExternal -> ArtistCard(
                description = this.info?:"",
                infoURL = this.url,
                source = Source.NYTimes,
                sourceLogoUrl = URL_LAST_FM_IMAGE//Fijarse aca porque esto deberia ser el logo de ellos, si es que existiera :)
            )
            else -> null
        }

}

internal class LastFMProxy(private val lastFMService: LastFMService): Proxy{
    override fun getCard(artistName: String) = lastFMService.getArtistBiography(artistName)?.map()

    private fun ArtistBiography.map() =
        ArtistCard(
            description = this.artistInfo,
            infoURL = this.url,
            source = Source.LastFM,
            sourceLogoUrl = URL_LAST_FM_IMAGE
        )

}