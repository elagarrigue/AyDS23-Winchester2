package ayds.winchester.songinfo.moredetails.presentation

import ayds.winchester2.wikipediadata.data.wikipedia.entity.Info.ArtistInfo
import ayds.winchester2.wikipediadata.data.wikipedia.entity.Info.EmptyInfo
import ayds.winchester.songinfo.moredetails.domain.repository.WikipediaRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

private const val ARTIST_NAME_TEST = "The Beatles"
class MoreDetailsPresenterTest {

    private val artistInfo = ArtistInfo("Some info", "Some url")
    private val emptyInfo = EmptyInfo
    private val wikipediaRepository = mockk<WikipediaRepository>(relaxUnitFun = true)
    private val infoDescriptionHelper = mockk<InfoDescriptionHelper>(relaxUnitFun = true)
    private val presenter = MoreDetailsPresenterImpl(wikipediaRepository, infoDescriptionHelper)

    @Test
    fun `Given an artist name when getArtistInfo is called then should call the correct methods from the repository and notify the ui state observable with the expected ui state`() {
        val expectedUiState = MoreDetailsUiState(artistInfoDescription = "Some info", artistInfoUrl = "Some url")
        every { wikipediaRepository.getCards(ARTIST_NAME_TEST) } returns artistInfo
        every { infoDescriptionHelper.getInfoDescriptionText(artistInfo, ARTIST_NAME_TEST) } returns "Some info"

        val infoTester: (MoreDetailsUiState) -> Unit = mockk(relaxed = true)
        presenter.uiStateObservable.subscribe {
            infoTester(it)
        }
        presenter.fetchArtistInfo(ARTIST_NAME_TEST)

        verify { wikipediaRepository.getCards(ARTIST_NAME_TEST) }
        verify { infoDescriptionHelper.getInfoDescriptionText(artistInfo, ARTIST_NAME_TEST) }
        verify { infoTester(expectedUiState) }
    }

    @Test
    fun `Given an artist name that returns an empty info when getArtistInfo is called then should notify the ui state observable with the expected ui state`() {
        val expectedUiState = MoreDetailsUiState(artistInfoDescription = "", artistInfoUrl = "")
        every { wikipediaRepository.getCards(ARTIST_NAME_TEST) } returns emptyInfo

        val infoTester: (MoreDetailsUiState) -> Unit = mockk(relaxed = true)
        presenter.uiStateObservable.subscribe {
            infoTester(it)
        }
        presenter.fetchArtistInfo(ARTIST_NAME_TEST)

        verify { infoTester(expectedUiState) }
    }
}