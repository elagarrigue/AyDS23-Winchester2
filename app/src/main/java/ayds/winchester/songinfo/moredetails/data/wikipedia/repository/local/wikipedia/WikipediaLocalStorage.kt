package ayds.winchester.songinfo.moredetails.data.wikipedia.repository.local.wikipedia

import ayds.winchester.songinfo.moredetails.domain.entity.Card.ArtistCard

interface WikipediaLocalStorage {

    fun insertInfo(artistName: String, artist: ArtistCard)

    fun getInfo(artist: String?): ArtistCard?
}
