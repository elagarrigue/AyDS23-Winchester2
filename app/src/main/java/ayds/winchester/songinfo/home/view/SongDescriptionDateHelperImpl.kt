package ayds.winchester.songinfo.home.view


interface SongDescriptionDateHelper{
    fun formatear(date: String): String
}

class SongDescriptionDateHelperDay(): SongDescriptionDateHelper{
    override fun formatear(date: String): String{
        val year = date.subSequence(0,4)
        val month = date.subSequence(5,7)
        val day = date.subSequence(8,10)
        return "$day/$month/$year"
    }
}

class SongDescriptionDateHelperMonth(): SongDescriptionDateHelper{
    override fun formatear(date: String): String{
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
class SongDescriptionDateHelperYear(): SongDescriptionDateHelper{
    override fun formatear(date: String): String{
        val isALeapYear = if(isALeapYear(date)) " (is a leap year)" else " (not a leap year)"
        return date + isALeapYear
    }

    private fun isALeapYear(year: String): Boolean{
        val n = year.toInt()
        return (n % 4 == 0) && (n % 100 != 0 || n % 400 == 0)
    }
}