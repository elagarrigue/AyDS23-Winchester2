package ayds.winchester.songinfo.moredetails.domain.repository

import ayds.winchester.songinfo.moredetails.domain.entity.Info

interface WikipediaRepository {
    fun getInfo(artist: String): Info
}