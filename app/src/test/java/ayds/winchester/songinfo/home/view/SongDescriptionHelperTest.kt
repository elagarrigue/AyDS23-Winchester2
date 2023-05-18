package ayds.winchester.songinfo.home.view

import ayds.winchester.songinfo.home.model.entities.Song
import ayds.winchester.songinfo.home.model.entities.Song.SpotifySong
import io.mockk.mockk
import io.mockk.every
import org.junit.Assert
import org.junit.Test

class SongDescriptionHelperTest {
    private val mockDateFormatterFactory = mockk<DateFormatterFactory>(relaxUnitFun = true){
        every { getReleaseDate(any()) } returns DayFormatter("1992-01-01")
    }
    private val songDescriptionHelper by lazy { SongDescriptionHelperImpl(mockDateFormatterFactory) }

    @Test
    fun `given a local song it should return the description`() {
        val song: Song = SpotifySong(
            "id",
            "Plush",
            "Stone Temple Pilots",
            "Core",
            "1992-01-01",
            "day",
            "url",
            "url",
            true,
        )

        val result = songDescriptionHelper.getSongDescriptionText(song)

        val expected =
            "Song: Plush [*]\n" +
                    "Artist: Stone Temple Pilots\n" +
                    "Album: Core\n" +
                    "Release date: 01/01/1992"

        Assert.assertEquals(expected, result)
    }

    @Test
    fun `given a non local song it should return the description`() {
        val song: Song = SpotifySong(
            "id",
            "Plush",
            "Stone Temple Pilots",
            "Core",
            "1992-01-01",
            "day",
            "url",
            "url",
            false,
        )

        val result = songDescriptionHelper.getSongDescriptionText(song)

        val expected =
            "Song: Plush \n" +
                    "Artist: Stone Temple Pilots\n" +
                    "Album: Core\n" +
                    "Release date: 01/01/1992"

        Assert.assertEquals(expected, result)
    }

    @Test
    fun `given a non spotify song it should return the song not found description`() {
        val song: Song = mockk()

        val result = songDescriptionHelper.getSongDescriptionText(song)

        val expected = "Song not found"

        Assert.assertEquals(expected, result)
    }
}