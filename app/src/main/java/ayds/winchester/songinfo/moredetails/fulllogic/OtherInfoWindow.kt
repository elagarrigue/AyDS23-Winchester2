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
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.squareup.picasso.Picasso
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.*

private const val WIKIPEDIA_URL = "https://en.wikipedia.org/w/"
private const val DEFAULT_WIKIPEDIA_IMAGE =
    "https://upload.wikimedia.org/wikipedia/commons/8/8c/Wikipedia-logo-v2-es.png"
private const val WIKIPEDIA_URL_PREFIX = "https://en.wikipedia.org/?curid="
private const val NO_RESULTS = "No Results"

class OtherInfoWindow : AppCompatActivity() {
    private lateinit var textPane2: TextView
    private lateinit var openUrlButton: Button
    private lateinit var imageView: ImageView
    private lateinit var dataBase: DataBase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_info)
        initProperties()
        initLocalDataBaseConnection()
        getArtistInfo(intent.getStringExtra(ARTIST_NAME_EXTRA))
    }

    private fun initProperties() {
        textPane2 = findViewById(R.id.textPane2)
        openUrlButton = findViewById(R.id.openUrlButton)
        imageView = findViewById(R.id.imageView)
    }

    private fun initLocalDataBaseConnection() {
        dataBase = DataBase(this)
    }

    private fun getArtistInfo(artistName: String?) {

        Thread { //TODO Solo mostrar informarcion
            // TODO RECUPERACION
            // TODO FORMATERO
            // TODO PRESENTACION
            var artistInfo = getInfoFromLocalDataBase(artistName)
            if (existsInLocalDataBase(artistInfo))
                artistInfo = markArtistInfoAsLocal(artistInfo)
            else {
                val artistInfoFromService = getInfoFromService(artistName)
                val artistSnippet = artistInfoFromService.getSnippet()
                val artistURL = artistInfoFromService.getURL()
                if (existsArtistSnippet(artistSnippet)) {
                    artistInfo = NO_RESULTS
                } else {
                    artistInfo = reformatToHtml(artistSnippet, artistName)
                    saveArtistToDataBase(artistName, artistInfo)
                }
                setOpenURLButtonListener(artistURL)
            }
            displayArtistInfo(artistInfo)
        }.start()
    }

    private fun getInfoFromLocalDataBase(artistName: String?) =
        DataBase.getInfo(dataBase, artistName)

    private fun existsInLocalDataBase(text: String?) = (text != null)

    private fun markArtistInfoAsLocal(artistInfo: String) = "[*]$artistInfo"

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
        return jsonObject["query"].asJsonObject
    }

    private fun JsonObject.getSnippet() = this["search"].asJsonArray[0].asJsonObject["snippet"]

    private fun JsonObject.getURL() = "$WIKIPEDIA_URL_PREFIX${this.getPageID()}"

    private fun JsonObject.getPageID() = this["search"].asJsonArray[0].asJsonObject["pageid"]

    private fun existsArtistSnippet(artistSnippet: JsonElement?) = artistSnippet == null

    private fun reformatToHtml(snippet: JsonElement, artistName: String?): String {
        val text1 = snippet.asString.replace("\\n", "\n")
        return textToHtml(text1, artistName)
    }

    private fun setOpenURLButtonListener(urlString: String) {
        openUrlButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(urlString)
            startActivity(intent)
        }
    }

    private fun displayArtistInfo(artistInfo: String?) {
        runOnUiThread {
            Picasso.get().load(DEFAULT_WIKIPEDIA_IMAGE).into(imageView)
            textPane2.text = Html.fromHtml(artistInfo)
        }
    }

    companion object {
        const val ARTIST_NAME_EXTRA = "artistName"
    }
    private fun textToHtml(text: String, term: String?): String {
        val builder = StringBuilder()
        builder.append("<html><div width=400>") //TODO
        builder.append("<font face=\"arial\">")
        val textWithBold = formatTextWithBold(text, term)
        builder.append(textWithBold)
        builder.append("</font></div></html>")
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
        DataBase.saveArtist(dataBase, artistName, text)
    }
}