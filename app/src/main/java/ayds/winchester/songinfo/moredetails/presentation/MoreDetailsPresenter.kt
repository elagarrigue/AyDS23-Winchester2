package ayds.winchester.songinfo.moredetails.presentation

import ayds.observer.Observable
import ayds.observer.Subject
import ayds.winchester.songinfo.moredetails.domain.entity.Card
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
    private var moreDetailsUiState = MoreDetailsUiState()

    override fun fetchArtistInfo(artistName: String) {
        Thread {
            getArtistInfo(artistName)
        }.start()
    }

    private fun getArtistInfo(artistName: String) {
        val cards = wikipediaRepository.getCards(artistName)
        updateUiState(cards, artistName)
        uiStateObservable.notify(moreDetailsUiState)
    }

    private fun updateUiState(artistCards: List<Card>, artistName: String) {
        moreDetailsUiState = moreDetailsUiState.copy(
            cardList = formatArtistCards(artistCards, artistName)
        )
    }

    private fun formatArtistCards(artistCards: List<Card>, artistName: String): List<Card>{
        val formattedCards = artistCards.map {card ->
            val formattedDescription = infoDescriptionHelper.getInfoDescriptionText(card, artistName)
            Card(
                description = formattedDescription,
                infoURL = card.infoURL,
                source = card.source,
                sourceLogoUrl = card.sourceLogoUrl,
                isLocallyStored = card.isLocallyStored
            )
        }
        return formattedCards
    }
}


