package ayds.winchester.songinfo.moredetails.data.wikipedia.repository

import ayds.winchester.songinfo.moredetails.domain.entity.Info.EmptyInfo
import ayds.winchester.songinfo.moredetails.domain.entity.Info.ArtistInfo
import ayds.winchester.songinfo.moredetails.data.wikipedia.repository.external.wikipedia.WikipediaTrackService
import ayds.winchester.songinfo.moredetails.data.wikipedia.repository.local.wikipedia.WikipediaLocalStorage
import ayds.winchester.songinfo.moredetails.domain.repository.WikipediaRepository
import io.mockk.*
import org.junit.Assert.*
import org.junit.Test
import java.lang.Exception

class WikipediaRepositoryTest {

    private val wikipediaLocalStorage: WikipediaLocalStorage = mockk(relaxUnitFun = true)
    private val wikipediaTrackService: WikipediaTrackService = mockk(relaxUnitFun = true)
    private val wikipediaRepository: WikipediaRepository by lazy {
        WikipediaRepositoryImpl(wikipediaLocalStorage, wikipediaTrackService)
    }

    @Test
    fun `given existing artist should return info and mark it as local`() {
        // Given
        val artist = "artist"
        val info = ArtistInfo("description", "url")
        every { wikipediaLocalStorage.getInfo(artist) } returns info

        // When
        val result = wikipediaRepository.getInfo(artist)

        // Then
        assertEquals(info, result)
        assertTrue(info.isLocallyStored)
    }

    @Test
    fun `given non existing artist should get the info and store it`() {
        val artist = "artist"
        val info = ArtistInfo("description", "url")
        every { wikipediaLocalStorage.getInfo(artist) } returns null
        every { wikipediaTrackService.getInfo(artist) } returns info
        every { wikipediaLocalStorage.insertInfo(artist, info) } just Runs

        // When
        val result = wikipediaRepository.getInfo(artist)

        // Then
        assertEquals(info, result)
        assertFalse(info.isLocallyStored)
        verify { wikipediaLocalStorage.insertInfo(artist, info) }
    }

    @Test
    fun `given service exception should return empty info`() {
        // Given
        val artist = "artist"
        every { wikipediaLocalStorage.getInfo(artist) } returns null
        every { wikipediaTrackService.getInfo(artist) } throws mockk<Exception>()

        // When
        val result = wikipediaRepository.getInfo(artist)

        // Then
        assertEquals(EmptyInfo, result)
    }

}
