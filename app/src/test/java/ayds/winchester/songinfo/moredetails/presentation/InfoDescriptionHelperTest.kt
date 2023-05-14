package ayds.winchester.songinfo.moredetails.presentation

import ayds.winchester.songinfo.moredetails.domain.entity.Info.ArtistInfo
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test

class InfoDescriptionHelperImplTest {

    private val descriptionFormatter = mockk<DescriptionFormatter>(relaxUnitFun = true)
    private val helper = InfoDescriptionHelperImpl(descriptionFormatter)

    @Test
    fun `getInfoDescriptionText should return formatted description with local indicator when ArtistInfo is locally stored`() {
        // Given
        val artistName = "The Beatles"
        val description = "The Beatles were an English rock band formed in Liverpool in 1960."
        val artistInfo = ArtistInfo(description,"url", isLocallyStored = true)

        every { descriptionFormatter.format(description, artistName) } returns "Formatted description"

        // When
        val result = helper.getInfoDescriptionText(artistInfo, artistName)

        // Then
        assertEquals("[*]Formatted description", result)
    }

    @Test
    fun `getInfoDescriptionText should return formatted description when ArtistInfo is not locally stored`() {
        // Given
        val artistName = "The Beatles"
        val description = "The Beatles were an English rock band formed in Liverpool in 1960."
        val artistInfo = ArtistInfo(description,"url",isLocallyStored = false)

        every { descriptionFormatter.format(description, artistName) } returns "Formatted description"

        // When
        val result = helper.getInfoDescriptionText(artistInfo, artistName)

        // Then
        assertEquals("Formatted description", result)
    }
}
