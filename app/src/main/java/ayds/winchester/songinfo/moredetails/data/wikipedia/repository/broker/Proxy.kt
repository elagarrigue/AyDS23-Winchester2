package ayds.winchester.songinfo.moredetails.data.wikipedia.repository.broker

import ayds.winchester.songinfo.moredetails.domain.entity.Card
import ayds.winchester.songinfo.moredetails.domain.entity.Card.ArtistCard
import ayds.winchester.songinfo.moredetails.domain.entity.Source
import ayds.winchester2.wikipediaexternal.data.wikipedia.WikipediaTrackService
import ayds.winchester2.wikipediaexternal.data.wikipedia.entity.ArtistInfo

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