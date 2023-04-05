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

    private fun formatearMonth(date: String): String{
        var result = when(date.subSequence(5,7)){
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