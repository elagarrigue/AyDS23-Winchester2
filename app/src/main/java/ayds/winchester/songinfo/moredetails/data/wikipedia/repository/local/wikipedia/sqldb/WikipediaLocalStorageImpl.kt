package ayds.winchester.songinfo.moredetails.data.wikipedia.repository.local.wikipedia.sqldb

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import ayds.winchester.songinfo.moredetails.domain.entity.Info.ArtistInfo
import ayds.winchester.songinfo.moredetails.data.wikipedia.repository.local.wikipedia.WikipediaLocalStorage

private const val DATABASE_VERSION = 1
private const val DATABASE_NAME = "dictionary.db"

internal class WikipediaLocalStorageImpl(
    context: Context,
    private val cursorToWikipediaInfoMapper: CursorToWikipediaInfoMapper
) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION),
    WikipediaLocalStorage {

    private val projection = arrayOf(
        COLUMN_ID,
        COLUMN_ARTIST,
        COLUMN_INFO,
        COLUMN_URL
    )

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    override fun insertInfo(artistName: String, artist: ArtistInfo) {
        val values = ContentValues().apply{
            put(COLUMN_ARTIST, artistName)
            put(COLUMN_INFO, artist.description)
            put(COLUMN_URL, artist.wikipediaURL)
            put(
                COLUMN_SOURCE,
                VALUE_SOURCE
            )
        }
        writableDatabase.insert(TABLE_NAME, null, values)

    }

    override fun getInfo(artist: String?): ArtistInfo? {
        val cursor = readableDatabase.query(
            TABLE_NAME,
            projection,
            COLUMNS_FOR_WHERE,
            arrayOf(artist),
            null,
            null,
            SORT_ORDER_CURSOR
        )
        val artistInfo = cursorToWikipediaInfoMapper.map(cursor)
        cursor.close()
        return artistInfo
    }
}