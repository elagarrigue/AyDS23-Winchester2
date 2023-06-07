package ayds.winchester.songinfo.moredetails.domain.repository

import ayds.winchester.songinfo.moredetails.domain.entity.Card

interface CardRepository {
    fun getCards(artist: String): List<Card>
}