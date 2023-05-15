package ayds.winchester.songinfo.moredetails.presentation

import ayds.winchester.songinfo.moredetails.domain.entity.Info.ArtistInfo

interface InfoDescriptionHelper {
    fun getInfoDescriptionText(artistInfo: ArtistInfo, artistName: String = ""): String
}

internal class InfoDescriptionHelperImpl(private val descriptionFormatter: DescriptionFormatter): InfoDescriptionHelper {
    override fun getInfoDescriptionText(artistInfo: ArtistInfo, artistName: String) =
        "${
            if (artistInfo.isLocallyStored) "[*]" else ""
        }${artistInfo.getFormattedDescription(artistName)}"

    private fun ArtistInfo.getFormattedDescription(artistName: String) =
        descriptionFormatter.format(this.description, artistName)

}