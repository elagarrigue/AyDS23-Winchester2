package ayds.winchester.songinfo.moredetails.data.wikipedia.repository.broker

import ayds.winchester.songinfo.moredetails.domain.entity.Card

interface Broker {
    fun getCards(artistName: String): List<Card>
}

internal class ArtistCardBroker(private val proxiesList: List<Proxy>): Broker {
    override fun getCards(artistName: String): List<Card>{
        val cardsList = mutableListOf<Card>()
        for(proxy in proxiesList){
            proxy.getCard(artistName)?.let { cardsList.add(it)}
        }
        return cardsList
    }
}