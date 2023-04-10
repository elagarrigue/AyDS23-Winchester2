package ayds.winchester.songinfo.home.model.entities
import ayds.winchester.songinfo.home.view.SongDescriptionDateHelper
import ayds.winchester.songinfo.home.view.SongDescriptionDateHelperDay
import ayds.winchester.songinfo.home.view.SongDescriptionDateHelperMonth
import ayds.winchester.songinfo.home.view.SongDescriptionDateHelperYear

sealed class Song {

    data class SpotifySong(
        val id: String,
        val songName: String,
        val artistName: String,
        val albumName: String,
        val releaseDate: String,
        val releaseDatePrecision: String,
        val spotifyUrl: String,
        val imageUrl: String,
        var isLocallyStored: Boolean = false
    ) : Song() {
        private val formateador: SongDescriptionDateHelper = when(releaseDatePrecision){
            "year" -> SongDescriptionDateHelperYear()
            "month" -> SongDescriptionDateHelperMonth()
            else -> SongDescriptionDateHelperDay()
        }
        val releaseFormateado: String = formateador.formatear(releaseDate)
    }

    object EmptySong : Song()
}