package ayds.winchester.songinfo.moredetails.data.wikipedia.repository.broker

import ayds.winchester.songinfo.moredetails.domain.entity.Card

interface Broker {
    fun getCard(artistName: String): Card.ArtistCard?
}

internal class ArtistCardBroker(private val wikipediaProxy: WikipediaProxy): Broker {
    override fun getCard(artistName: String) =
        wikipediaProxy.getCard(artistName)

}