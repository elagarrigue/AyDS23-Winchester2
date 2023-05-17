package ayds.winchester.songinfo.moredetails.presentation

import ayds.winchester.songinfo.moredetails.domain.entity.Info.ArtistInfo
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test

const val ARTIST_NAME = "The Beatles"
const val DESCRIPTION = "The Beatles were an English rock band formed in Liverpool in 1960."
class InfoDescriptionHelperImplTest {

    private val descriptionFormatter = mockk<DescriptionFormatter>(relaxUnitFun = true)
    private val helper = InfoDescriptionHelperImpl(descriptionFormatter)

    @Test
    fun `getInfoDescriptionText should return formatted description with local indicator when ArtistInfo is locally stored`() {
        val artistInfo = ArtistInfo(DESCRIPTION,"url", isLocallyStored = true)

        every { descriptionFormatter.format(DESCRIPTION, ARTIST_NAME) } returns "Formatted description"

        val result = helper.getInfoDescriptionText(artistInfo, ARTIST_NAME)

        assertEquals("[*]Formatted description", result)
    }

    @Test
    fun `getInfoDescriptionText should return formatted description when ArtistInfo is not locally stored`() {
        val artistInfo = ArtistInfo(DESCRIPTION,"url",isLocallyStored = false)

        every { descriptionFormatter.format(DESCRIPTION, ARTIST_NAME) } returns "Formatted description"

        val result = helper.getInfoDescriptionText(artistInfo, ARTIST_NAME)

        assertEquals("Formatted description", result)
    }
}
