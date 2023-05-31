package ayds.winchester.songinfo.moredetails.data.wikipedia.repository.proxy

import ayds.aknewyork.external.service.data.NYTimesService
import ayds.aknewyork.external.service.data.entities.ArtistDataExternal
import ayds.aknewyork.external.service.data.entities.NYT_LOGO_URL
import ayds.winchester.songinfo.moredetails.domain.entity.Card
import ayds.winchester.songinfo.moredetails.domain.entity.Source

internal class NYTimesProxy(private val nyTimesService: NYTimesService): Proxy {
    override fun getCard(artistName: String) = nyTimesService.getArtistInfo(artistName).map()

    private fun ArtistDataExternal.map() =
        when(this){
            is ArtistDataExternal.ArtistWithDataExternal -> Card(
                description = this.info?:"",
                infoURL = this.url,
                source = Source.NYTimes,
                sourceLogoUrl = NYT_LOGO_URL           )
            else -> null
        }

}