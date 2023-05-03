package ayds.winchester.songinfo.moredetails.data.wikipedia.repository.local.wikipedia.sqldb

import android.database.Cursor
import ayds.winchester.songinfo.moredetails.data.wikipedia.entity.Artist
import java.sql.SQLException

interface CursorToWikipediaInfoMapper {

    fun map(cursor: Cursor): Artist?
}

internal class CursorToWikipediaInfoMapperImpl : CursorToWikipediaInfoMapper {

    override fun map(cursor: Cursor): Artist? =
        try {
            with(cursor) {
                if (moveToNext()) {
                    Artist(
                        id = getString(getColumnIndexOrThrow(COLUMN_ID)),
                        name = getString(getColumnIndexOrThrow(COLUMN_ARTIST)),
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