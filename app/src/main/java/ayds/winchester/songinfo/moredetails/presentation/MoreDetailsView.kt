package ayds.winchester.songinfo.moredetails.presentation

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import ayds.observer.Observer
import ayds.winchester.songinfo.R
import ayds.winchester.songinfo.moredetails.domain.entity.Card
import ayds.winchester.songinfo.moredetails.domain.entity.Source
import ayds.winchester.songinfo.moredetails.injector.MoreDetailsInjector
import ayds.winchester.songinfo.utils.UtilsInjector
import ayds.winchester.songinfo.utils.navigation.NavigationUtils
import com.squareup.picasso.Picasso

interface MoreDetailsView {
    fun setMoreDetailsPresenter(moreDetailsPresenter: MoreDetailsPresenter)
}

class MoreDetailsViewImpl: AppCompatActivity(), MoreDetailsView{
    private val navigationUtils: NavigationUtils = UtilsInjector.navigationUtils

    private lateinit var artistInfoTextPane: TextView
    private lateinit var openUrlButton: Button
    private lateinit var imageView: ImageView
    private lateinit var moreDetailsPresenter: MoreDetailsPresenter
    private lateinit var sourceLabel: TextView
    private lateinit var spinnerUI: Spinner

    private lateinit var artistCards: List<Card>

    private val observer: Observer<MoreDetailsUiState> =
        Observer {
                value -> updateView(value)
        }

    companion object {
        const val ARTIST_NAME_EXTRA = "artistName"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_info)
        initModule()
        initProperties()
        updateArtistInfo()
    }

    private fun initModule() {
        MoreDetailsInjector.init(this)
    }

    private fun initProperties(){
        artistInfoTextPane = findViewById(R.id.textPane2)
        openUrlButton = findViewById(R.id.openUrlButton)
        imageView = findViewById(R.id.imageView)
        spinnerUI = findViewById(R.id.spinner)
        sourceLabel = findViewById(R.id.sourceTextView)
    }

    private fun updateArtistInfo() {
        moreDetailsPresenter.fetchArtistInfo(getArtistName())
    }

    override fun setMoreDetailsPresenter(moreDetailsPresenter: MoreDetailsPresenter) {
        this.moreDetailsPresenter = moreDetailsPresenter
        moreDetailsPresenter.uiStateObservable.subscribe(observer)
    }

    private fun updateView(uiState: MoreDetailsUiState){
        initArtistCards(uiState.cardList)
        initCard()
        initSpinner(uiState.spinnerValues)
        setActionsEnabled(uiState.actionsEnabled)
    }

    private fun initArtistCards(cardList: List<Card>) {
        artistCards = cardList
    }

    private fun initCard() {
        if(artistCards.isEmpty())
            artistCards = listOf(Card(description = "No results", infoURL = "No results", sourceLogoUrl = "No results"))
        updateCardView(artistCards.first())
    }

    private fun initSpinner(spinnerValues: List<String>) {
        runOnUiThread {
            spinnerUI.adapter =
                ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, spinnerValues)
        }

        spinnerUI.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                index: Int,
                id: Long
            ) {
                updateCardView(artistCards[index])
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun updateCardView(card: Card) {
        loadSourceLogo(card.sourceLogoUrl)
        setArtistDescription(card.description)
        setUrl(card.infoURL)
        setSourceLabel(card.source.toString())
    }

    private fun getArtistName() = intent.getStringExtra(ARTIST_NAME_EXTRA) ?: ""

    private fun loadSourceLogo(logo: String){
        runOnUiThread {
            Picasso.get().load(logo).into(imageView)
        }
    }

    private fun setArtistDescription(artistInfoDescription: String){
        runOnUiThread {
            artistInfoTextPane.text = HtmlCompat.fromHtml(artistInfoDescription,HtmlCompat.FROM_HTML_MODE_LEGACY)
        }
    }

    private fun setActionsEnabled(buttonEnabled: Boolean) {
        runOnUiThread {
            spinnerUI.isEnabled = buttonEnabled
            openUrlButton.isEnabled = buttonEnabled
        }
    }

    private fun setSourceLabel(sourceName: String){
        runOnUiThread {
            sourceLabel.text = sourceName
        }
    }

    private fun setUrl(url: String){
        runOnUiThread {
            openUrlButton.setOnClickListener{
                navigationUtils.openExternalUrl(this, url)
            }
        }
    }
}