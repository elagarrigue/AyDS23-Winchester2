package ayds.winchester.songinfo.moredetails.data.card.repository.local.card.sqldb

const val COLUMN_ID = "id"
const val COLUMN_ARTIST = "artist"
const val COLUMN_INFO = "info"
const val COLUMN_URL = "url"
const val COLUMN_SOURCE_LOGO_URL = "source_logo_url"
const val COLUMN_SOURCE = "source"
const val TABLE_NAME = "artists"
const val COLUMNS_FOR_WHERE = "$COLUMN_ARTIST = ?"
const val SORT_ORDER_CURSOR = "$COLUMN_ARTIST DESC"
const val SQL_CREATE_TABLE =
    "create table $TABLE_NAME (" +
            "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
            "$COLUMN_ARTIST string," +
            "$COLUMN_INFO string," +
            "$COLUMN_URL string," +
            "$COLUMN_SOURCE_LOGO_URL string," +
            "$COLUMN_SOURCE integer)"