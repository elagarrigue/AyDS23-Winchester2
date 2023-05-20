package ayds.winchester.songinfo.moredetails.presentation

import ayds.observer.Observable
import ayds.observer.Subject
import ayds.winchester2.wikipediaexternal.data.wikipedia.entity.Info
import ayds.winchester2.wikipediaexternal.data.wikipedia.entity.Info.ArtistInfo
import ayds.winchester2.wikipediaexternal.data.wikipedia.entity.Info.EmptyInfo
import ayds.winchester.songinfo.moredetails.domain.repository.WikipediaRepository

interface MoreDetailsPresenter {
    val uiStateObservable: Observable<MoreDetailsUiState>
    fun fetchArtistInfo(artistName: String)
}

internal class MoreDetailsPresenterImpl(
    private val wikipediaRepository: WikipediaRepository,
    private val infoDescriptionHelper: InfoDescriptionHelper
) : MoreDetailsPresenter {

    private val onActionSubject = Subject<MoreDetailsUiState>()
    override val uiStateObservable = onActionSubject
    private var moreDetailsUiState = MoreDetailsUiState(artistInfoDescription = "", artistInfoUrl = "")

    override fun fetchArtistInfo(artistName: String) {
        Thread {
            getArtistInfo(artistName)
        }.start()
    }

    private fun getArtistInfo(artistName: String) {
        val artistInfo = wikipediaRepository.getInfo(artistName)
        updateUiState(artistInfo, artistName)
        uiStateObservable.notify(moreDetailsUiState)
    }

    private fun updateUiState(artistInfo: Info, artistName: String) {
        moreDetailsUiState = when (artistInfo) {
            is ArtistInfo -> moreDetailsUiState.copy(
                artistInfoDescription = infoDescriptionHelper.getInfoDescriptionText(
                    artistInfo, artistName
                ), artistInfoUrl = artistInfo.wikipediaURL
            )
            is EmptyInfo -> moreDetailsUiState.copy(
                artistInfoDescription = "", artistInfoUrl = ""
            )
        }
    }
}