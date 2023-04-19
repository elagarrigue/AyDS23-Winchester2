package ayds.winchester.songinfo.moredetails.fulllogic

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DataBase(context: Context?) : SQLiteOpenHelper(context, "dictionary.db", null, 1) {
    companion object {
        private const val COLUMN_ID = "id"
        private const val COLUMN_ARTIST = "artist"
        private const val COLUMN_INFO = "info"
        private const val COLUMN_SOURCE = "source"
        private const val TABLE_NAME = "artists"
        private const val SELECTION = "$COLUMN_ARTIST = ?"
        private const val SORT_ORDER_CURSOR = "$COLUMN_ARTIST DESC"
        
        @JvmStatic
        fun saveArtist(dbHelper: DataBase, artist: String?, info: String?) {
            val db = getDBInWriteMode(dbHelper)

            // Create a new map of values, where column names are the keys
            val values = contentValues(artist, info)

            // Insert the new row, returning the primary key value of the new row
            db.insert(TABLE_NAME, null, values)
        }

        private fun contentValues(artist: String?, info: String?): ContentValues {
            val values = ContentValues()
            values.put(COLUMN_ARTIST, artist)
            values.put(COLUMN_INFO, info)
            values.put(COLUMN_SOURCE, 1)
            return values
        }

        @JvmStatic
        fun getInfo(dbHelper: DataBase, artist: String): String? {
            val db = getDBInReadMode(dbHelper)

            val cursor = createDataBaseQuery(db, artist)
            val items = getCursorInfo(cursor)
            cursor.close()
            return if (items.isEmpty()) null else items[0]
        }

        private fun createDataBaseQuery(db: SQLiteDatabase, artist: String): Cursor{
            return db.query(
                TABLE_NAME,
                columnsToReturn(),
                SELECTION,  // The columns for the WHERE clause
                selectionArgs(artist),  // The values for the WHERE clause
                null,  // don't group the rows
                null,  // don't filter by row groups
                SORT_ORDER_CURSOR
            )
        }

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

        private fun getDBInWriteMode(db: DataBase) = db.writableDatabase

        private fun getDBInReadMode(db: DataBase) = db.readableDatabase

        private fun columnsToReturn() = arrayOf(
            COLUMN_ID,
            COLUMN_ARTIST,
            COLUMN_INFO
        )

        private fun selectionArgs(artist: String) = arrayOf(artist)

    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "create table artists (id INTEGER PRIMARY KEY AUTOINCREMENT, artist string, info string, source integer)"
        )
        Log.i("DB", "DB created")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

}