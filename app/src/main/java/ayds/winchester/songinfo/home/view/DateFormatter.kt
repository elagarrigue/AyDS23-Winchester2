package ayds.winchester.songinfo.home.view

import ayds.winchester.songinfo.home.model.entities.Song

class DateFormatterFactory {
    fun get(song: Song.SpotifySong) =
        when (song.releaseDatePrecision) {
            "year" -> YearFormatter(song.releaseDate)
            "month" -> MonthFormatter(song.releaseDate)
            else -> DayFormatter(song.releaseDate)
        }
}
sealed class DateFormatter(val releaseDate: String){
    abstract fun formatear(): String
}

class DayFormatter(releaseDate: String): DateFormatter(releaseDate){
    override fun formatear(): String{
        val year = releaseDate.subSequence(0,4)
        val month = releaseDate.subSequence(5,7)
        val day = releaseDate.subSequence(8,10)
        return "$day/$month/$year"
    }
}

class MonthFormatter(releaseDate: String): DateFormatter(releaseDate){
    override fun formatear(): String{
        val result = when(releaseDate.subSequence(5,7)){
            "01" -> "January, "
            "02" -> "February, "
            "03" -> "March, "
            "04" -> "April, "
            "05" -> "May, "
            "06" -> "June, "
            "07" -> "July, "
            "08" -> "August, "
            "09" -> "September, "
            "10" -> "October, "
            "11" -> "November, "
            "12" -> "December, "
            else -> ""
        }
        return result + releaseDate.subSequence(0,4)
    }
}
class YearFormatter(releaseDate: String): DateFormatter(releaseDate){
    override fun formatear(): String{
        val isALeapYear = if(isALeapYear(releaseDate)) " (is a leap year)" else " (not a leap year)"
        return releaseDate + isALeapYear
    }

    private fun isALeapYear(year: String): Boolean{
        val n = year.toInt()
        return (n % 4 == 0) && (n % 100 != 0 || n % 400 == 0)
    }
}