package ayds.winchester.songinfo.moredetails.data.wikipedia.repository.external.wikipedia

import ayds.winchester.songinfo.moredetails.domain.entity.Info.ArtistInfo

interface WikipediaTrackService {
    fun getInfo(artistName: String): ArtistInfo?
}