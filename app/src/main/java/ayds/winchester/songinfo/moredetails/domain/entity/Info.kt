package ayds.winchester.songinfo.moredetails.domain.entity

sealed class Info {

    data class ArtistInfo(
        var description: String,
        var wikipediaURL: String,
        var isLocallyStored: Boolean = false,
    ): Info()

    object EmptyInfo: Info()
}