package collector.app.nexd.com.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import collector.app.nexd.com.app.inter.UpBeaconListener;
import collector.app.nexd.com.app.model.TargetModel;

/**
 * Created by xun on 2016/7/26.
 */
public class FilterUtil {

    public static Map<String, String> whiteList;

    static {
        whiteList = new HashMap<>();
        whiteList.put("01:17:C5:96:27:5D", "Beacon1");
        whiteList.put("01:17:C5:96:24:94", "Beacon2");
        whiteList.put("01:17:C5:96:3E:07", "Beacon3");
        whiteList.put("01:17:C5:96:03:55", "Beacon4");
        whiteList.put("01:17:C5:96:15:0D", "Beacon5");
        whiteList.put("01:17:C5:96:A7:CA", "Beacon6");

    }

    public List<String> enterQueue = new ArrayList<>();
    public List<String> exitQueue = new ArrayList<>();
    private List<String> oldBeacon;
    private List<String> exitBeacon;


    public FilterUtil(List<String> oldBeacon/*, List<String> exitBeacon*/) {
        this.oldBeacon = oldBeacon;
//        this.exitBeacon = exitBeacon;
    }

    public void enterNewBeacon(String name) {
        if (!oldBeacon.contains(name)) {
            oldBeacon.add(name);
        }

    }

    public void enterBeacon(String name) {
        if (exitBeacon.contains(name)) {
            if (!oldBeacon.contains(name)) {
                oldBeacon.add(name);
            }
            //oldBeacon.put(macId, exitBeacon.get(macId));
            exitBeacon.remove(name);
        } else {
            //TargetModel targetModel = new TargetModel(macId, whiteList.get(macId), true);
            if (!oldBeacon.contains(name)) {
                oldBeacon.add(name);
            }
        }
        //去重
        for (int i = 0; i < oldBeacon.size(); i++) {
            for (int j = oldBeacon.size() - 1; j > i; j--) {
                if (oldBeacon.get(i).equals(oldBeacon.get(j))) {
                    oldBeacon.remove(j);
                }
            }
        }
    }

    public void exitBeacon(String name) {
        if (!exitBeacon.contains(name)) {
            exitBeacon.add(name);
            oldBeacon.remove(name);
        }

//        exitBeacon.put(macId, oldBeacon.get(macId));
//        oldBeacon.remove(macId);
    }
}
