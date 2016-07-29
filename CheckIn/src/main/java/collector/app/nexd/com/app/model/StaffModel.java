package collector.app.nexd.com.app.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by xun on 2016/7/25.
 */
public class StaffModel implements Parcelable {


    private String macAddress;

    private String beaconName;

    private int timer = 60;


    public StaffModel(String macAddress, String beaconName) {
        this.macAddress = macAddress;
        this.beaconName = beaconName;
    }

    protected StaffModel(Parcel in) {
        macAddress = in.readString();
        beaconName = in.readString();
        timer = in.readInt();
    }

    public static final Creator<StaffModel> CREATOR = new Creator<StaffModel>() {
        @Override
        public StaffModel createFromParcel(Parcel in) {
            return new StaffModel(in);
        }

        @Override
        public StaffModel[] newArray(int size) {
            return new StaffModel[size];
        }
    };

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getBeaconName() {
        return beaconName;
    }

    public void setBeaconName(String beaconName) {
        this.beaconName = beaconName;
    }

    public int getTimer() {
        return timer;
    }

    public void setTimer() {
        this.timer = 60;
    }

    public void subTimer() {
        timer--;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(macAddress);
        dest.writeString(beaconName);
        dest.writeInt(timer);
    }
}
