package ayds.winchester.songinfo.home.view

interface SongDescriptionDateHelper{
    fun formatear(): String
}

class SongDescriptionDateHelperImpl(private val releaseDate: String, private val releaseDatePrecision: String): SongDescriptionDateHelper{
    
    override fun formatear(): String{
        return when(releaseDatePrecision){
            "year" -> formatearYear(releaseDate)
            "month" -> formatearMonth(releaseDate)
            else -> formatearDay(releaseDate)
        }
    }

    private fun isALeapYear(year: String): Boolean{
        val n = year.toInt()
        return (n % 4 == 0) && (n % 100 != 0 || n % 400 == 0)
    }

    private fun formatearYear(date: String): String{
        val isALeapYear = if(isALeapYear(date)) "(is a leap year)" else "(not a leap year)"
        return date + isALeapYear
    }
}