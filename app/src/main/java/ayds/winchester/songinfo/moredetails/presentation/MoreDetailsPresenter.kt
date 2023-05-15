package ayds.winchester.songinfo.moredetails.presentation

import ayds.observer.Observable
import ayds.observer.Subject
import ayds.winchester.songinfo.moredetails.domain.entity.Info.ArtistInfo
import ayds.winchester.songinfo.moredetails.domain.entity.Info.EmptyInfo
import ayds.winchester.songinfo.moredetails.domain.repository.WikipediaRepository

interface MoreDetailsPresenter {
    val uiStateObservable: Observable<MoreDetailsUiState>
    fun createThread(artistName: String)
}

internal class MoreDetailsPresenterImpl(
    private val wikipediaRepository: WikipediaRepository,
    private val infoDescriptionHelper: InfoDescriptionHelper
) : MoreDetailsPresenter {

    private val onActionSubject = Subject<MoreDetailsUiState>()
    override val uiStateObservable = onActionSubject

    override fun createThread(artistName: String) {
        Thread {
            getArtistInfo(artistName)
        }.start()
    }

    private fun getArtistInfo(artistName: String) {
        when (val artistInfo = wikipediaRepository.getInfo(artistName)) {
            is ArtistInfo ->
                uiStateObservable.notify(buildUiStateArtistInfo(artistInfo, artistName))
            is EmptyInfo ->
                uiStateObservable.notify(buildUiStateEmptyInfo())
        }
    }

    private fun buildUiStateArtistInfo(artistInfo: ArtistInfo, artistName: String) =
        MoreDetailsUiState(
            artistInfoDescription = infoDescriptionHelper.getInfoDescriptionText(artistInfo, artistName),
            artistInfoUrl = artistInfo.wikipediaURL
        )

    private fun buildUiStateEmptyInfo() =
        MoreDetailsUiState(
            artistInfoDescription = "",
            artistInfoUrl = ""
        )
}