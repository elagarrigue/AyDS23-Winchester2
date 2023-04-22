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
import java.io.IOException
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
        initDataBaseConnection()

        open(intent.getStringExtra("artistName"))
    }

    private fun initDataBaseConnection() {
        dataBase = DataBase(this)
    }

    private fun getArtistInfo(artistName: String?) {

        Thread {
            var text = getInfoFromLocalDataBase(artistName) // TODO Recibir PageID y Snippet (text) --> NO ARREGLAR
            if (existsInLocalDataBase(text))
                text = markArtistAsLocal(text)
            else {
                try {
                    val artistInfoFromService = getInfoFromService(artistName)
                    val snippet = artistInfoFromService.getSnippet()
                    val pageID = artistInfoFromService.getPageID()
                    if (snippet == null) {
                        text = NO_RESULTS
                    } else {
                        text = reformatToHtml(snippet, artistName)
                        saveArtistToDataBase(artistName, text)
                    }
                    val urlString = generateUrlString(pageID) // TODO (Rama) No se saca del else ya que no hay que arreglar nada, se refactorizo
                    setUrlToOpenButton(urlString)
                } catch (e1: IOException) {
                    e1.printStackTrace()
                }
            }
            val finalText = text
            // TODO Refactor
            runOnUiThread {
                Picasso.get().load(DEFAULT_WIKIPEDIA_IMAGE).into(imageView)
                textPane2!!.text = Html.fromHtml(finalText)
            }
        }.start()
    }

    private fun setUrlToOpenButton(urlString: String) { //TODO (Rama) - Nombre correcto?
        openUrlButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(urlString)
            startActivity(intent)
        }
    }

    private fun reformatToHtml(snippet: JsonElement, artistName: String?): String {
        var text1 = snippet.asString.replace("\\n", "\n")
        return textToHtml(text1, artistName)
    }

    private fun generateUrlString(pageID: JsonElement): String{
        return "$WIKIPEDIA_URL_PREFIX$pageID"
    }

    private fun getInfoFromService(artistName: String?): JsonObject { //TODO Mejor nivel de abstraccion
        val wikipediaAPI = createWikipediaAPI()
        val callResponse = wikipediaAPI.getArtistInfo(artistName).execute()
        val gson = Gson()
        val jobj = gson.fromJson(callResponse.body(), JsonObject::class.java)
        return jobj["query"].asJsonObject
    }

    private fun JsonObject.getSnippet() = this["search"].asJsonArray[0].asJsonObject["snippet"]

    private fun JsonObject.getPageID() = this["search"].asJsonArray[0].asJsonObject["pageid"]

    private fun getInfoFromLocalDataBase(artistName: String?) = DataBase.getInfo(dataBase, artistName)

    private fun markArtistAsLocal(artist: String) = "[*]$artist" //TODO (Rama) ¿Esta bien que sea artist el parametro? No deberia ser text? Pq nunca se nombra artist en more details

    private fun open(artist: String?) {
        getArtistInfo(artist)
    }

    companion object { // TODO Refactor
        const val ARTIST_NAME_EXTRA = "artistName"
        fun textToHtml(text: String, term: String?): String {
            val builder = StringBuilder()
            builder.append("<html><div width=400>")
            builder.append("<font face=\"arial\">")
            val textWithBold = text
                .replace("'", " ")
                .replace("\n", "<br>")
                .replace(
                    "(?i)$term".toRegex(),
                    "<b>" + term!!.uppercase(Locale.getDefault()) + "</b>"
                )
            builder.append(textWithBold)
            builder.append("</font></div></html>")
            return builder.toString()
        }
    }

    private fun initProperties() {
        textPane2 = findViewById(R.id.textPane2)
        openUrlButton = findViewById(R.id.openUrlButton)
        imageView = findViewById(R.id.imageView)
    }

    private fun createWikipediaAPI(): WikipediaAPI {
        val retrofit = Retrofit.Builder().baseUrl(WIKIPEDIA_URL)
            .addConverterFactory(ScalarsConverterFactory.create()).build()
        return retrofit.create(WikipediaAPI::class.java)
    }

    private fun saveArtistToDataBase(artistName: String?, text: String) {
        DataBase.saveArtist(dataBase, artistName, text)
    }

    private fun existsInLocalDataBase(text: String?) = (text != null)
}