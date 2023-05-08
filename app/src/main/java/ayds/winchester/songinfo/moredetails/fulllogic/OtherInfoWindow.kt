package ayds.winchester.songinfo.moredetails.fulllogic

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ayds.winchester.songinfo.R
import ayds.winchester.songinfo.moredetails.domain.entity.Info.ArtistInfo
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.squareup.picasso.Picasso
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.*

private const val WIKIPEDIA_URL = "https://en.wikipedia.org/w/"
private const val DEFAULT_WIKIPEDIA_IMAGE = "https://upload.wikimedia.org/wikipedia/commons/8/8c/Wikipedia-logo-v2-es.png"
private const val WIKIPEDIA_URL_PREFIX = "https://en.wikipedia.org/?curid="
private const val NO_RESULTS = "No Results"
private const val HTML_START_WIDTH = "<html><div width=400>"
private const val HTML_FONT = "<font face=\"arial\">"
private const val HTML_END = "</font></div></html>"
private const val QUERY = "query"
private const val SEARCH = "search"
private const val SNIPPET = "snippet"
private const val PAGE_ID = "pageid"

class OtherInfoWindow : AppCompatActivity() {
    private lateinit var artistInfoTextPane: TextView
    private lateinit var openUrlButton: Button
    private lateinit var imageView: ImageView
    private val dataBase = DataBase(this)

    companion object {
        const val ARTIST_NAME_EXTRA = "artistName"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initProperties()
        createThreadForInfo()
    }

    private fun initProperties() {
        setContentView(R.layout.activity_other_info)
        artistInfoTextPane = findViewById(R.id.textPane2)
        openUrlButton = findViewById(R.id.openUrlButton)
        imageView = findViewById(R.id.imageView)
    }

    private fun createThreadForInfo() {
        Thread {
            displayWindow()
        }.start()
    }

    private fun displayWindow() {
        val artist = getArtistInfo()
        val description = formatArtistInfo(artist)
        runOnUiThread {
            loadWikipediaLogo()
            setArtistDescription(description)
        }
        setOpenURLButtonListener(artist.wikipediaURL)
    }

    private fun getArtistInfo():ArtistInfo{
        val artistName = intent.getStringExtra(ARTIST_NAME_EXTRA)
        var artistInfo = getInfoFromLocalDataBase(artistName)
        if (!artistInfo.isLocallyStored) {
            val artistInfoFromService = getInfoFromService(artistName)
            artistInfo = resolveArtistInfo(artistInfoFromService,artistName)
            if(artistInfo.description != NO_RESULTS)
                saveArtistToDataBase(artistName,artistInfo.description)
        }
        return artistInfo
    }

    private fun resolveArtistInfo(artistInfoFromService: JsonObject, artistName: String?): ArtistInfo {
        val artistSnippet = artistInfoFromService.getSnippet()
        return if (artistSnippet == null) {
            ArtistInfo(NO_RESULTS, WIKIPEDIA_URL_PREFIX)
        } else {
            ArtistInfo(reformatToHtml(artistSnippet, artistName),artistInfoFromService.getURL(),false)
        }
    }

    private fun getInfoFromLocalDataBase(artistName: String?):ArtistInfo{
        val description = dataBase.getArtistInfo(artistName)
        return if (description == "") ArtistInfo("",WIKIPEDIA_URL_PREFIX) else ArtistInfo(description,WIKIPEDIA_URL_PREFIX, isLocallyStored = true)

    }

    private fun formatInfoFromLocalDataBase(artistInfo: String?) = "[*]$artistInfo"

    private fun getInfoFromService(artistName: String?): JsonObject {
        val wikipediaAPI = createWikipediaAPIConnection()
        return wikipediaAPI.getArtistInfoFromQuery(artistName)
    }

    private fun createWikipediaAPIConnection(): WikipediaAPI {
        val retrofit = Retrofit.Builder().baseUrl(WIKIPEDIA_URL)
            .addConverterFactory(ScalarsConverterFactory.create()).build()
        return retrofit.create(WikipediaAPI::class.java)
    }

    private fun WikipediaAPI.getArtistInfoFromQuery(artistName: String?): JsonObject {
        val callResponse = this.getArtistInfo(artistName).execute()
        val jsonObject = Gson().fromJson(callResponse.body(), JsonObject::class.java)
        return jsonObject[QUERY].asJsonObject
    }

    private fun JsonObject.getSnippet() = this[SEARCH].asJsonArray[0].asJsonObject[SNIPPET]

    private fun JsonObject.getURL() = "$WIKIPEDIA_URL_PREFIX${this.getPageID()}"

    private fun JsonObject.getPageID() = this[SEARCH].asJsonArray[0].asJsonObject[PAGE_ID]

    private fun reformatToHtml(snippet: JsonElement, artistName: String?): String {
        val text1 = snippet.asString.replace("\\n", "\n")
        return textToHtml(text1, artistName)
    }

    private fun setOpenURLButtonListener(urlString: String) {
        openUrlButton.setOnClickListener {
            navigateTo(urlString)
        }
    }

    private fun navigateTo(urlString: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(urlString)
        startActivity(intent)
    }

    private fun textToHtml(text: String, term: String?): String {
        val builder = StringBuilder()
        builder.append(HTML_START_WIDTH)
        builder.append(HTML_FONT)
        val textWithBold = formatTextWithBold(text, term)
        builder.append(textWithBold)
        builder.append(HTML_END)
        return builder.toString()
    }

    private fun formatTextWithBold(text: String, term: String?): String {
        return text
            .replace("'", " ")
            .replace("\n", "<br>")
            .replace(
                "(?i)$term".toRegex(),
                "<b>" + term!!.uppercase(Locale.getDefault()) + "</b>"
            )
    }

    private fun saveArtistToDataBase(artistName: String?, text: String) {
        dataBase.saveArtist(artistName, text)
    }

    private fun loadWikipediaLogo(){
        Picasso.get().load(DEFAULT_WIKIPEDIA_IMAGE).into(imageView)
    }

    private fun setArtistDescription(artistInfo: String?){
        artistInfoTextPane.text = Html.fromHtml(artistInfo)
    }

    private fun formatArtistInfo(artist: ArtistInfo) = if (artist.isLocallyStored) formatInfoFromLocalDataBase(artist.description) else artist.description
}



