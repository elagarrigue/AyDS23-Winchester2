package ayds.winchester.songinfo.moredetails.injector

import android.content.Context
import ayds.winchester.songinfo.moredetails.data.wikipedia.repository.InfoRepositoryImpl
import ayds.winchester.songinfo.moredetails.data.wikipedia.repository.external.wikipedia.WikipediaTrackService
import ayds.winchester.songinfo.moredetails.data.wikipedia.repository.external.wikipedia.tracks.*
import ayds.winchester.songinfo.moredetails.data.wikipedia.repository.external.wikipedia.tracks.JsonToInfoResolver
import ayds.winchester.songinfo.moredetails.data.wikipedia.repository.external.wikipedia.tracks.WikipediaTrackAPI
import ayds.winchester.songinfo.moredetails.data.wikipedia.repository.external.wikipedia.tracks.WikipediaTrackServiceImpl
import ayds.winchester.songinfo.moredetails.data.wikipedia.repository.local.wikipedia.WikipediaLocalStorage
import ayds.winchester.songinfo.moredetails.data.wikipedia.repository.local.wikipedia.sqldb.CursorToWikipediaInfoMapper
import ayds.winchester.songinfo.moredetails.data.wikipedia.repository.local.wikipedia.sqldb.CursorToWikipediaInfoMapperImpl
import ayds.winchester.songinfo.moredetails.data.wikipedia.repository.local.wikipedia.sqldb.WikipediaLocalStorageImpl
import ayds.winchester.songinfo.moredetails.domain.repository.InfoRepository
import ayds.winchester.songinfo.moredetails.presentation.MoreDetailsPresenter
import ayds.winchester.songinfo.moredetails.presentation.MoreDetailsPresenterImpl
import ayds.winchester.songinfo.moredetails.presentation.MoreDetailsView
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

private const val WIKIPEDIA_URL = "https://en.wikipedia.org/w/"

object MoreDetailsInjector {

    private val wikipediaAPIRetrofit = getRetrofit()
    private val wikipediaTrackAPI = getWikipediaAPI(wikipediaAPIRetrofit)
    private val wikipediaToInfoResolver: WikipediaToInfoResolver = JsonToInfoResolver()
    private val wikipediaTrackService: WikipediaTrackService = WikipediaTrackServiceImpl(wikipediaTrackAPI, wikipediaToInfoResolver)

    private val cursorToWikipediaInfoMapper: CursorToWikipediaInfoMapper = CursorToWikipediaInfoMapperImpl()
    private lateinit var wikipediaLocalStorage: WikipediaLocalStorage

    private lateinit var moreDetailsPresenter: MoreDetailsPresenter
    val repository: InfoRepository = InfoRepositoryImpl(wikipediaLocalStorage, wikipediaTrackService)

    fun init(moreDetailsView: MoreDetailsView){
        initLocalStorage(moreDetailsView)
        initMoreDetailsPresenter(moreDetailsView)
    }

    private fun initLocalStorage(moreDetailsView: MoreDetailsView){
        wikipediaLocalStorage = WikipediaLocalStorageImpl( moreDetailsView as Context, cursorToWikipediaInfoMapper)
    }

    private fun initMoreDetailsPresenter(moreDetailsView: MoreDetailsView){
        moreDetailsPresenter = MoreDetailsPresenterImpl(repository)
        moreDetailsPresenter.setMoreDetailsView(moreDetailsView)
    }

    private fun getRetrofit() = Retrofit.Builder()
        .baseUrl(WIKIPEDIA_URL)
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()

    private fun getWikipediaAPI(retrofit: Retrofit) = retrofit.create(WikipediaTrackAPI::class.java)
}