package ayds.winchester.songinfo.moredetails.data.wikipedia.entity

data class Artist(
    var id: String,
    var name: String,
    var description: String,
    var wikipediaURL: String,
    var isLocallyStored: Boolean = false,
)