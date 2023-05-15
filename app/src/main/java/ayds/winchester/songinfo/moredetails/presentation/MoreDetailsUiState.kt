package ayds.winchester.songinfo.moredetails.presentation

data class MoreDetailsUiState(
    val wikipediaDefaultImage: String = DEFAULT_IMAGE,
    val artistInfoDescription: String = "",
    val artistInfoUrl: String = ""
){
    companion object {
        const val DEFAULT_IMAGE =
            "https://upload.wikimedia.org/wikipedia/commons/8/8c/Wikipedia-logo-v2-es.png"
    }
}
