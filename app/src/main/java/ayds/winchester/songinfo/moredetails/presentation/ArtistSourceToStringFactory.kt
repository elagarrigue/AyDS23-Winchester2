package ayds.winchester.songinfo.moredetails.presentation

import ayds.winchester.songinfo.moredetails.domain.entity.Source

interface ArtistSourceToStringFactory {
    fun get(source: Source): String
}

internal class ArtistSourceToStringFactoryImpl : ArtistSourceToStringFactory {
    override fun get(source: Source): String =
        when (source){
            Source.LastFM -> LastFMSourceToStringMapper().map()
            Source.NYTimes -> NewYorkTimesSourceToStringMapper().map()
            Source.Wikipedia -> WikipediaSourceToStringMapper().map()
            Source.NotFound -> EmptySourceToStringMapper().map()
        }
}

interface SourceToStringMapper {
    fun map(): String
}

internal class LastFMSourceToStringMapper: SourceToStringMapper {
    override fun map() =
        "Last FM"
}

internal class NewYorkTimesSourceToStringMapper: SourceToStringMapper {
    override fun map() =
        "The New York Times"
}

internal class WikipediaSourceToStringMapper: SourceToStringMapper {
    override fun map() =
        "Wikipedia"
}

internal class EmptySourceToStringMapper: SourceToStringMapper {
    override fun map() =
        "No results"
}