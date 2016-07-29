package collector.app.nexd.com.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import collector.app.nexd.com.app.model.StaffModel;


/**
 * Created by xun on 2016/7/26.
 */
public class FilterUtil {

    public static Map<String, StaffModel> whiteList;

    static {
        whiteList = new HashMap<>();
        whiteList.put("01:17:C5:96:27:5D", new StaffModel("01:17:C5:96:27:5D", "Beacon_27:5D"));
        whiteList.put("01:17:C5:96:24:94", new StaffModel("01:17:C5:96:24:94", "Beacon_24:94"));
        whiteList.put("01:17:C5:96:3E:07", new StaffModel("01:17:C5:96:3E:07", "Beacon_3E:07"));
        whiteList.put("01:17:C5:96:03:55", new StaffModel("01:17:C5:96:03:55", "Beacon_03:55"));
        whiteList.put("01:17:C5:96:15:0D", new StaffModel("01:17:C5:96:15:0D", "Beacon_15:0D"));
        whiteList.put("01:17:C5:96:A7:CA", new StaffModel("01:17:C5:96:A7:CA", "Beacon_A7:CA"));
    }

    public List<StaffModel> innerLists = new ArrayList<>();
    public List<StaffModel> outerLists = new ArrayList<>();


    public void enterStaff(StaffModel enterStaff) {
        innerLists.add(enterStaff);
        if (outerLists.contains(enterStaff)) {
            outerLists.remove(enterStaff);
        }
    }

    public void exitStaff(StaffModel exitStaff) {
        outerLists.add(exitStaff);
        innerLists.remove(exitStaff);
    }


    // =================================华丽的分割线====================================================


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
            //StaffModel targetModel = new StaffModel(macId, whiteList.get(macId), true);
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
