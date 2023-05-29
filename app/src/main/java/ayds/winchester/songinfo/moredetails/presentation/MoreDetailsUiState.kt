package ayds.winchester.songinfo.moredetails.presentation

import ayds.winchester.songinfo.moredetails.domain.entity.Card

data class MoreDetailsUiState(
    val cardList: List<Card> = emptyList()
)