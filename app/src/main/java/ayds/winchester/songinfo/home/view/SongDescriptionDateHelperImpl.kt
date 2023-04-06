package ayds.winchester.songinfo.home.view

import ayds.winchester.songinfo.home.model.entities.Song.SpotifySong

interface SongDescriptionDateHelper{
    fun formatear(): String
}

class SongDescriptionDateHelperImpl(private val song: SpotifySong): SongDescriptionDateHelper{

    override fun formatear(): String{
        return when(song.releaseDatePrecision){
            "year" -> formatearYear(song.releaseDate)
            "month" -> formatearMonth(song.releaseDate)
            else -> formatearDay(song.releaseDate)
        }
    }

    private fun isALeapYear(year: String): Boolean{
        val n = year.toInt()
        return (n % 4 == 0) && (n % 100 != 0 || n % 400 == 0)
    }

    private fun formatearDay(date: String): String{
        val year = date.subSequence(0,4)
        val month = date.subSequence(5,7)
        val day = date.subSequence(8,10)
        return "$day/$month/$year"
    }

    private fun formatearYear(date: String): String{
        val isALeapYear = if(isALeapYear(date)) " (is a leap year)" else " (not a leap year)"
        return date + isALeapYear
    }

    private fun formatearMonth(date: String): String{
        val result = when(date.subSequence(5,7)){
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
        return result + date.subSequence(0,4)
    }
}