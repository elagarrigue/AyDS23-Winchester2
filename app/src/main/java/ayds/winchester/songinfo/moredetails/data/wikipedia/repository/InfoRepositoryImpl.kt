package ayds.winchester.songinfo.moredetails.data.wikipedia.repository

import ayds.winchester.songinfo.moredetails.domain.entity.Info
import ayds.winchester.songinfo.moredetails.domain.entity.Info.EmptyInfo
import ayds.winchester.songinfo.moredetails.domain.entity.Info.ArtistInfo
import ayds.winchester.songinfo.moredetails.data.wikipedia.repository.external.wikipedia.WikipediaTrackService
import ayds.winchester.songinfo.moredetails.data.wikipedia.repository.local.wikipedia.WikipediaLocalStorage
import ayds.winchester.songinfo.moredetails.domain.repository.InfoRepository
import ayds.winchester.songinfo.moredetails.presentation.InfoDescriptionHelper


internal class InfoRepositoryImpl(
    private val wikipediaLocalStorage: WikipediaLocalStorage,
    private val wikipediaTrackService: WikipediaTrackService,
    private val infoDescriptionHelper: InfoDescriptionHelper
) : InfoRepository {

    override fun getInfo(artist: String): Info {
        var artistInfo = wikipediaLocalStorage.getInfo(artist)

        when {
            artistInfo != null -> markInfoAsLocal(artistInfo,artist)
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

    private fun markInfoAsLocal(info: ArtistInfo,artist: String) {
        info.isLocallyStored = true
        info.description = infoDescriptionHelper.getInfoDescriptionText(info, artist)
    }
}