package ayds.winchester.songinfo.moredetails.data.wikipedia.repository.local.wikipedia

import ayds.winchester.songinfo.moredetails.domain.entity.Card

interface CardLocalStorage {

    fun insertCard(artistName: String, artist: Card)

    fun getCards(artist: String?): List<Card>
}
