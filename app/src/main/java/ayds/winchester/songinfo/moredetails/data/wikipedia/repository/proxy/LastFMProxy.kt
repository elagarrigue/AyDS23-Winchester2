package ayds.winchester.songinfo.moredetails.data.wikipedia.repository.proxy

import ayds.winchester.songinfo.moredetails.domain.entity.Card
import ayds.winchester.songinfo.moredetails.domain.entity.Source
import lisboa4LastFM.ArtistBiography
import lisboa4LastFM.LastFMService

internal class LastFMProxy(private val lastFMService: LastFMService): Proxy{
    override fun getCard(artistName: String) = lastFMService.getArtistBiography(artistName)?.map()

    private fun ArtistBiography.map() =
        Card(
            description = this.artistInfo,
            infoURL = this.url,
            source = Source.LastFM,
            sourceLogoUrl = ArtistBiography.URL_LAST_FM_IMAGE
        )

}