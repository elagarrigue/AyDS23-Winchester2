package ayds.winchester.songinfo.moredetails.data.wikipedia.repository

import ayds.winchester2.wikipediaexternal.data.wikipedia.entity.Info
import ayds.winchester2.wikipediaexternal.data.wikipedia.entity.Info.EmptyInfo
import ayds.winchester2.wikipediaexternal.data.wikipedia.entity.Info.ArtistInfo
import ayds.winchester2.wikipediaexternal.data.wikipedia.WikipediaTrackService
import ayds.winchester.songinfo.moredetails.data.wikipedia.repository.local.wikipedia.WikipediaLocalStorage
import ayds.winchester.songinfo.moredetails.domain.repository.WikipediaRepository

internal class WikipediaRepositoryImpl(
    private val wikipediaLocalStorage: WikipediaLocalStorage,
    private val wikipediaTrackService: WikipediaTrackService,
) : WikipediaRepository {

    override fun getInfo(artist: String): Info {
        var artistInfo = wikipediaLocalStorage.getInfo(artist)

        when {
            artistInfo != null -> markInfoAsLocal(artistInfo)
            else -> {
                try {
                    artistInfo = wikipediaTrackService.getInfo(artist)

                    artistInfo?.let {
                        wikipediaLocalStorage.insertInfo(artist,it)
                    }
                } catch (e: Exception) {
                    artistInfo = null
                }
            }
        }

        return artistInfo ?: EmptyInfo
    }

    private fun markInfoAsLocal(info: ArtistInfo) {
        info.isLocallyStored = true
    }
}