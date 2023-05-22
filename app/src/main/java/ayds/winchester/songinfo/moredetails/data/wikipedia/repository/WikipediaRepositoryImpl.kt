package ayds.winchester.songinfo.moredetails.data.wikipedia.repository

import ayds.winchester.songinfo.moredetails.domain.entity.Card
import ayds.winchester.songinfo.moredetails.domain.entity.Card.EmptyCard
import ayds.winchester.songinfo.moredetails.domain.entity.Card.ArtistCard
import ayds.winchester2.wikipediadata.data.wikipedia.WikipediaTrackService
import ayds.winchester2.wikipediadata.data.wikipedia.entity.ArtistInfo as ArtistInfoService
import ayds.winchester.songinfo.moredetails.data.wikipedia.repository.local.wikipedia.WikipediaLocalStorage
import ayds.winchester.songinfo.moredetails.domain.repository.WikipediaRepository

internal class WikipediaRepositoryImpl(
    private val wikipediaLocalStorage: WikipediaLocalStorage,
    private val wikipediaTrackService: WikipediaTrackService,
) : WikipediaRepository {

    override fun getInfo(artist: String): Card {
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

        return artistInfo ?: EmptyCard
    }

    private fun markInfoAsLocal(info: ArtistCard) {
        info.isLocallyStored = true
    }

    private fun ArtistInfoService.mapToLocalArtistInfo() =
        ArtistCard(
            description = this.description,
            infoURL = this.wikipediaURL
        )
}