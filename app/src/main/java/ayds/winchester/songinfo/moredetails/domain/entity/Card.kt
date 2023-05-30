package ayds.winchester.songinfo.moredetails.domain.entity

enum class Source {
    Wikipedia,
    NYTimes,
    LastFM,
    NotFound
}

data class Card(
    var description: String = "No results",
    var infoURL: String = "No results",
    var source: Source = Source.NotFound,
    var sourceLogoUrl: String = "No results",
    var isLocallyStored: Boolean = false,
)