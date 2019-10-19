package android.bignerdranch.mycheckins;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import database.CheckInSchema.CheckInDbSchema.CheckInTable;
import database.CheckInSchema.CheckInBaseHelper;
import database.CheckInSchema.CheckInCursorWrapper;

public class CheckInList {
    private static CheckInList sCheckInList;
    private Context mContext;
    public SQLiteDatabase mDataBase;

    public static CheckInList get(Context context) {
        if (sCheckInList == null) {
            sCheckInList = new CheckInList(context);
        }
        return sCheckInList;
    }

    public CheckInList(Context context) {
        mContext = context.getApplicationContext();
        mDataBase = new CheckInBaseHelper(mContext).getWritableDatabase();
    }

    public int deleteCheckIn(CheckIn c) {
        //mDataBase.delete(DATABASE_NAME,  CheckInTable.Cols.UUID + "=\"" + c.getId().toString() + "\"", null);
       //return mDataBase.delete(CheckInTable.NAME,  CheckInTable.Cols.UUID + "=?", new String[]{c.getId()});
        return 0;
    }

    public void addCheckIn(CheckIn c) {
        ContentValues values = getContentValues(c);
        mDataBase.insert(CheckInTable.NAME, null, values);
    }

    public List<CheckIn> getCheckInList() {
        List<CheckIn> checkins = new ArrayList<>();
        CheckInCursorWrapper cursor = queryCheckIns(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                checkins.add(cursor.getCheckIn());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return checkins;
    }

    public CheckIn getCheckInID(UUID id) {
        CheckInCursorWrapper cursor = queryCheckIns(
                CheckInTable.Cols.UUID + " = ?",
                new String[] { id.toString() }
        );

        try {
            if (cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();
            return cursor.getCheckIn();
        } finally {
            cursor.close();
        }
    }

    public File getPhotoFile(CheckIn checkin) {
        File filesDir = mContext.getFilesDir();
        return new File(filesDir, checkin.getPhotoFileName());
    }

    public void updateCheckIn(CheckIn checkin) {
        String uuidString = checkin.getId().toString();
        ContentValues values = getContentValues(checkin);

        mDataBase.update(CheckInTable.NAME, values,
                CheckInTable.Cols.UUID + " = ?",
                new String[] {uuidString});
    }

    private CheckInCursorWrapper queryCheckIns(String whereClause, String[] whereArgs) {
        Cursor cursor = mDataBase.query(
                CheckInTable.NAME,
            null, // columns - null selects all columns
                    whereClause,
                    whereArgs,
            null, // groupBy
            null, // having
            null // orderBy

        );
        return new CheckInCursorWrapper(cursor);
    }

    private static ContentValues getContentValues(CheckIn checkin) {
        ContentValues values = new ContentValues();
        values.put(CheckInTable.Cols.UUID, checkin.getId().toString());
        values.put(CheckInTable.Cols.TITLE, checkin.getTitle());
        values.put(CheckInTable.Cols.PLACE, checkin.getPlace());
        values.put(CheckInTable.Cols.DETAILS, checkin.getDetails());
        values.put(CheckInTable.Cols.DATE, checkin.getDate().getTime());
        values.put(CheckInTable.Cols.LATITUDE, checkin.getLatitude());
        values.put(CheckInTable.Cols.LONGITUDE, checkin.getLatitude());

        return values;
    }



}
