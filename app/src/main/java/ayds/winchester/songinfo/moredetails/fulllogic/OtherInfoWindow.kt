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
import com.google.gson.JsonObject
import com.squareup.picasso.Picasso
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.IOException
import java.util.*

private const val WIKIPEDIA_URL = "https://en.wikipedia.org/w/"
private const val DEFAULT_WIKIPEDIA_IMAGE =
    "https://upload.wikimedia.org/wikipedia/commons/8/8c/Wikipedia-logo-v2-es.png"
private const val WIKIPEDIA_URL_PREFIX = "https://en.wikipedia.org/?curid="

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

        val wikipediaAPI = createWikipediaAPI()
        Thread {
            var text = DataBase.getInfo(dataBase, artistName)
            if (existsInLocalDataBase(text)) text = markArtistAsLocal(text)
            else {
                val callResponse: Response<String>
                try {
                    callResponse = wikipediaAPI.getArtistInfo(artistName).execute()
                    val gson = Gson()
                    val jobj = gson.fromJson(callResponse.body(), JsonObject::class.java)
                    val query = jobj["query"].asJsonObject
                    val snippet = query["search"].asJsonArray[0].asJsonObject["snippet"]
                    val pageid = query["search"].asJsonArray[0].asJsonObject["pageid"]
                    if (snippet == null) {
                        text = "No Results"
                    } else {
                        text = snippet.asString.replace("\\n", "\n")
                        text = textToHtml(text, artistName)
                        saveArtistToDataBase(artistName, text)
                    }
                    val urlString = "$WIKIPEDIA_URL_PREFIX$pageid"
                    openUrlButton.setOnClickListener {
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.data = Uri.parse(urlString)
                        startActivity(intent)
                    }
                } catch (e1: IOException) {
                    e1.printStackTrace()
                }
            }
            val finalText = text
            runOnUiThread {
                Picasso.get().load(DEFAULT_WIKIPEDIA_IMAGE).into(imageView)
                textPane2!!.text = Html.fromHtml(finalText)
            }
        }.start()
    }

    private fun markArtistAsLocal(artist: String) = "[*]$artist"

    private fun open(artist: String?) {
        getArtistInfo(artist)
    }

    companion object {
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