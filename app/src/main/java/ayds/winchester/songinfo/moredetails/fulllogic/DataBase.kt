package ayds.winchester.songinfo.moredetails.fulllogic

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement

class DataBase(context: Context?) : SQLiteOpenHelper(context, "dictionary.db", null, 1) {
    companion object {
        private const val COLUMN_ID = "id"
        private const val COLUMN_ARTIST = "artist"
        private const val COLUMN_INFO = "info"
        private const val COLUMN_SOURCE = "source"
        private const val TABLE_NAME = "artists"

        private lateinit var connection: Connection
        fun testDB() {
            try {
                val statement: Statement = createDataBaseConnection()
                val rs = getResultSet(statement)
                readResultSet(rs)
            } catch (e: SQLException) {
                // if the error message is "out of memory",
                // it probably means no database file is found
                System.err.println(e.message)
            } finally {
                try {
                    connection?.close()
                } catch (e: SQLException) {
                    // connection close failed.
                    System.err.println(e)
                }
            }
        }
        private fun getResultSet(statement: Statement) = statement.executeQuery("select * from artists")
        private fun readResultSet(rs:ResultSet){//Este metodo seria mas correcto rs.readResultSet? Habria que hacer un read para cada columna?
            while (rs.next()) {
                println("$COLUMN_ID = " + rs.getInt(COLUMN_ID))
                println("$COLUMN_ARTIST = " + rs.getString(COLUMN_ARTIST))
                println("$COLUMN_INFO = " + rs.getString(COLUMN_INFO))
                println("$COLUMN_SOURCE = " + rs.getString(COLUMN_SOURCE))
            }
        }

        private fun createDataBaseConnection(): Statement {
            connection = DriverManager.getConnection("jdbc:sqlite:./dictionary.db")
            val statement: Statement = connection.createStatement()
            statement.queryTimeout = 30 // set timeout to 30 sec.
            return statement
        }

        private fun getDBInWriteMode(db: DataBase) = db.writableDatabase
        private fun getDBInReadMode(db: DataBase) = db.readableDatabase

        @JvmStatic
        fun saveArtist(dbHelper: DataBase, artist: String?, info: String?) {
            val db = getDBInWriteMode(dbHelper)

            // Create a new map of values, where column names are the keys
            val values = ContentValues()
            values.put(COLUMN_ARTIST, artist)
            values.put(COLUMN_INFO, info)
            values.put(COLUMN_SOURCE, 1)

            // Insert the new row, returning the primary key value of the new row
            val newRowId = db.insert(TABLE_NAME, null, values)
        }

        @JvmStatic
        fun getInfo(dbHelper: DataBase, artist: String): String? {
            val db = getDBInReadMode(dbHelper)

            // Define a projection that specifies which columns from the database
            // you will actually use after this query.
            val projection = arrayOf(
                COLUMN_ID,
                COLUMN_ARTIST,
                COLUMN_INFO
            )

            // Filter results WHERE "title" = 'My Title'
            val selection = "$COLUMN_ARTIST = ?"
            val selectionArgs = arrayOf(artist)

            // How you want the results sorted in the resulting Cursor
            val sortOrder = "$COLUMN_ARTIST DESC"
            val cursor = db.query(
                TABLE_NAME,  // The table to query
                projection,  // The array of columns to return (pass null to get all)
                selection,  // The columns for the WHERE clause
                selectionArgs,  // The values for the WHERE clause
                null,  // don't group the rows
                null,  // don't filter by row groups
                sortOrder // The sort order
            )
            val items: MutableList<String> = ArrayList()
            while (cursor.moveToNext()) {
                val info = cursor.getString(
                    cursor.getColumnIndexOrThrow(COLUMN_INFO)
                )
                items.add(info)
            }
            cursor.close()
            return if (items.isEmpty()) null else items[0]
        }
    }
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "create table artists (id INTEGER PRIMARY KEY AUTOINCREMENT, artist string, info string, source integer)"
        )
        Log.i("DB", "DB created")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

}