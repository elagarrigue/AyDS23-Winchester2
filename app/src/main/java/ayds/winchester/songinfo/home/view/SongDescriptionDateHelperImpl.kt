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
}