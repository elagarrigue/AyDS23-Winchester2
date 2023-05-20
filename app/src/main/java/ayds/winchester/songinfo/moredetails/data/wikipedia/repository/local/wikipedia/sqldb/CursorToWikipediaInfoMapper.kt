package ayds.winchester.songinfo.moredetails.data.wikipedia.repository.local.wikipedia.sqldb

import android.database.Cursor
import ayds.winchester2.wikipediaexternal.data.wikipedia.entity.Info.ArtistInfo
import java.sql.SQLException

interface CursorToWikipediaInfoMapper {

    fun map(cursor: Cursor): ArtistInfo?
}

internal class CursorToWikipediaInfoMapperImpl : CursorToWikipediaInfoMapper {

    override fun map(cursor: Cursor): ArtistInfo? =
        try {
            with(cursor) {
                if (moveToNext()) {
                    ArtistInfo(
                        description = getString(getColumnIndexOrThrow(COLUMN_INFO)),
                        wikipediaURL = getString(getColumnIndexOrThrow(COLUMN_URL)),
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