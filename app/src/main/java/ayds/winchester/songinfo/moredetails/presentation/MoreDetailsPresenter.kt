package ayds.winchester.songinfo.moredetails.presentation

import ayds.observer.Observable
import ayds.observer.Subject
import ayds.winchester.songinfo.moredetails.domain.entity.Card
import ayds.winchester.songinfo.moredetails.domain.entity.Source
import ayds.winchester.songinfo.moredetails.domain.repository.CardRepository

interface MoreDetailsPresenter {
    val uiStateObservable: Observable<MoreDetailsUiState>
    fun fetchArtistInfo(artistName: String)
}

internal class MoreDetailsPresenterImpl(
    private val cardRepository: CardRepository,
    private val cardDescriptionHelper: CardDescriptionHelper,
    private val artistSourceToStringFactory: ArtistSourceToStringFactory,
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
        val cards = cardRepository.getCards(artistName)
        updateUiState(cards, artistName)
        uiStateObservable.notify(moreDetailsUiState)
    }

    private fun updateUiState(artistCards: List<Card>, artistName: String) {
        moreDetailsUiState = if (artistCards.isEmpty()) {
            moreDetailsUiState.copy(
                spinnerValues = listOf("Not found")
            )
        } else {
            moreDetailsUiState.copy(
                cardList = formatArtistCards(artistCards, artistName),
                actionsEnabled = true,
                spinnerValues = formatSourceValues(artistCards)
                )
        }
    }

    private fun formatSourceValues(artistCards: List<Card>): List<String> {
        val spinnerValues = artistCards.map {
            artistSourceToStringFactory.get(it.source)
        }
        return spinnerValues
    }

    private fun formatArtistCards(artistCards: List<Card>, artistName: String): List<Card>{
        val formattedCards = artistCards.map {card ->
            val formattedDescription = cardDescriptionHelper.getInfoDescriptionText(card, artistName)
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


