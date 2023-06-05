package ayds.winchester.songinfo.moredetails.injector

import android.content.Context
import ayds.aknewyork.external.service.injector.NYTimesInjector
import ayds.winchester.songinfo.moredetails.data.card.repository.CardRepositoryImpl
import ayds.winchester.songinfo.moredetails.data.card.repository.broker.CardsBrokerImpl
import ayds.winchester.songinfo.moredetails.data.card.repository.proxy.LastFMCardProxy
import ayds.winchester.songinfo.moredetails.data.card.repository.proxy.NYTimesCardProxy
import ayds.winchester.songinfo.moredetails.data.card.repository.proxy.WikipediaCardProxy
import ayds.winchester.songinfo.moredetails.data.card.repository.local.card.CardLocalStorage
import ayds.winchester.songinfo.moredetails.data.card.repository.local.card.sqldb.CursorToCardMapper
import ayds.winchester.songinfo.moredetails.data.card.repository.local.card.sqldb.CursorToCardMapperImpl
import ayds.winchester.songinfo.moredetails.data.card.repository.local.card.sqldb.CardLocalStorageImpl
import ayds.winchester.songinfo.moredetails.domain.repository.CardRepository
import ayds.winchester.songinfo.moredetails.presentation.*
import ayds.winchester2.wikipediaexternal.injector.WikipediaInjector
import lisboa4LastFM.LastFMInjector


object MoreDetailsInjector {

    private val descriptionFormatter: DescriptionFormatter = HtmlDescriptionFormatter()
    private val cardDescriptionHelper: CardDescriptionHelper = CardDescriptionHelperImpl(descriptionFormatter)
    private val artistSourceToStringFactory: ArtistSourceToStringFactory = ArtistSourceToStringFactoryImpl()

    private val cursorToCardMapper: CursorToCardMapper = CursorToCardMapperImpl()
    private lateinit var cardLocalStorage: CardLocalStorage

    private val wikipediaProxy: WikipediaCardProxy = WikipediaCardProxy(WikipediaInjector.wikipediaTrackService)
    private val nyTimesProxy : NYTimesCardProxy = NYTimesCardProxy(NYTimesInjector.nyTimesService)
    private val lastFMProxy: LastFMCardProxy = LastFMCardProxy(LastFMInjector.getLastFmService())
    private val proxiesList = listOf(wikipediaProxy, nyTimesProxy, lastFMProxy)
    private val artistCardBroker: CardsBrokerImpl = CardsBrokerImpl(proxiesList)

    private lateinit var repository: CardRepository
    private lateinit var moreDetailsPresenter: MoreDetailsPresenter

    fun init(moreDetailsView: MoreDetailsView){
        initLocalStorage(moreDetailsView)
        initRepository()
        initMoreDetailsPresenter(moreDetailsView)
    }

    private fun initMoreDetailsPresenter(moreDetailsView: MoreDetailsView){
        moreDetailsPresenter = MoreDetailsPresenterImpl(repository, cardDescriptionHelper, artistSourceToStringFactory)
        moreDetailsView.setMoreDetailsPresenter(moreDetailsPresenter)
    }

    private fun initLocalStorage(moreDetailsView: MoreDetailsView){
        cardLocalStorage = CardLocalStorageImpl( moreDetailsView as Context, cursorToCardMapper)
    }

    private fun initRepository(){
        repository = CardRepositoryImpl(cardLocalStorage, artistCardBroker)
    }
}