package collector.app.nexd.com.app.test;

/**
 * Created by wangxu on 16/8/23.
 */
public class BeaconScanResult {

    private String address;
    private String name;

    private float level;
    private String time;

    public BeaconScanResult(String address, String name, float level, String time) {
        this.address = address;
        this.name = name;
        this.level = level;
        this.time = time;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
