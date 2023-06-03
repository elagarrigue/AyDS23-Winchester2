package ayds.winchester.songinfo.moredetails.injector

import android.content.Context
import ayds.aknewyork.external.service.injector.NYTimesInjector
import ayds.winchester.songinfo.moredetails.data.wikipedia.repository.CardRepositoryImpl
import ayds.winchester.songinfo.moredetails.data.wikipedia.repository.broker.CardsBrokerImpl
import ayds.winchester.songinfo.moredetails.data.wikipedia.repository.proxy.LastFMProxy
import ayds.winchester.songinfo.moredetails.data.wikipedia.repository.proxy.NYTimesProxy
import ayds.winchester.songinfo.moredetails.data.wikipedia.repository.proxy.WikipediaProxy
import ayds.winchester.songinfo.moredetails.data.wikipedia.repository.local.wikipedia.CardLocalStorage
import ayds.winchester.songinfo.moredetails.data.wikipedia.repository.local.wikipedia.sqldb.CursorToWikipediaInfoMapper
import ayds.winchester.songinfo.moredetails.data.wikipedia.repository.local.wikipedia.sqldb.CursorToWikipediaInfoMapperImpl
import ayds.winchester.songinfo.moredetails.data.wikipedia.repository.local.wikipedia.sqldb.CardLocalStorageImpl
import ayds.winchester.songinfo.moredetails.domain.repository.CardRepository
import ayds.winchester.songinfo.moredetails.presentation.*
import ayds.winchester2.wikipediaexternal.injector.WikipediaInjector
import lisboa4LastFM.LastFMInjector


object MoreDetailsInjector {

    private val descriptionFormatter: DescriptionFormatter = HtmlDescriptionFormatter()
    private val infoDescriptionHelper: InfoDescriptionHelper = InfoDescriptionHelperImpl(descriptionFormatter)
    private val artistSourceToStringFactory: ArtistSourceToStringFactory = ArtistSourceToStringFactoryImpl()

    private val cursorToWikipediaInfoMapper: CursorToWikipediaInfoMapper = CursorToWikipediaInfoMapperImpl()
    private lateinit var cardLocalStorage: CardLocalStorage

    private val wikipediaProxy: WikipediaProxy = WikipediaProxy(WikipediaInjector.wikipediaTrackService)
    private val nyTimesProxy : NYTimesProxy = NYTimesProxy(NYTimesInjector.nyTimesService)
    private val lastFMProxy: LastFMProxy = LastFMProxy(LastFMInjector.getLastFmService())
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
        moreDetailsPresenter = MoreDetailsPresenterImpl(repository, infoDescriptionHelper, artistSourceToStringFactory)
        moreDetailsView.setMoreDetailsPresenter(moreDetailsPresenter)
    }

    private fun initLocalStorage(moreDetailsView: MoreDetailsView){
        cardLocalStorage = CardLocalStorageImpl( moreDetailsView as Context, cursorToWikipediaInfoMapper)
    }

    private fun initRepository(){
        repository = CardRepositoryImpl(cardLocalStorage, artistCardBroker)
    }
}