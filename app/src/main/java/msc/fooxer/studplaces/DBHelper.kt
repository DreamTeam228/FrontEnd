package msc.fooxer.studplaces

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class DBHelper(context: Context?, name: String? = DB_NAME, factory: SQLiteDatabase.CursorFactory? = null, version: Int = CURRENT_VER) :
    SQLiteOpenHelper(context, name, factory, version) {
    override fun onCreate(db: SQLiteDatabase?) {
        if (db != null) {
            db.execSQL("create table $FAV_TABLE_NAME($KEY_INDEX integer primary key,$KEY_NAME text, $KEY_CATEGORY text,  $KEY_DESCR text,$KEY_PRICE integer, $KEY_PIC text, " +
                    "$KEY_METRO text, $KEY_PHONE text, $KEY_ADDRESS text, $KEY_FAV integer, $KEY_DATE integer, $KEY_DISCOUNT integer, $KEY_URL text)")


        }

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (db != null) {
            //if (oldVersion == 1 && newVersion == 2)
            db.execSQL("drop table if exists $CASH_TABLE_NAME")
            db.execSQL("drop table if exists $FAV_TABLE_NAME")
            onCreate(db)
        }
    }

    override fun onDowngrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (db != null) {
            db.execSQL("drop table if exists $FAV_TABLE_NAME")
            onCreate(db)
        }
    }
}