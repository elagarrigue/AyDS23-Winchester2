package ayds.winchester.songinfo.moredetails.presentation
import ayds.winchester.songinfo.moredetails.domain.entity.Info.ArtistInfo
import ayds.winchester.songinfo.moredetails.domain.entity.Info.EmptyInfo
import ayds.winchester.songinfo.moredetails.domain.repository.WikipediaRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class MoreDetailsPresenterImplTest {

    private val wikipediaRepository = mockk<WikipediaRepository>(relaxUnitFun = true)
    private val infoDescriptionHelper = mockk<InfoDescriptionHelper>(relaxUnitFun = true)
    private val presenter = MoreDetailsPresenterImpl(wikipediaRepository, infoDescriptionHelper)

    @Test
    fun `Given an artist name when getArtistInfo is called then should call the correct methods from the repository and notify the ui state observable with the expected ui state`() {
        // Given
        val artistName = "The Beatles"
        val artistInfo = ArtistInfo("Some info", "Some url")
        val expectedUiState = MoreDetailsUiState("Some info", "Some url", "https://en.wikipedia.org/static/images/project-logos/enwiki.png")
        every { wikipediaRepository.getInfo(artistName) } returns artistInfo
        every { infoDescriptionHelper.getInfoDescriptionText(artistInfo, artistName) } returns "Some info"

        // When
        presenter.createThread(artistName)

        // Then
        verify { wikipediaRepository.getInfo(artistName) }
        verify { infoDescriptionHelper.getInfoDescriptionText(artistInfo, artistName) }
        verify { presenter.uiStateObservable.notify(expectedUiState) }
    }

    @Test
    fun `Given an artist name that returns an empty info when getArtistInfo is called then should notify the ui state observable with the expected ui state`() {
        // Given
        val artistName = "The Beatles"
        val emptyInfo = EmptyInfo
        val expectedUiState = MoreDetailsUiState("", "", "https://en.wikipedia.org/static/images/project-logos/enwiki.png")
        every { wikipediaRepository.getInfo(artistName) } returns emptyInfo

        // When
        presenter.createThread(artistName)

        // Then
        verify { presenter.uiStateObservable.notify(expectedUiState) }
    }
}