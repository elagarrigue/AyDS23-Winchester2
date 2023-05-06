package ayds.winchester.songinfo.moredetails.data.wikipedia.repository.external.wikipedia

import ayds.winchester.songinfo.moredetails.data.wikipedia.entity.Info.ArtistInfo

interface WikipediaTrackService {
    fun getInfo(artistName: String): ArtistInfo?
}