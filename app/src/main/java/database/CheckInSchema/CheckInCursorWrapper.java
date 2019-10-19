package database.CheckInSchema;

import android.bignerdranch.mycheckins.CheckIn;
import android.database.Cursor;
import android.database.CursorWrapper;
import database.CheckInSchema.CheckInDbSchema.CheckInTable;
import java.util.Date;
import java.util.UUID;

public class CheckInCursorWrapper extends CursorWrapper {
    public CheckInCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public CheckIn getCheckIn() {
        String uuidString = getString(getColumnIndex(CheckInTable.Cols.UUID));
        String title = getString(getColumnIndex(CheckInTable.Cols.TITLE));
        String place = getString(getColumnIndex(CheckInTable.Cols.PLACE));
        String details = getString(getColumnIndex(CheckInTable.Cols.DETAILS));
        long date = getInt(getColumnIndex(CheckInTable.Cols.DATE));
        Double latitude = getDouble(getColumnIndex(CheckInTable.Cols.LATITUDE));
        Double longitude = getDouble(getColumnIndex(CheckInTable.Cols.LONGITUDE));

        CheckIn checkin = new CheckIn(UUID.fromString(uuidString));
        checkin.setTitle(title);
        checkin.setPlace(place);
        checkin.setDetails(details);
        checkin.setDate(new Date(date));
        checkin.setLatitude(latitude);
        checkin.setLongitude(longitude);

        return checkin;
    }
}
