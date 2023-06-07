package ayds.winchester.songinfo.moredetails.data.card.repository.proxy

import ayds.winchester.songinfo.moredetails.domain.entity.Card


interface CardProxy {
    fun getCard(artistName: String): Card?
}