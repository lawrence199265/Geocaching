package android.nexd.com.geocaching.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wangxu on 16/6/14.
 */
public class TargetModel implements Parcelable {


    private String macId;

    private double distance;

    private boolean isFinish;

    private boolean isApproach;


    private String answer;


    public TargetModel(String macId, double distance, boolean isFinish, boolean isApproach) {
        this.macId = macId;
        this.distance = distance;
        this.isFinish = isFinish;
        this.isApproach = isApproach;
    }

    public TargetModel(String macId, double distance, boolean isFinish, boolean isApproach, String answer) {
        this.macId = macId;
        this.distance = distance;
        this.isFinish = isFinish;
        this.isApproach = isApproach;
        this.answer = answer;
    }

    protected TargetModel(Parcel in) {
        macId = in.readString();
        distance = in.readDouble();
        isFinish = in.readByte() != 0;
        isApproach = in.readByte() != 0;
        answer = in.readString();
    }

    public static final Creator<TargetModel> CREATOR = new Creator<TargetModel>() {
        @Override
        public TargetModel createFromParcel(Parcel in) {
            return new TargetModel(in);
        }

        @Override
        public TargetModel[] newArray(int size) {
            return new TargetModel[size];
        }
    };

    public String getMacId() {
        return macId;
    }

    public void setMacId(String macId) {
        this.macId = macId;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = (distance + this.distance) / 2;
    }

    public boolean isFinish() {
        return isFinish;
    }

    public void setFinish(boolean finish) {
        isFinish = finish;
    }

    public boolean isApproach() {
        return isApproach;
    }

    public void setApproach(boolean approach) {
        isApproach = approach;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(macId);
        dest.writeDouble(distance);
        dest.writeByte((byte) (isFinish ? 1 : 0));
        dest.writeByte((byte) (isApproach ? 1 : 0));
        dest.writeString(answer);
    }
}
