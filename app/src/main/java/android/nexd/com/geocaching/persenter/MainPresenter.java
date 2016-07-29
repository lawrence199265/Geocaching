package android.nexd.com.geocaching.persenter;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.nexd.com.geocaching.model.TargetModel;
import android.nexd.com.geocaching.ui.iView.IMainView;
import android.nexd.com.geocaching.util.QRCodeUtil;
import android.os.Environment;
import android.util.Log;

import com.sensoro.beacon.kit.Beacon;
import com.sensoro.beacon.kit.BeaconManagerListener;
import com.sensoro.cloud.SensoroManager;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by wangxu on 16/6/14.
 */
public class MainPresenter extends BasePresenter<IMainView> {
    private static final int REQUEST_ENABLE_BT = 0x1001;// 请求蓝牙开启标识

    private static final String TAG = "MainPresenter";

    private Map<String, TargetModel> targetBeacons;
    private SensoroManager sensoroManager;


    public MainPresenter(Context context, IMainView iView) {
        super(context, iView);
    }

    @Override
    public void release() {

    }

    public void getTaskList(final List<TargetModel> beacons) {
        // 初始化任务集合
        targetBeacons = new HashMap<>();
        for (TargetModel beacon : beacons) {
            targetBeacons.put(beacon.getMacId(), beacon);
        }


        // 初始化蓝牙管理工具
        sensoroManager = SensoroManager.getInstance(context);
        // 初始化蓝牙扫描
        if (!sensoroManager.isBluetoothEnabled()) {
            ((Activity) context).startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), REQUEST_ENABLE_BT);
        } else {
            BeaconManagerListener beaconManagerListener = new BeaconManagerListener() {
                @Override
                public void onNewBeacon(Beacon beacon) {
                    Log.d(TAG, "onNewBeacon: appear a new Beacon");
                    // 一个传感器出现
                    if (targetBeacons.containsKey(beacon.getSerialNumber())) {
                        Log.d(TAG, "onNewBeacon: this is my data");
                        TargetModel targetModel = targetBeacons.get(beacon.getSerialNumber());
                        targetModel.setDistance(beacon.getAccuracy());
                        targetModel.setApproach(true);
                        targetBeacons.put(beacon.getSerialNumber(), targetModel);
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                iView.updateAdapter();
                            }
                        });
                    }
                }

                @Override
                public void onGoneBeacon(Beacon beacon) {
                    Log.d(TAG, "onGoneBeacon: a Beacon gone");
                    // 一个传感器消失
                    if (targetBeacons.containsKey(beacon.getSerialNumber())) {
                        Log.d(TAG, "onGoneBeacon: this is my data");
                        TargetModel targetModel = targetBeacons.get(beacon.getSerialNumber());
                        targetModel.setApproach(false);
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                iView.updateAdapter();
                            }
                        });
                    }
                }

                @Override
                public void onUpdateBeacon(ArrayList<Beacon> arrayList) {
                    Log.d(TAG, "onUpdateBeacon: update all Beacon");
                    // 传感器信息更新
                    for (Beacon beacon : arrayList) {
                        if (targetBeacons.containsKey(beacon.getSerialNumber())) {
                            Log.d(TAG, "onUpdateBeacon: this is my data");
                            TargetModel targetModel = targetBeacons.get(beacon.getSerialNumber());
                            targetModel.setDistance(beacon.getAccuracy());
                            targetModel.setApproach(true);
                            targetBeacons.put(beacon.getSerialNumber(), targetModel);
                        }
                    }
                    ((Activity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            iView.updateAdapter();
                        }
                    });

                }
            };
            sensoroManager.setBeaconManagerListener(beaconManagerListener);
        }
    }

    public void startLooking() {
        try {
            sensoroManager.startService();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopLooking() {
        sensoroManager.stopService();
    }

    private String getFileRoot(Context context) {

        return Environment.getExternalStorageDirectory().getPath() + "/geocaching/picture";
//        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//            File external = context.getExternalFilesDir(null);
//            if (external != null) {
//                return external.getAbsolutePath();
//            }
//        }
//        return context.getFilesDir().getAbsolutePath();
    }

    public void createQECode(List<TargetModel> list, String userName, String telNumber) {

        final String filePath = getFileRoot(context) + File.separator + "/qr_" + System.currentTimeMillis() + ".jpg";
        final StringBuffer content = new StringBuffer();
        content.append(userName)
                .append("\n")
                .append(telNumber)
                .append("\n\n");
        for (TargetModel targetModel : list) {
            content/*.append(targetModel.getMacId())
                    .append("\n")*/
                    .append(targetModel.getAnswer())
                    .append("\n\n");
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean isSuccess = QRCodeUtil.createQRImage(content.toString(), 1000, 1000, null, filePath);
                if (isSuccess) {
                    ((Activity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            iView.updateImageView(filePath);
                        }
                    });
                }
            }
        }).start();
    }


}
