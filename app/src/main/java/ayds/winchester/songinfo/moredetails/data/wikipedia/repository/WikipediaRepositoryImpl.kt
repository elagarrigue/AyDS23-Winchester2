package ayds.winchester.songinfo.moredetails.data.wikipedia.repository

import ayds.winchester.songinfo.moredetails.domain.entity.Info
import ayds.winchester.songinfo.moredetails.domain.entity.Info.EmptyInfo
import ayds.winchester.songinfo.moredetails.domain.entity.Info.ArtistInfo
import ayds.winchester2.wikipediadata.data.wikipedia.WikipediaTrackService
import ayds.winchester2.wikipediadata.data.wikipedia.entity.ArtistInfo as ArtistInfoService
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
                    val serviceArtistInfo = wikipediaTrackService.getInfo(artist)
                    artistInfo = serviceArtistInfo?.mapToLocalArtistInfo()

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

    private fun ArtistInfoService.mapToLocalArtistInfo() =
        ArtistInfo(
            description = this.description,
            wikipediaURL = this.wikipediaURL
        )
}