package android.bignerdranch.mycheckins;

import java.util.Date;
import java.util.UUID;

public class CheckIn  {
    private UUID mId;
    private String mTitle;
    private String mPlace;
    private String mDetails;
    private Date mDate;
    private double mLatitude;
    private double mLongitude;

    public double getLatitude() { return mLatitude; }

    public void setLatitude(double mLatitude) { this.mLatitude = mLatitude; }

    public double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(double mLongitude) { this.mLongitude = mLongitude; }

    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getPlace() {
        return mPlace;
    }

    public void setPlace(String mPlace) {
        this.mPlace = mPlace;
    }

    public String getDetails() {
        return mDetails;
    }

    public void setDetails(String mDetails) {
        this.mDetails = mDetails;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date mDate) {
        this.mDate = mDate;
    }

    public CheckIn() {
        this(UUID.randomUUID());
    }

    public CheckIn(UUID id) {
        mId = id;
        mDate = new Date();
    }
    public String getPhotoFileName() {
        return "IMG_" + getId().toString() + ".jpg";
    }
}
