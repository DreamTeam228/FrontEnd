package msc.fooxer.studplaces

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class DBHelper(context: Context?, name: String? = DB_NAME, factory: SQLiteDatabase.CursorFactory? = null, version: Int = CURRENT_VER) :
    SQLiteOpenHelper(context, name, factory, version) {
    override fun onCreate(db: SQLiteDatabase?) {
        if (db!=null) {
            db.execSQL("create table $FAV_TABLE_NAME($KEY_INDEX integer primary key,$KEY_NAME text, $KEY_CATEGORY text,  $KEY_DESCR text,$KEY_PRICE integer, $KEY_PIC text, $KEY_METRO text, $KEY_PHONE text, $KEY_ADDRESS text, $KEY_FAV integer)")
            db.execSQL("create table $CASH_TABLE_NAME($KEY_INDEX integer primary key,$KEY_NAME text, $KEY_CATEGORY text,  $KEY_DESCR text,$KEY_PRICE integer, $KEY_PIC text, $KEY_METRO text, $KEY_PHONE text, $KEY_ADDRESS text, $KEY_FAV integer)")
            /*db.execSQL("create table $FAV_TABLE_NAME($KEY_INDEX integer primary key,$KEY_NAME text, $KEY_CATEGORY text, $KEY_DESCR text," +
                    "$KEY_PRICE integer, $KEY_METRO text, $KEY_PHONE text, $KEY_ADDRESS text, $KEY_FAV integer)")
            db.execSQL("create table $CASH_TABLE_NAME($KEY_INDEX integer primary key, $KEY_NAME text, $KEY_CATEGORY text, $KEY_DESCR text," +
                    "$KEY_PRICE integer, $KEY_METRO text, $KEY_PHONE text, $KEY_ADDRESS text, $KEY_FAV integer)")
            db.execSQL("create table $TABLE_NAME($KEY_INDEX integer primary key, $KEY_NAME text,$KEY_TIME text)")*/


        }

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}