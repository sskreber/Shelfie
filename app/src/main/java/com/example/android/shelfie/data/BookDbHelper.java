package com.example.android.shelfie.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.android.shelfie.data.BookContract.BookEntry;

public class BookDbHelper extends SQLiteOpenHelper {

    protected static final String DATABASE_NAME = "shelfie.db";
    protected static final int DATABASE_VERSION = 1;

    protected static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + BookEntry.TABLE_NAME + ";";

    public BookDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_BOOKS_TABLE = "CREATE TABLE " + BookEntry.TABLE_NAME + " ("
                + BookEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + BookEntry.COLUMN_BOOK_SUPPLIER_NAME + " TEXT, "
                + BookEntry.COLUMN_BOOK_SUPPLIER_PHONE_NUMBER + " INTEGER, "

                + BookEntry.COLUMN_BOOK_PRODUCT_NAME + " TEXT, "
                + BookEntry.COLUMN_BOOK_QUANTITY + " INTEGER NOT NULL DEFAULT 1, "

                + BookEntry.COLUMN_BOOK_AUTHOR + " TEXT, "
                + BookEntry.COLUMN_BOOK_TITLE + " TEXT, "
                + BookEntry.COLUMN_BOOK_PUBLICATION_YEAR + " INTEGER, "
                + BookEntry.COLUMN_BOOK_LANGUAGE + " TEXT, "
                + BookEntry.COLUMN_BOOK_PRICE + " INTEGER, "

                + BookEntry.COLUMN_BOOK_STATE + " INTEGER, "
                + BookEntry.COLUMN_BOOK_AVAILABILITY + " INTEGER NOT NULL DEFAULT 0); ";

        db.execSQL(SQL_CREATE_BOOKS_TABLE);
        Log.e("Db created", SQL_CREATE_BOOKS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}
