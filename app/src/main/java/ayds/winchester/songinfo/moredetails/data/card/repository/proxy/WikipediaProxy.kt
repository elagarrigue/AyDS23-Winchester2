package ayds.winchester.songinfo.moredetails.data.card.repository.proxy

import ayds.winchester.songinfo.moredetails.domain.entity.Card
import ayds.winchester.songinfo.moredetails.domain.entity.Source
import ayds.winchester2.wikipediaexternal.data.wikipedia.WikipediaTrackService
import ayds.winchester2.wikipediaexternal.data.wikipedia.entity.ArtistInfo

internal class WikipediaProxy(private val wikipediaTrackService: WikipediaTrackService): Proxy {
    override fun getCard(artistName: String) =
        wikipediaTrackService.getInfo(artistName)?.map()

    private fun ArtistInfo.map() =
        Card(
            description = this.description,
            infoURL = this.wikipediaURL,
            source = Source.Wikipedia,
            sourceLogoUrl = this.wikipediaLogoURL
        )
}
