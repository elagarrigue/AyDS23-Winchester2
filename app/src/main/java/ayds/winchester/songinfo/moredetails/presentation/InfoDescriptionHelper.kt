package ayds.winchester.songinfo.moredetails.presentation

interface InfoDescriptionHelper {
    fun getInfoDescriptionText(artistState: MoreDetailsUiState, artistName: String = ""): String
}

internal class InfoDescriptionHelperImpl(private val htmlFormatter: HtmlFormatter): InfoDescriptionHelper {
    override fun getInfoDescriptionText(artistState: MoreDetailsUiState, artistName: String) =
        "${
            if (artistState.isLocallyStored) "[*]" else ""
        }${artistState.getFormattedDescription(artistName)}"

    private fun MoreDetailsUiState.getFormattedDescription(artistName: String) =
        htmlFormatter.reformatToHtml(this.artistInfoDescription, artistName)

}