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

const val ARTIST = "artist"
class WikipediaRepositoryTest {

    private val wikipediaLocalStorage: WikipediaLocalStorage = mockk(relaxUnitFun = true)
    private val wikipediaTrackService: WikipediaTrackService = mockk(relaxUnitFun = true)
    private val wikipediaRepository: WikipediaRepository by lazy {
        WikipediaRepositoryImpl(wikipediaLocalStorage, wikipediaTrackService)
    }
    private val info = ArtistInfo("description","url")

    @Test
    fun `given existing artist should return info and mark it as local`() {
        every { wikipediaLocalStorage.getInfo(ARTIST) } returns info

        val result = wikipediaRepository.getInfo(ARTIST)

        assertEquals(info, result)
        assertTrue(info.isLocallyStored)
    }

    @Test
    fun `given non existing artist should get the info and store it`() {
        every { wikipediaLocalStorage.getInfo(ARTIST) } returns null
        every { wikipediaTrackService.getInfo(ARTIST) } returns info

        val result = wikipediaRepository.getInfo(ARTIST)

        assertEquals(info, result)
        assertFalse(info.isLocallyStored)
        verify { wikipediaLocalStorage.insertInfo(ARTIST, info) }
    }

    @Test
    fun `given service exception should return empty info`() {
        every { wikipediaLocalStorage.getInfo(ARTIST) } returns null
        every { wikipediaTrackService.getInfo(ARTIST) } throws mockk<Exception>()

        val result = wikipediaRepository.getInfo(ARTIST)

        assertEquals(EmptyInfo, result)
    }

}
