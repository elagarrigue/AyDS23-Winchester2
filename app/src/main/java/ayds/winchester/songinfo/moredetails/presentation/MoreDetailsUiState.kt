package ayds.winchester.songinfo.moredetails.presentation

import ayds.winchester.songinfo.moredetails.domain.entity.Card
import ayds.winchester.songinfo.moredetails.domain.entity.Source

data class MoreDetailsUiState(
    var actionsEnabled: Boolean = false,
    var spinnerValues: List<String> = emptyList(),
    var cardList: List<Card> = listOf(Card())
)