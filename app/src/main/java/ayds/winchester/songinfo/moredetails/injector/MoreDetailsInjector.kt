package ayds.winchester.songinfo.moredetails.injector

import android.content.Context
import ayds.aknewyork.external.service.injector.NYTimesInjector
import ayds.winchester.songinfo.moredetails.data.wikipedia.repository.WikipediaRepositoryImpl
import ayds.winchester.songinfo.moredetails.data.wikipedia.repository.broker.ArtistCardBroker
import ayds.winchester.songinfo.moredetails.data.wikipedia.repository.broker.LastFMProxy
import ayds.winchester.songinfo.moredetails.data.wikipedia.repository.broker.NYTimesProxy
import ayds.winchester.songinfo.moredetails.data.wikipedia.repository.broker.WikipediaProxy
import ayds.winchester.songinfo.moredetails.data.wikipedia.repository.local.wikipedia.WikipediaLocalStorage
import ayds.winchester.songinfo.moredetails.data.wikipedia.repository.local.wikipedia.sqldb.CursorToWikipediaInfoMapper
import ayds.winchester.songinfo.moredetails.data.wikipedia.repository.local.wikipedia.sqldb.CursorToWikipediaInfoMapperImpl
import ayds.winchester.songinfo.moredetails.data.wikipedia.repository.local.wikipedia.sqldb.WikipediaLocalStorageImpl
import ayds.winchester.songinfo.moredetails.domain.repository.WikipediaRepository
import ayds.winchester.songinfo.moredetails.presentation.*
import ayds.winchester2.wikipediaexternal.injector.WikipediaInjector
import lisboa4LastFM.LastFMAPI
import lisboa4LastFM.LastFMInjector


object MoreDetailsInjector {

    private val descriptionFormatter: DescriptionFormatter = HtmlDescriptionFormatter()
    private val infoDescriptionHelper: InfoDescriptionHelper = InfoDescriptionHelperImpl(descriptionFormatter)

    private val cursorToWikipediaInfoMapper: CursorToWikipediaInfoMapper = CursorToWikipediaInfoMapperImpl()
    private lateinit var wikipediaLocalStorage: WikipediaLocalStorage

    private val wikipediaProxy: WikipediaProxy = WikipediaProxy(WikipediaInjector.wikipediaTrackService)
    private val nyTimesProxy : NYTimesProxy = NYTimesProxy(NYTimesInjector.nyTimesService)
    private val lastFMProxy: LastFMProxy = LastFMProxy(LastFMInjector.getLastFmService())
    private val proxiesList = listOf(wikipediaProxy, nyTimesProxy, lastFMProxy)
    private val artistCardBroker: ArtistCardBroker = ArtistCardBroker(proxiesList)

    private lateinit var repository: WikipediaRepository
    private lateinit var moreDetailsPresenter: MoreDetailsPresenter

    fun init(moreDetailsView: MoreDetailsView){
        initLocalStorage(moreDetailsView)
        initRepository()
        initMoreDetailsPresenter(moreDetailsView)
    }

    private fun initMoreDetailsPresenter(moreDetailsView: MoreDetailsView){
        moreDetailsPresenter = MoreDetailsPresenterImpl(repository, infoDescriptionHelper)
        moreDetailsView.setMoreDetailsPresenter(moreDetailsPresenter)
    }

    private fun initLocalStorage(moreDetailsView: MoreDetailsView){
        wikipediaLocalStorage = WikipediaLocalStorageImpl( moreDetailsView as Context, cursorToWikipediaInfoMapper)
    }

    private fun initRepository(){
        repository = WikipediaRepositoryImpl(wikipediaLocalStorage, artistCardBroker)
    }
}