package ayds.winchester.songinfo.moredetails.domain.entity

enum class Source {
    Wikipedia,
    NYTimes,
    LastFM,
    NotFound
}

data class Card(
    val description: String,
    val infoURL: String,
    val source: Source = Source.NotFound,
    val sourceLogoUrl: String,
    var isLocallyStored: Boolean = false,
)