package ayds.winchester.songinfo.moredetails.fulllogic

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

private const val COLUMN_ID = "id"
private const val COLUMN_ARTIST = "artist"
private const val COLUMN_INFO = "info"
private const val COLUMN_SOURCE = "source"
private const val TABLE_NAME = "artists"
private const val COLUMNS_FOR_WHERE = "$COLUMN_ARTIST = ?"
private const val SORT_ORDER_CURSOR = "$COLUMN_ARTIST DESC"

class DataBase(context: Context?) : SQLiteOpenHelper(context, "dictionary.db", null, 1) {

    private val databaseInWriteMode = this.writableDatabase
    private val databaseInReadMode = this.readableDatabase

    fun saveArtist(artist: String, info: String) {
        val values = createMapOfValues(artist, info)
        insertRowInDataBase(values)
    }
    fun getInfo(artist: String): String? {
        val cursor = createDataBaseQuery(artist)
        val items = getCursorInfo(cursor)
        cursor.close()
        return if (items.isEmpty()) null else items[0]
    }

    private fun insertRowInDataBase(values: ContentValues) {
        databaseInWriteMode.insert(TABLE_NAME, null, values)
    }

    private fun createMapOfValues(artist: String, info: String): ContentValues {
        val values = ContentValues()
        values.put(COLUMN_ARTIST, artist)
        values.put(COLUMN_INFO, info)
        values.put(COLUMN_SOURCE, 1)
        return values
    }

    private fun createDataBaseQuery(artist: String) =
        databaseInReadMode.query(
            TABLE_NAME,
            columnsToReturn(),
            COLUMNS_FOR_WHERE,
            valuesForWhere(artist),
            null,  // don't group the rows
            null,  // don't filter by row groups
            SORT_ORDER_CURSOR
        )

    private fun getCursorInfo(cursor: Cursor): MutableList<String> {
        val items: MutableList<String> = ArrayList()
        while (cursor.moveToNext()) {
            val info = cursor.getString(
                cursor.getColumnIndexOrThrow(COLUMN_INFO)
            )
            items.add(info)
        }
        return items
    }

    private fun columnsToReturn() = arrayOf(
        COLUMN_ID,
        COLUMN_ARTIST,
        COLUMN_INFO
    )

    private fun valuesForWhere(artist: String) = arrayOf(artist)

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "create table artists (id INTEGER PRIMARY KEY AUTOINCREMENT, artist string, info string, source integer)"
        )
        Log.i("DB", "DB created")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

}