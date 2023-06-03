package ayds.winchester.songinfo.moredetails.data.card.repository

import ayds.winchester2.wikipediadata.data.wikipedia.entity.Info.EmptyInfo
import ayds.winchester2.wikipediadata.data.wikipedia.entity.Info.ArtistInfo
import ayds.winchester2.wikipediadata.data.wikipedia.WikipediaTrackService
import ayds.winchester.songinfo.moredetails.data.card.repository.local.card.CardLocalStorage
import ayds.winchester.songinfo.moredetails.domain.repository.CardRepository
import io.mockk.*
import org.junit.Assert.*
import org.junit.Test
import java.lang.Exception

private const val ARTIST = "artist"
class CardRepositoryTest {

    private val cardLocalStorage: CardLocalStorage = mockk(relaxUnitFun = true)
    private val wikipediaTrackService: ayds.winchester2.wikipediadata.data.wikipedia.WikipediaTrackService = mockk(relaxUnitFun = true)
    private val cardRepository: CardRepository by lazy {
        CardRepositoryImpl(cardLocalStorage, wikipediaTrackService)
    }
    private val info = ArtistInfo("description","url")

    @Test
    fun `given existing artist should return info and mark it as local`() {
        every { cardLocalStorage.getCards(ARTIST) } returns info

        val result = cardRepository.getCards(ARTIST)

        assertEquals(info, result)
        assertTrue(info.isLocallyStored)
    }

    @Test
    fun `given non existing artist should get the info and store it`() {
        every { cardLocalStorage.getCards(ARTIST) } returns null
        every { wikipediaTrackService.getInfo(ARTIST) } returns info

        val result = cardRepository.getCards(ARTIST)

        assertEquals(info, result)
        assertFalse(info.isLocallyStored)
        verify { cardLocalStorage.insertCard(ARTIST, info) }
    }

    @Test
    fun `given service exception should return empty info`() {
        every { cardLocalStorage.getCards(ARTIST) } returns null
        every { wikipediaTrackService.getInfo(ARTIST) } throws mockk<Exception>()

        val result = cardRepository.getCards(ARTIST)

        assertEquals(EmptyInfo, result)
    }

}
