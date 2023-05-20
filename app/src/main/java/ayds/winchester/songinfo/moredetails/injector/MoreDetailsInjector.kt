package ayds.winchester.songinfo.moredetails.injector

import android.content.Context
import ayds.winchester.songinfo.moredetails.data.wikipedia.repository.WikipediaRepositoryImpl
import ayds.winchester2.wikipediaexternal.data.wikipedia.WikipediaTrackService
import ayds.winchester2.wikipediaexternal.data.wikipedia.tracks.JsonToInfoResolver
import ayds.winchester2.wikipediaexternal.data.wikipedia.tracks.WikipediaTrackAPI
import ayds.winchester2.wikipediaexternal.data.wikipedia.tracks.WikipediaTrackServiceImpl
import ayds.winchester.songinfo.moredetails.data.wikipedia.repository.local.wikipedia.WikipediaLocalStorage
import ayds.winchester.songinfo.moredetails.data.wikipedia.repository.local.wikipedia.sqldb.CursorToWikipediaInfoMapper
import ayds.winchester.songinfo.moredetails.data.wikipedia.repository.local.wikipedia.sqldb.CursorToWikipediaInfoMapperImpl
import ayds.winchester.songinfo.moredetails.data.wikipedia.repository.local.wikipedia.sqldb.WikipediaLocalStorageImpl
import ayds.winchester.songinfo.moredetails.domain.repository.WikipediaRepository
import ayds.winchester.songinfo.moredetails.presentation.*
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

private const val WIKIPEDIA_URL = "https://en.wikipedia.org/w/"

object MoreDetailsInjector {

    private val wikipediaAPIRetrofit = getRetrofit()
    private val wikipediaTrackAPI = getWikipediaAPI(wikipediaAPIRetrofit)
    private val wikipediaToInfoResolver: ayds.winchester2.wikipediaexternal.data.wikipedia.tracks.WikipediaToInfoResolver = JsonToInfoResolver()
    private val wikipediaTrackService: WikipediaTrackService =
        WikipediaTrackServiceImpl(
            wikipediaTrackAPI,
            wikipediaToInfoResolver
        )

    private val descriptionFormatter: DescriptionFormatter = HtmlDescriptionFormatter()
    private val infoDescriptionHelper: InfoDescriptionHelper = InfoDescriptionHelperImpl(descriptionFormatter)

    private val cursorToWikipediaInfoMapper: CursorToWikipediaInfoMapper = CursorToWikipediaInfoMapperImpl()
    private lateinit var wikipediaLocalStorage: WikipediaLocalStorage

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
        repository = WikipediaRepositoryImpl(wikipediaLocalStorage, wikipediaTrackService)
    }

    private fun getRetrofit() = Retrofit.Builder()
        .baseUrl(WIKIPEDIA_URL)
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()

    private fun getWikipediaAPI(retrofit: Retrofit) = retrofit.create(WikipediaTrackAPI::class.java)
}