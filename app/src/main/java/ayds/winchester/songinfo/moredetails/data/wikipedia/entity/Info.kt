package ayds.winchester.songinfo.moredetails.data.wikipedia.entity

sealed class Info {

    data class ArtistInfo(
        var id: String,
        var name: String,
        var description: String,
        var wikipediaURL: String,
        var isLocallyStored: Boolean = false,
    ): Info()

    object EmptyInfo: Info()
}