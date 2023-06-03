package ayds.winchester.songinfo.moredetails.data.wikipedia.repository

import ayds.winchester.songinfo.moredetails.data.wikipedia.repository.broker.CardsBroker
import ayds.winchester.songinfo.moredetails.domain.entity.Card
import ayds.winchester.songinfo.moredetails.data.wikipedia.repository.local.wikipedia.WikipediaLocalStorage
import ayds.winchester.songinfo.moredetails.domain.repository.CardRepository

internal class CardRepositoryImpl(
    private val wikipediaLocalStorage: WikipediaLocalStorage,
    private val cardsBroker: CardsBroker,
) : CardRepository {

    override fun getCards(artist: String): List<Card> {
        var artistCards:List<Card> = wikipediaLocalStorage.getInfo(artist)
        when {
            artistCards.isNotEmpty() -> markInfoAsLocal(artistCards)
            else -> {
                try {
                    artistCards = cardsBroker.getCards(artist)
                    for(artistCard in artistCards)
                        wikipediaLocalStorage.insertInfo(artist, artistCard)
                } catch (e: Exception) {}
            }
        }
        return artistCards
    }

    private fun markInfoAsLocal(artists: List<Card>) {
        for(artist in artists)
            artist.isLocallyStored = true
    }
}