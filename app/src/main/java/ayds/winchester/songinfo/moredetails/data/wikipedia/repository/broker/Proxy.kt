package ayds.winchester.songinfo.moredetails.data.wikipedia.repository.broker

import ayds.winchester.songinfo.moredetails.domain.entity.Card
import ayds.winchester.songinfo.moredetails.domain.entity.Card.ArtistCard
import ayds.winchester.songinfo.moredetails.domain.entity.Source
import ayds.winchester2.wikipediaexternal.data.wikipedia.WikipediaTrackService

interface Proxy {
    fun getCard(artistName: String): Card?
}
internal class WikipediaProxy(private val wikipediaTrackService: WikipediaTrackService): Proxy{
    override fun getCard(artistName: String) =
        wikipediaTrackService.getInfo(artistName)?.let {
            ArtistCard(
                description = it.description,
                infoURL = it.wikipediaURL,
                source = Source.Wikipedia,
                sourceLogoUrl = "https://upload.wikimedia.org/wikipedia/commons/8/8c/Wikipedia-logo-v2-es.png"
            )
        }
}