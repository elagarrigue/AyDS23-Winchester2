package ayds.winchester.songinfo.moredetails.presentation

import ayds.winchester.songinfo.moredetails.domain.entity.Card

interface InfoDescriptionHelper {
    fun getInfoDescriptionText(artistInfo: Card, artistName: String = ""): String
}

internal class InfoDescriptionHelperImpl(private val descriptionFormatter: DescriptionFormatter): InfoDescriptionHelper {
    override fun getInfoDescriptionText(artistInfo: Card, artistName: String) =
        "${
            if (artistInfo.isLocallyStored) "[*]" else ""
        }${artistInfo.getFormattedDescription(artistName)}"

    private fun Card.getFormattedDescription(artistName: String) =
        descriptionFormatter.format(this.description, artistName)

}