package ayds.winchester.songinfo.moredetails.domain.repository

import ayds.winchester.songinfo.moredetails.domain.entity.Card

interface WikipediaRepository {
    fun getInfo(artist: String): Card
}