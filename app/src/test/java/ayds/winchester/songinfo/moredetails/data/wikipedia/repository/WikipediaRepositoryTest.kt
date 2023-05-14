package ayds.winchester.songinfo.moredetails.data.wikipedia.repository
import ayds.winchester.songinfo.moredetails.domain.entity.Info.EmptyInfo
import ayds.winchester.songinfo.moredetails.domain.entity.Info.ArtistInfo
import ayds.winchester.songinfo.moredetails.data.wikipedia.repository.external.wikipedia.WikipediaTrackService
import ayds.winchester.songinfo.moredetails.data.wikipedia.repository.local.wikipedia.WikipediaLocalStorage
import ayds.winchester.songinfo.moredetails.domain.repository.WikipediaRepository
import io.mockk.*
import org.junit.Assert.assertEquals
import org.junit.Test

class WikipediaRepositoryTest {

    private val wikipediaLocalStorage: WikipediaLocalStorage = mockk(relaxUnitFun = true)
    private val wikipediaTrackService: WikipediaTrackService = mockk(relaxUnitFun = true)
    private val wikipediaRepository: WikipediaRepository = by lazy {
        WikipediaRepositoryImpl(wikipediaLocalStorage, wikipediaTrackService)
    }

    @Test
    fun testGetInfo_LocalStorageReturnsInfo() {
        // Given
        val artist = "The Beatles"
        val info = ArtistInfo("The Beatles", "The Beatles were an English rock band formed in Liverpool in 1960.")
        every { wikipediaLocalStorage.getInfo(artist) } returns info

        // When
        val result = wikipediaRepository.getInfo(artist)

        // Then
        assertEquals(info, result)
    }

    @Test
    fun testGetInfo_ServiceReturnsInfo() {
        // Given
        val artist = "The Beatles"
        val info = ArtistInfo("The Beatles", "The Beatles were an English rock band formed in Liverpool in 1960.")
        every { wikipediaLocalStorage.getInfo(artist) } returns null
        every { wikipediaTrackService.getInfo(artist) } returns info
        every { wikipediaLocalStorage.insertInfo(artist, info) } just Runs

        // When
        val result = wikipediaRepository.getInfo(artist)

        // Then
        assertEquals(info, result)
        verify(exactly = 1) { wikipediaLocalStorage.insertInfo(artist, info) }
    }

    @Test
    fun testGetInfo_ServiceThrowsException() {
        // Given
        val artist = "The Beatles"
        every { wikipediaLocalStorage.getInfo(artist) } returns null
        every { wikipediaTrackService.getInfo(artist) } throws Exception()

        // When
        val result = wikipediaRepository.getInfo(artist)

        // Then
        assertEquals(EmptyInfo, result)
    }

}
