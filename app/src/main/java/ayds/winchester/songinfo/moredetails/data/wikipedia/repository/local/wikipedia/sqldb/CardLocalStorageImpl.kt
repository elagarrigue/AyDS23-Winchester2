package ayds.winchester.songinfo.moredetails.data.wikipedia.repository.local.wikipedia.sqldb

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import ayds.winchester.songinfo.moredetails.domain.entity.Card
import ayds.winchester.songinfo.moredetails.data.wikipedia.repository.local.wikipedia.CardLocalStorage

private const val DATABASE_VERSION = 1
private const val DATABASE_NAME = "dictionary.db"

internal class CardLocalStorageImpl(
    context: Context,
    private val cursorToWikipediaInfoMapper: CursorToWikipediaInfoMapper
) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION),
    CardLocalStorage {

    private val projection = arrayOf(
        COLUMN_ID,
        COLUMN_ARTIST,
        COLUMN_INFO,
        COLUMN_URL,
        COLUMN_SOURCE_LOGO_URL,
        COLUMN_SOURCE
    )

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    override fun insertCard(artistName: String, artist: Card) {
        val values = ContentValues().apply{
            put(COLUMN_ARTIST, artistName)
            put(COLUMN_INFO, artist.description)
            put(COLUMN_URL, artist.infoURL)
            put(COLUMN_SOURCE_LOGO_URL, artist.sourceLogoUrl)
            put(COLUMN_SOURCE, artist.source.ordinal)
        }
        writableDatabase.insert(TABLE_NAME, null, values)

    }

    override fun getCards(artist: String?): List<Card> {
        val cursor = readableDatabase.query(
            TABLE_NAME,
            projection,
            COLUMNS_FOR_WHERE,
            arrayOf(artist),
            null,
            null,
            SORT_ORDER_CURSOR
        )
        val artistCardList = cursorToWikipediaInfoMapper.map(cursor)
        cursor.close()
        return artistCardList
    }
}