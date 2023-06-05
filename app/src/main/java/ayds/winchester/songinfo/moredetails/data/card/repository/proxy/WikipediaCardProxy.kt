package ayds.winchester.songinfo.moredetails.data.card.repository.proxy

import ayds.winchester.songinfo.moredetails.domain.entity.Card
import ayds.winchester.songinfo.moredetails.domain.entity.Source
import ayds.winchester2.wikipediaexternal.data.wikipedia.WikipediaTrackService
import ayds.winchester2.wikipediaexternal.data.wikipedia.entity.ArtistInfo

internal class WikipediaCardProxy(private val wikipediaTrackService: WikipediaTrackService): CardProxy {
    override fun getCard(artistName: String): Card? {
        val card: Card? =
            try{
                val wikipediaTrackService = wikipediaTrackService.getInfo(artistName)
                wikipediaTrackService?.map()
            }
            catch(e: Exception){null}
        return card
    }
    private fun ArtistInfo.map() =
        Card(
            description = this.description,
            infoURL = this.wikipediaURL,
            source = Source.Wikipedia,
            sourceLogoUrl = this.wikipediaLogoURL
        )
}
