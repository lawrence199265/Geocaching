package collector.app.nexd.com.app.model;

/**
 * Created by xun on 2016/7/25.
 */
public class TargetModel {
    private String macAddr;  // address
    private String beaconName; // name
    private boolean aliveValue; //

    public boolean getAliveValue() {
        return aliveValue;
    }

    public void setAliveValue(boolean aliveValue) {
        this.aliveValue = aliveValue;
    }

    public TargetModel(String macAddr, String beaconName, boolean aliveValue) {
        this.macAddr = macAddr;
        this.beaconName = beaconName;
        this.aliveValue = aliveValue;
    }

    public void setMacAddr(String macAddr) {
        this.macAddr = macAddr;
    }

    public void setBeaconName(String beaconName) {
        this.beaconName = beaconName;
    }


    public String getMacAddr() {
        return macAddr;
    }

    public String getBeaconName() {
        return beaconName;
    }


}
