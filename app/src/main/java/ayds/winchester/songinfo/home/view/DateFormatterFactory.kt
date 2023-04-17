package ayds.winchester.songinfo.home.view

import ayds.winchester.songinfo.home.model.entities.Song

private const val YEAR = "year"
private const val MONTH = "month"
private const val DAY = "day"
interface DateFormatterFactory {
    fun getReleaseDate(song: Song.SpotifySong): DateFormatter
}

class DateFormatterFactoryImpl : DateFormatterFactory {
    override fun getReleaseDate(song: Song.SpotifySong) = when (song.releaseDatePrecision) {
        YEAR -> YearFormatter(song.releaseDate)
        MONTH -> MonthFormatter(song.releaseDate)
        DAY -> DayFormatter(song.releaseDate)
        else -> DefaultFormatter(song.releaseDate)
    }
}