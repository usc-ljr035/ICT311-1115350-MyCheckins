package database.CheckInSchema;

import android.bignerdranch.mycheckins.CheckIn;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import database.CheckInSchema.CheckInDbSchema.CheckInTable;

public class CheckInBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    public static final String DATABASE_NAME = "checkinBase.db";

    public CheckInBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create TABLE " + CheckInTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                CheckInTable.Cols.UUID + ", " +
                CheckInTable.Cols.TITLE + ", " +
                CheckInTable.Cols.PLACE + ", " +
                CheckInTable.Cols.DETAILS + ", " +
                CheckInTable.Cols.DATE + ", " +
                CheckInTable.Cols.LATITUDE + ", " +
                CheckInTable.Cols.LONGITUDE +
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
