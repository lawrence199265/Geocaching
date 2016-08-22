package android.nexd.com.geocaching.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wangxu on 16/6/14.
 */
public class BoothMessage implements Parcelable {

    private String boothName;
    private String convertCode;
    private String boothPresent;
    private int boothCoordinate;
    private String presentRoute;


    public BoothMessage(String boothName, String convertCode, String boothPresent, int boothCoordinate, String presentRoute) {
        this.boothName = boothName;
        this.convertCode = convertCode;
        this.boothPresent = boothPresent;
        this.boothCoordinate = boothCoordinate;
        this.presentRoute = presentRoute;
    }

    protected BoothMessage(Parcel in) {
        boothName = in.readString();
        convertCode = in.readString();
        boothPresent = in.readString();
        boothCoordinate = in.readInt();
        presentRoute = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(boothName);
        dest.writeString(convertCode);
        dest.writeString(boothPresent);
        dest.writeInt(boothCoordinate);
        dest.writeString(presentRoute);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BoothMessage> CREATOR = new Creator<BoothMessage>() {
        @Override
        public BoothMessage createFromParcel(Parcel in) {
            return new BoothMessage(in);
        }

        @Override
        public BoothMessage[] newArray(int size) {
            return new BoothMessage[size];
        }
    };


    public String getBoothName() {
        return boothName;
    }

    public void setBoothName(String boothName) {
        this.boothName = boothName;
    }

    public String getConvertCode() {
        return convertCode;
    }

    public void setConvertCode(String convertCode) {
        this.convertCode = convertCode;
    }

    public String getBoothPresent() {
        return boothPresent;
    }

    public void setBoothPresent(String boothPresent) {
        this.boothPresent = boothPresent;
    }

    public int getBoothCoordinate() {
        return boothCoordinate;
    }

    public void setBoothCoordinate(int boothCoordinate) {
        this.boothCoordinate = boothCoordinate;
    }

    public String getPresentRoute() {
        return presentRoute;
    }

    public void setPresentRoute(String presentRoute) {
        this.presentRoute = presentRoute;
    }
}
