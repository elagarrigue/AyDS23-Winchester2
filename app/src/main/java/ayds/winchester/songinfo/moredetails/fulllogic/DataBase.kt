package ayds.winchester.songinfo.moredetails.fulllogic

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

private const val COLUMN_ID = "id"
private const val COLUMN_ARTIST = "artist"
private const val COLUMN_INFO = "info"
private const val COLUMN_SOURCE = "source"
private const val TABLE_NAME = "artists"
private const val COLUMNS_FOR_WHERE = "$COLUMN_ARTIST = ?"
private const val SORT_ORDER_CURSOR = "$COLUMN_ARTIST DESC"
private const val DICTIONARY_DATABASE = "dictionary.db"
private const val SQL_CREATE_TABLE = "create table artists (id INTEGER PRIMARY KEY AUTOINCREMENT, $COLUMN_ARTIST, $COLUMN_INFO, $COLUMN_SOURCE)"
private const val VERSION = 1
private const val VALUE_SOURCE = 1

class DataBase(context: Context?) : SQLiteOpenHelper(context, DICTIONARY_DATABASE, null, VERSION) {

    fun saveArtist(artist: String?, info: String) {
        val values = createMapOfValues(artist, info)
        insertRowInDataBase(values)
    }

    fun getArtistInfo(artist: String?): String {
        val cursor = createDataBaseQuery(artist)
        val items = getCursorInfo(cursor)
        cursor.close()
        return items
    }
    
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            SQL_CREATE_TABLE
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

    private fun insertRowInDataBase(values: ContentValues) {
        writableDatabase.insert(TABLE_NAME, null, values)
    }

    private fun createMapOfValues(artist: String?, info: String): ContentValues {
        val values = ContentValues()
        values.put(COLUMN_ARTIST, artist)
        values.put(COLUMN_INFO, info)
        values.put(COLUMN_SOURCE, VALUE_SOURCE)
        return values
    }

    private fun createDataBaseQuery(artist: String?) =
        readableDatabase.query(
            TABLE_NAME,
            columnsToReturn(),
            COLUMNS_FOR_WHERE,
            valuesForWhere(artist),
            null,
            null,
            SORT_ORDER_CURSOR
        )

    private fun getCursorInfo(cursor: Cursor): String {
        var info = ""
        if (cursor.moveToNext()) {
            info = cursor.getString(
                cursor.getColumnIndexOrThrow(COLUMN_INFO)
            )
        }
        return info
    }

    private fun columnsToReturn() = arrayOf(
        COLUMN_ID,
        COLUMN_ARTIST,
        COLUMN_INFO
    )

    private fun valuesForWhere(artist: String?) = arrayOf(artist)

}