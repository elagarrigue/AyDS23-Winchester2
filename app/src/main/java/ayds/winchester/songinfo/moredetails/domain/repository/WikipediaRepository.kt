package ayds.winchester.songinfo.moredetails.domain.repository

import ayds.winchester2.wikipediaexternal.data.wikipedia.entity.Info

interface WikipediaRepository {
    fun getInfo(artist: String): Info
}