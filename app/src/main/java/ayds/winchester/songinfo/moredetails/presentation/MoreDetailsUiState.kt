package ayds.winchester.songinfo.moredetails.presentation

data class MoreDetailsUiState(
    val wikipediaDefaultImage: String = DEFAULT_IMAGE,
    val artistInfoDescription: String = NO_RESULTS,
    val artistInfoUrl: String = "",
    val buttonEnabled: Boolean = false,
    val sourceName: String = ""
){
    companion object {
        const val NO_RESULTS = "No Results"
        const val DEFAULT_IMAGE =
            "https://upload.wikimedia.org/wikipedia/commons/8/8c/Wikipedia-logo-v2-es.png"
    }
}
