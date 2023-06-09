package ayds.winchester.songinfo.home.view

private const val DAY = 2
private const val MONTH = 1
private const val YEAR = 0

interface DateFormatter {
     fun format(): String
}

internal class DayFormatter(private val releaseDate: String) : DateFormatter {
    override fun format(): String {
        val yearMonthDay = this.releaseDate.split("-")
        return "${yearMonthDay[DAY]}/${yearMonthDay[MONTH]}/${yearMonthDay[YEAR]}"
    }
}

internal class MonthFormatter(private val releaseDate: String) : DateFormatter {
    override fun format(): String {
        val result = when (releaseDate.subSequence(5, 7)) {
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
        return result + releaseDate.subSequence(0, 4)
    }
}

internal class YearFormatter(private val releaseDate: String) : DateFormatter {
    override fun format() =
        "$releaseDate ${if (isALeapYear(releaseDate)) "(is a leap year)" else "(not a leap year)"}"

    private fun isALeapYear(year: String): Boolean {
        val n = year.toInt()
        return (n % 4 == 0) && (n % 100 != 0 || n % 400 == 0)
    }
}

internal class DefaultFormatter(private val releaseDate: String) : DateFormatter {
    override fun format() = releaseDate
}