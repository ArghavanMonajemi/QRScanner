package com.gmail.monajemi.am.android.qrscanner.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.gmail.monajemi.am.android.qrscanner.model.History;

import java.util.ArrayList;
import java.util.List;

public class SqliteDatabase extends SQLiteOpenHelper {

    Context context;
    private static final String DATABASE_NAME = "qrScannerDb";
    private static final int DATABASE_VERSION = 1;

    private final String HISTORY_TABLE_NAME = "history";
    final static String HISTORY_COL_ID = "id";              //0
    final String HISTORY_COL_DATE = "date";                 //1
    final String HISTORY_COL_LINK = "link";                 //2

    public SqliteDatabase(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String SQL_COMMAND_CREATE_HISTORY_TABLE = "CREATE TABLE IF NOT EXISTS " + HISTORY_TABLE_NAME + "(" +
                HISTORY_COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                HISTORY_COL_LINK + " TEXT, " +
                HISTORY_COL_DATE + " TEXT);";
        sqLiteDatabase.execSQL(SQL_COMMAND_CREATE_HISTORY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + HISTORY_TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void insertHistory(History history) {
        ContentValues cv = new ContentValues();
        cv.put(HISTORY_COL_LINK, history.getData());
        cv.put(HISTORY_COL_DATE, history.getDate());
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.insert(HISTORY_TABLE_NAME, null, cv);
    }

    public List<History> getAllHistory() {
        List<History> historyList = new ArrayList<>();
        String query = "SELECT " + HISTORY_COL_ID + "," + HISTORY_COL_LINK + "," + HISTORY_COL_DATE + " FROM " + HISTORY_TABLE_NAME;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            while (!cursor.isAfterLast()) {
                History history = new History(cursor.getInt(0), cursor.getString(1), cursor.getString(2));
                historyList.add(history);
                cursor.moveToNext();
            }
        }
        cursor.close();
        sqLiteDatabase.close();
        return historyList;
    }


    public void deleteHistory(String id) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(HISTORY_TABLE_NAME, HISTORY_COL_ID+" = ?", new String[]{id});
        sqLiteDatabase.close();
    }
}
