package ayds.winchester.songinfo.moredetails.data.wikipedia.repository

import ayds.winchester.songinfo.moredetails.data.wikipedia.repository.broker.Broker
import ayds.winchester.songinfo.moredetails.domain.entity.Card
import ayds.winchester.songinfo.moredetails.domain.entity.Card.EmptyCard
import ayds.winchester.songinfo.moredetails.domain.entity.Card.ArtistCard
import ayds.winchester.songinfo.moredetails.data.wikipedia.repository.local.wikipedia.WikipediaLocalStorage
import ayds.winchester.songinfo.moredetails.domain.repository.WikipediaRepository

internal class WikipediaRepositoryImpl(
    private val wikipediaLocalStorage: WikipediaLocalStorage,
    private val artistCardBroker: Broker,
) : WikipediaRepository {

    override fun getInfo(artist: String): Card {
        var artistCard = wikipediaLocalStorage.getInfo(artist)

        when {
            artistCard != null -> markInfoAsLocal(artistCard)
            else -> {
                try {
                    artistCard = artistCardBroker.getCard(artist)

                    artistCard?.let {
                        wikipediaLocalStorage.insertInfo(artist,it)
                    }
                } catch (e: Exception) {
                    artistCard = null
                }
            }
        }

        return artistCard ?: EmptyCard
    }

    private fun markInfoAsLocal(info: ArtistCard) {
        info.isLocallyStored = true
    }
}