package ayds.winchester.songinfo.moredetails.data.wikipedia.repository.local.wikipedia

import ayds.winchester.songinfo.moredetails.domain.entity.Card

interface WikipediaLocalStorage {

    fun insertInfo(artistName: String, artist: Card)

    fun getInfo(artist: String?): List<Card>
}
