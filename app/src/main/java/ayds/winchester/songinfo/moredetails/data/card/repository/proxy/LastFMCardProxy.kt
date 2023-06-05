package ayds.winchester.songinfo.moredetails.data.card.repository.proxy

import ayds.winchester.songinfo.moredetails.domain.entity.Card
import ayds.winchester.songinfo.moredetails.domain.entity.Source
import lisboa4LastFM.ArtistBiography
import lisboa4LastFM.LastFMService

internal class LastFMCardProxy(private val lastFMService: LastFMService): CardProxy{
    override fun getCard(artistName: String): Card?{
        val lastFmArtistBiography = lastFMService.getArtistBiography(artistName)

        val card: Card? =
            try{
                lastFmArtistBiography?.map()
            }
            catch (e: Exception){
                null
            }
        return card
    }

    private fun ArtistBiography.map() =
        Card(
            description = this.artistInfo,
            infoURL = this.url,
            source = Source.LastFM,
            sourceLogoUrl = ArtistBiography.URL_LAST_FM_IMAGE
        )

}