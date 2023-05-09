package ayds.winchester.songinfo.moredetails.presentation

import ayds.observer.Observer
import ayds.winchester.songinfo.moredetails.domain.entity.Info.EmptyInfo
import ayds.winchester.songinfo.moredetails.domain.entity.Info.ArtistInfo
import ayds.winchester.songinfo.moredetails.domain.repository.InfoRepository

interface MoreDetailsPresenter {
    fun setMoreDetailsView(moreDetailsView: MoreDetailsView)
}

class MoreDetailsPresenterImpl(private val infoRepository: InfoRepository, private val infoDescriptionHelper: InfoDescriptionHelper): MoreDetailsPresenter{
    private lateinit var moreDetailsView: MoreDetailsView

    override fun setMoreDetailsView(moreDetailsView: MoreDetailsView) {
        this.moreDetailsView = moreDetailsView
        moreDetailsView.uiEventObservable.subscribe(observer)
        createThread()
    }

    private val observer: Observer<MoreDetailsUiEvent> =
        Observer {value ->
            when(value){
                MoreDetailsUiEvent.ViewFullArticleUrl -> openInfoUrl()
            }
        }

    private fun openInfoUrl() {
        moreDetailsView.openExternalLink(moreDetailsView.uiState.artistInfoUrl)
    }

    private fun createThread(){
        Thread {
            getArtistInfo()
        }.start()
    }

    private fun getArtistInfo(){
        val artistName = moreDetailsView.getArtistName()
        when (val artistInfo = infoRepository.getInfo(artistName)) {
            is ArtistInfo -> {
                moreDetailsView.uiState = moreDetailsView.uiState.copy(artistInfoDescription = infoDescriptionHelper.getInfoDescriptionText(artistInfo, artistName), artistInfoUrl = artistInfo.wikipediaURL)
            }
            is EmptyInfo -> {
                moreDetailsView.uiState = moreDetailsView.uiState.copy(artistInfoDescription = "", artistInfoUrl = "")
            }
        }
        moreDetailsView.updateArtistInfo()
    }
}