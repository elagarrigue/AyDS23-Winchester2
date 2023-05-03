package ayds.winchester.songinfo.moredetails.data.wikipedia.repository.local.wikipedia

import ayds.winchester.songinfo.moredetails.data.wikipedia.entity.Artist

interface WikipediaLocalStorage {

    fun insertArtist(query: String, artist: Artist)

    fun getArtistInfo(artist: String?): Artist?
}