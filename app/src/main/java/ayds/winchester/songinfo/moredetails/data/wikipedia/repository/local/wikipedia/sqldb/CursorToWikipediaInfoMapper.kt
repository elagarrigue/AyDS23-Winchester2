package ayds.winchester.songinfo.moredetails.data.wikipedia.repository.local.wikipedia.sqldb

import android.database.Cursor
import ayds.winchester.songinfo.moredetails.domain.entity.Card.ArtistCard
import java.sql.SQLException
import javax.xml.transform.Source

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
                        isLocallyStored = false,
                        // TODO Source
                        sourceLogoUrl = getString(getColumnIndexOrThrow(COLUMN_SOURCE_LOGO_URL))
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