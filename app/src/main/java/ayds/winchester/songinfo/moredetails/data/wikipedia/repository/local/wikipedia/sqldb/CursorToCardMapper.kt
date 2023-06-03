package ayds.winchester.songinfo.moredetails.data.wikipedia.repository.local.wikipedia.sqldb

import android.database.Cursor
import ayds.winchester.songinfo.moredetails.domain.entity.Card
import java.sql.SQLException
import ayds.winchester.songinfo.moredetails.domain.entity.Source

interface CursorToCardMapper {

    fun map(cursor: Cursor): List<Card>
}

internal class CursorToCardMapperImpl : CursorToCardMapper {

    override fun map(cursor: Cursor): List<Card> {
        val artistCards = mutableListOf<Card>()
        try {
            with(cursor) {
                while (moveToNext()) {
                    artistCards.add(
                        Card(
                            description = getString(getColumnIndexOrThrow(COLUMN_INFO)),
                            infoURL = getString(getColumnIndexOrThrow(COLUMN_URL)),
                            isLocallyStored = false,
                            source = Source.values()[getInt(getColumnIndexOrThrow(COLUMN_SOURCE))],
                            sourceLogoUrl = getString(getColumnIndexOrThrow(COLUMN_SOURCE_LOGO_URL))
                        )
                    )
                }
            }
        }
        catch (e: SQLException) {
            e.printStackTrace()
        }
        return artistCards
    }

}