package ayds.winchester.songinfo.moredetails.data.wikipedia.repository

import ayds.winchester.songinfo.moredetails.data.wikipedia.repository.broker.CardsBroker
import ayds.winchester.songinfo.moredetails.domain.entity.Card
import ayds.winchester.songinfo.moredetails.data.wikipedia.repository.local.wikipedia.CardLocalStorage
import ayds.winchester.songinfo.moredetails.domain.repository.CardRepository

internal class CardRepositoryImpl(
    private val cardLocalStorage: CardLocalStorage,
    private val cardsBroker: CardsBroker,
) : CardRepository {

    override fun getCards(artist: String): List<Card> {
        var artistCards:List<Card> = cardLocalStorage.getCards(artist)
        when {
            artistCards.isNotEmpty() -> markInfoAsLocal(artistCards)
            else -> {
                try {
                    artistCards = cardsBroker.getCards(artist)
                    for(artistCard in artistCards)
                        cardLocalStorage.insertCard(artist, artistCard)
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