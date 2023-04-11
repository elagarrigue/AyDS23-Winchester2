package ayds.winchester.songinfo.home.view

import ayds.winchester.songinfo.home.model.entities.Song

interface DateFormatterFactory {
    fun get(song: Song.SpotifySong): DateFormatter
}

class DateFormatterFactoryImpl : DateFormatterFactory {
    override fun get(song: Song.SpotifySong) = when (song.releaseDatePrecision) {
        "year" -> YearFormatter(song.releaseDate)
        "month" -> MonthFormatter(song.releaseDate)
        else -> DayFormatter(song.releaseDate)
    }
}