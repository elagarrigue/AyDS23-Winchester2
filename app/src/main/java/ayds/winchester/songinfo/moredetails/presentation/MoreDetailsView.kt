package ayds.winchester.songinfo.moredetails.presentation

import android.os.Bundle
import android.text.Html
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ayds.observer.Observable
import ayds.observer.Subject
import ayds.winchester.songinfo.R
import ayds.winchester.songinfo.moredetails.injector.MoreDetailsInjector
import ayds.winchester.songinfo.utils.UtilsInjector
import ayds.winchester.songinfo.utils.navigation.NavigationUtils
import com.squareup.picasso.Picasso

interface MoreDetailsView {
    val uiEventObservable: Observable<MoreDetailsUiEvent>
    var uiState: MoreDetailsUiState

    fun openExternalLink(url: String)
    fun getArtistName(): String
    fun updateArtistInfo()
}

private const val DEFAULT_WIKIPEDIA_IMAGE = "https://upload.wikimedia.org/wikipedia/commons/8/8c/Wikipedia-logo-v2-es.png"

class MoreDetailsViewImpl: AppCompatActivity(), MoreDetailsView{

    private val onActionSubject = Subject<MoreDetailsUiEvent>()
    private val navigationUtils: NavigationUtils = UtilsInjector.navigationUtils

    private lateinit var artistInfoTextPane: TextView
    private lateinit var openUrlButton: Button
    private lateinit var imageView: ImageView

    override val uiEventObservable: Observable<MoreDetailsUiEvent> = onActionSubject
    override var uiState: MoreDetailsUiState = MoreDetailsUiState()

    companion object {
        const val ARTIST_NAME_EXTRA = "artistName"
    }

    override fun openExternalLink(url: String) {
        navigationUtils.openExternalUrl(this, url)
    }

    override fun getArtistName() = intent.getStringExtra(ARTIST_NAME_EXTRA)?.let { it } ?: ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_info)
        initModule()
        initProperties()
        initListeners()
    }

    override fun updateArtistInfo() {
        loadWikipediaLogo()
        setArtistDescription()
    }

    private fun initModule() {
        MoreDetailsInjector.init(this)
    }

    private fun initProperties(){
        artistInfoTextPane = findViewById(R.id.textPane2)
        openUrlButton = findViewById(R.id.openUrlButton)
        imageView = findViewById(R.id.imageView)
    }

    private fun initListeners() {
        openUrlButton.setOnClickListener {
            notifyOpenUrlAction()
        }
    }

    private fun loadWikipediaLogo(){
        runOnUiThread {
            Picasso.get().load(DEFAULT_WIKIPEDIA_IMAGE).into(imageView)
        }
    }

    private fun setArtistDescription(){
        runOnUiThread {
            artistInfoTextPane.text = Html.fromHtml(uiState.artistInfoDescription)
        }
    }

    private fun notifyOpenUrlAction(){
        onActionSubject.notify(MoreDetailsUiEvent.ViewFullArticleUrl)
    }
}