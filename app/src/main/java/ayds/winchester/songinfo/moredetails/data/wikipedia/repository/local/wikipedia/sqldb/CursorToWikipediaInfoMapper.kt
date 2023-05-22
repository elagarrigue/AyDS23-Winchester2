package ayds.winchester.songinfo.moredetails.data.wikipedia.repository.local.wikipedia.sqldb

import android.database.Cursor
import ayds.winchester.songinfo.moredetails.domain.entity.Card.ArtistCard
import java.sql.SQLException

interface CursorToWikipediaInfoMapper {

    fun map(cursor: Cursor): ArtistCard?
}

internal class CursorToWikipediaInfoMapperImpl : CursorToWikipediaInfoMapper {

    override fun map(cursor: Cursor): ArtistCard? =
        try {
            with(cursor) {
                if (moveToNext()) {
                    ArtistCard(
                        description = getString(getColumnIndexOrThrow(COLUMN_INFO)),
                        infoURL = getString(getColumnIndexOrThrow(COLUMN_URL)),
                        isLocallyStored = false
                    )
                } else {
                    null
                }
            }
        } catch (e: SQLException) {
            e.printStackTrace()
            null
        }
}