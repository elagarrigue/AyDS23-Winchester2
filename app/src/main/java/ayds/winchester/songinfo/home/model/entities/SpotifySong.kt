package ayds.winchester.songinfo.home.model.entities
import ayds.winchester.songinfo.home.view.*

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
        private val formateador: DateFormatter = DateFormatterFactory.get(this)
        val releaseFormateado: String = formateador.formatear()
    }

    object EmptySong : Song()
}