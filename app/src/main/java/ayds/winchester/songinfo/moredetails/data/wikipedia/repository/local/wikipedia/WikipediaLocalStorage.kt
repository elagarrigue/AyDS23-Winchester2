package ayds.winchester.songinfo.moredetails.data.wikipedia.repository.local.wikipedia

import ayds.winchester.songinfo.moredetails.data.wikipedia.entity.Info.ArtistInfo

interface WikipediaLocalStorage {

    fun insertInfo(artistName: String, artist: ArtistInfo)

    fun getInfo(artist: String?): ArtistInfo?
}