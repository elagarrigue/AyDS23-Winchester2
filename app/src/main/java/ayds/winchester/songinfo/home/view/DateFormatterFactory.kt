package ayds.winchester.songinfo.home.view

import ayds.winchester.songinfo.home.model.entities.Song

private const val YEAR = "year"
private const val MONTH = "month"
interface DateFormatterFactory {
    fun get(song: Song.SpotifySong): DateFormatter
}

class DateFormatterFactoryImpl : DateFormatterFactory {
    override fun get(song: Song.SpotifySong) = when (song.releaseDatePrecision) {
        YEAR -> YearFormatter(song.releaseDate)
        MONTH -> MonthFormatter(song.releaseDate)
        else -> DayFormatter(song.releaseDate)
    }
}