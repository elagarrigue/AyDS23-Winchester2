package ayds.winchester.songinfo.moredetails.data.wikipedia.repository.external.wikipedia.tracks

import ayds.winchester.songinfo.moredetails.domain.entity.Info.ArtistInfo
import com.google.gson.Gson
import com.google.gson.JsonObject

interface WikipediaToInfoResolver {
    fun getInfoFromExternalData(serviceData: String?): ArtistInfo?
}

private const val WIKIPEDIA_URL_PREFIX = "https://en.wikipedia.org/?curid="
private const val SEARCH = "search"
private const val SNIPPET = "snippet"
private const val PAGE_ID = "pageid"
private const val QUERY = "query"

internal class JsonToInfoResolver : WikipediaToInfoResolver {

    override fun getInfoFromExternalData(serviceData: String?): ArtistInfo? =
        try {
            serviceData?.getFirstItem()?.let { item ->
                ArtistInfo(
                    item.getSnippet(),
                    item.getURL()
                )
            }
        } catch (e: Exception) {
            null
        }


    private fun String?.getFirstItem(): JsonObject {
        val jsonObject = Gson().fromJson(this, JsonObject::class.java)
        return jsonObject[QUERY].asJsonObject
    }

    private fun JsonObject.getSnippet() = this[SEARCH].asJsonArray[0].asJsonObject[SNIPPET].asString

    private fun JsonObject.getURL() = "$WIKIPEDIA_URL_PREFIX${this.getPageID()}"

    private fun JsonObject.getPageID() = this[SEARCH].asJsonArray[0].asJsonObject[PAGE_ID]
}