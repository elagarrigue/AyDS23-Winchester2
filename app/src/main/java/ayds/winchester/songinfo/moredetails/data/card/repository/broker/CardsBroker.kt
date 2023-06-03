package ayds.winchester.songinfo.moredetails.data.card.repository.broker

import ayds.winchester.songinfo.moredetails.data.card.repository.proxy.Proxy
import ayds.winchester.songinfo.moredetails.domain.entity.Card

interface CardsBroker {
    fun getCards(artistName: String): List<Card>
}

internal class CardsBrokerImpl(private val proxiesList: List<Proxy>): CardsBroker {
    override fun getCards(artistName: String): List<Card>{
        val cardsList = mutableListOf<Card>()
        for(proxy in proxiesList){
            proxy.getCard(artistName)?.let { cardsList.add(it)}
        }
        return cardsList
    }
}