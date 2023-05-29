package ayds.winchester.songinfo.moredetails.domain.entity

enum class Source {
    Wikipedia,
    NYTimes,
    LastFM
}

data class Card(
    var description: String,
    var infoURL: String,
    var source: Source = Source.Wikipedia,
    var sourceLogoUrl: String = "",
    var isLocallyStored: Boolean = false,
)