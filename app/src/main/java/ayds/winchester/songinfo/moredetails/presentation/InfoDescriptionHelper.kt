package ayds.winchester.songinfo.moredetails.presentation

import ayds.winchester.songinfo.moredetails.domain.entity.Info
import ayds.winchester.songinfo.moredetails.domain.entity.Info.ArtistInfo
import ayds.winchester.songinfo.moredetails.domain.entity.Info.EmptyInfo

interface InfoDescriptionHelper {
    fun getInfoDescriptionText(info: Info = EmptyInfo, artistName: String = ""): String
}

internal class InfoDescriptionHelperImpl(private val htmlFormatter: HtmlFormatter): InfoDescriptionHelper {
    override fun getInfoDescriptionText(info: Info, artistName: String): String {
        return when (info) {
            is ArtistInfo ->
                "${
                    if (info.isLocallyStored) "[*]" else ""
                }${info.getFormattedDescription(artistName)}"
            else -> "No Results"
        }
    }

    private fun ArtistInfo.getFormattedDescription(artistName: String) =
        htmlFormatter.reformatToHtml(this.description, artistName)

}