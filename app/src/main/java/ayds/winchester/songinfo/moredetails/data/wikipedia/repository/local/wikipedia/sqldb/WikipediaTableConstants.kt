package ayds.winchester.songinfo.moredetails.data.wikipedia.repository.local.wikipedia.sqldb

const val COLUMN_ID = "id"
const val COLUMN_ARTIST = "artist"
const val COLUMN_INFO = "info"
const val COLUMN_SOURCE = "source"
const val COLUMN_URL = "url"
const val TABLE_NAME = "artists"
const val COLUMNS_FOR_WHERE = "$COLUMN_ARTIST = ?"
const val SORT_ORDER_CURSOR = "$COLUMN_ARTIST DESC"
const val VALUE_SOURCE = 1
const val SQL_CREATE_TABLE =
    "create table $TABLE_NAME (" +
            "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "$COLUMN_ARTIST, " +
            "$COLUMN_INFO, " +
            "$COLUMN_SOURCE)"