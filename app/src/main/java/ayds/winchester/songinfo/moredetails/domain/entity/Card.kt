package ayds.winchester.songinfo.moredetails.domain.entity

enum class Source {
    Wikipedia
}

sealed class Card {

    data class ArtistCard(
        var description: String,
        var infoURL: String,
        var source: Source = Source.Wikipedia,
        var sourceUrl: String = "",
        var isLocallyStored: Boolean = false,
    ): Card()

    object EmptyCard: Card()
}