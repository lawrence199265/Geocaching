package collector.app.nexd.com.app;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sensoro.beacon.kit.Beacon;
import com.sensoro.beacon.kit.BeaconManagerListener;
import com.sensoro.cloud.SensoroManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.nexd.sdk.collector.NexdCollectorAgent;
import cn.nexd.sdk.collector.NexdCollectorConfiguration;
import cn.nexd.sdk.collector.NexdCollectorResult;
import cn.nexd.sdk.collector.NexdCollectorResultListener;
import collector.app.nexd.com.app.model.StaffModel;

public class MainActivity extends AppCompatActivity implements BeaconManagerListener {
    private static final String TAG = "MainActivity";
    private SensoroManager sensorManager;
    private Map<String, String> whiteList = new HashMap<>();
    private Map<String, StaffModel> resultBeacon = new HashMap<>();
    private ProgressBar runMan;
    private TextView tvShowInfo;
    private FilterUtil filterUtil;
    private ListView lvNew, lvExit;
    private ArrayAdapter<String> newListAdapter, exitListAdapter;
    private List<String> newList = new ArrayList<>();
    private List<String> exitList = new ArrayList<>();
    private View footView;
    private int index = 0;
    private Button btnSubmit;
    private EditText etInputSecond;
    public static volatile int finalSecond = 60;


    // ================================华丽的分割线================================
    private InnerStaffAdapter innerStaffAdapter;
    private OuterStaffAdapter outerStaffAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 初始化过滤器
        filterUtil = new FilterUtil(newList/*, exitList*/);
        initView();
        // 初始化列表业务
        innerStaffAdapter = new InnerStaffAdapter(this, filterUtil.innerLists);
        outerStaffAdapter = new OuterStaffAdapter(this, filterUtil.outerLists);
        lvNew.setAdapter(innerStaffAdapter);
        lvExit.setAdapter(outerStaffAdapter);
        // 初始化采集业务
        NexdCollectorAgent collectorAgent = NexdCollectorAgent.getCollectorAgent(this);
        NexdCollectorConfiguration.Buidler buidler = new NexdCollectorConfiguration.Buidler();
        buidler.setAppkey("").setBeaconCollectorDelay(1000);
        buidler.setBeaconCollectorRate(1000);
        buidler.setBeaconCollectorEnable(true);
        buidler.setDebugMode(true);
        buidler.setWifiCollectorEnable(false);
        buidler.setBeaconResultCacheTime(3000);

        // 开始采集,  采集中的临时集合尽量不要提取处理啊,有可能会影响业务
        collectorAgent.startCollector(buidler.build(), new NexdCollectorResultListener() {
            @Override
            public void onCollectorChanged(List<NexdCollectorResult> list) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        runMan.setVisibility(View.VISIBLE);
                        tvShowInfo.setText("正在查找中...");
                    }
                });
                if (list.get(0).getStateCode() == NexdCollectorResult.ERROR_CODE_COLLECTOR_SUCCESS) {
                    List<StaffModel> tempModels = new ArrayList<>();
                    for (NexdCollectorResult nexdCollectorResult : list) {
                        // 将白名单的 Beacon 过滤出来
                        if (FilterUtil.whiteList.containsKey(nexdCollectorResult.getSingleSourceAddress())) {
                            Log.d(TAG, "onCollectorChanged: " + nexdCollectorResult.getSingleSourceAddress());
                            tempModels.add(FilterUtil.whiteList.get(nexdCollectorResult.getSingleSourceAddress()));
                        }
                    }

                    checkUpdate(tempModels);
                }
            }
        });
    }


    public void checkUpdate(List<StaffModel> tempModels) {
        // 将扫描到的数据,添加并重置时钟到inner中
        for (StaffModel tempModel : tempModels) {
            if (!filterUtil.innerLists.contains(tempModel)) {
                filterUtil.enterStaff(tempModel);
            }
            tempModel.setTimer();
        }
        List<StaffModel> tempOutStaff = new ArrayList<>();
        // 获取,本次没有扫描到的Beacon, 对其 timer 倒计时统计
        for (StaffModel innerList : filterUtil.innerLists) {
            if (!tempModels.contains(innerList)) {
                innerList.subTimer();
            }
            if (innerList.getTimer() == 0) {
                tempOutStaff.add(innerList);
            }
        }
        // 更新outer列表
        updateOutStaff(tempOutStaff);
    }

    private void updateOutStaff(List<StaffModel> tempOutStaff) {
        // 将 tempOutStaff 列表中的数据,添加到 outerList 中
        for (StaffModel staffModel : tempOutStaff) {
            filterUtil.exitStaff(staffModel);
        }

        // 刷新列表
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                StringBuffer enterQueue = new StringBuffer();
                for (StaffModel inner : filterUtil.innerLists) {
                    enterQueue.append(inner.getBeaconName() + "\n");
                }
                runMan.setVisibility(View.GONE);
                tvShowInfo.setText("Welcome\n" + enterQueue.toString());
                innerStaffAdapter.notifyDataSetChanged();
                outerStaffAdapter.notifyDataSetChanged();
            }
        });
    }


    // ================================华丽的分割线===================================================

    private void bindData() {
        newListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, newList);
        exitListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, exitList);
        lvNew.setAdapter(newListAdapter);
        lvExit.setAdapter(exitListAdapter);
    }


    @Override
    protected void onStop() {
        super.onStop();
//        sensorManager.stopService();
    }


    private void initView() {
        footView = findViewById(R.id.foot_layout);
        runMan = (ProgressBar) footView.findViewById(R.id.main_man_progressBar);
        tvShowInfo = (TextView) footView.findViewById(R.id.tv_info);
        lvNew = (ListView) findViewById(R.id.lv_new);
        lvExit = (ListView) findViewById(R.id.lv_exit);
        btnSubmit = (Button) findViewById(R.id.btn_submit);
        etInputSecond = (EditText) findViewById(R.id.edit_input_second);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etInputSecond.getText().toString())) {
                    Toast.makeText(MainActivity.this, "您输入的秒数为空", Toast.LENGTH_SHORT).show();
                }


                finalSecond = Integer.valueOf(etInputSecond.getText().toString());
                index = 0;
                Toast.makeText(MainActivity.this, "提交成功，您当前的延时为" + finalSecond + "秒", Toast.LENGTH_SHORT).show();
            }
        });
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                lvNew.addFooterView(footView);
//            }
//        });

    }


    private void init() {
        filterUtil = new FilterUtil(newList/*, exitList*/);
        sensorManager = SensoroManager.getInstance(this);
        if (!sensorManager.isBluetoothEnabled()) {
            Toast.makeText(MainActivity.this, "请检查蓝牙是否可用...", Toast.LENGTH_SHORT).show();
        } else {
            try {
                sensorManager.startService();
                sensorManager.setBeaconManagerListener(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    private void welcomeYou(final String name) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                runMan.setVisibility(View.GONE);
                tvShowInfo.setText("欢迎您的到来\n" + name);
                tvShowInfo.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        runMan.setVisibility(View.VISIBLE);
                        tvShowInfo.setText("Beacon正在努力查找中...");
                    }
                }, 5000);
            }
        });

    }

    @Override
    public void onNewBeacon(final Beacon beacon) {
        if (FilterUtil.whiteList.containsKey(beacon.getMacAddress())) {
            Log.d(TAG, "onNewBeacon: " + beacon.getMacAddress());
            if (!newList.contains(FilterUtil.whiteList.get(beacon.getMacAddress()))) {
                if (exitList.contains(FilterUtil.whiteList.get(beacon.getMacAddress()))) {
                    exitList.remove(FilterUtil.whiteList.get(beacon.getMacAddress()));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            exitListAdapter.notifyDataSetChanged();
                        }
                    });
                }
//                filterUtil.enterNewBeacon(FilterUtil.whiteList.get(beacon.getMacAddress()));
                synchronized (filterUtil.enterQueue) {
//                    filterUtil.enterQueue.add(FilterUtil.whiteList.get(beacon.getMacAddress()));
                }
            }
        }


    }

    @Override
    public void onGoneBeacon(final Beacon beacon) {
        if (FilterUtil.whiteList.containsKey(beacon.getMacAddress())) {
            Log.d(TAG, beacon.getMacAddress());
        }
    }

    List<String> temp = new ArrayList<>();

    @Override
    public void onUpdateBeacon(ArrayList<Beacon> arrayList) {

        if (runMan.getVisibility() == View.GONE) {

        } else {
            if (filterUtil.enterQueue.size() > 0) {
                StringBuffer stringBuffer = new StringBuffer();
                for (String s : filterUtil.enterQueue) {
                    stringBuffer.append(s)
                            .append("\n");
                }
                welcomeYou(stringBuffer.toString());
                filterUtil.enterQueue.clear();
            }
        }
        index++;
        if (index == finalSecond) {
            index = 0;
            temp.clear();
            for (int i = 0; i < arrayList.size(); i++) {
                if (FilterUtil.whiteList.containsKey(arrayList.get(i).getMacAddress())) {
                    newList.remove(FilterUtil.whiteList.get(arrayList.get(i).getMacAddress()));
//                    temp.add(FilterUtil.whiteList.get(arrayList.get(i).getMacAddress()));
                }
            }

            exitList.addAll(newList);

            newList.clear();
            newList.addAll(temp);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    newListAdapter.notifyDataSetChanged();
                    exitListAdapter.notifyDataSetChanged();
                }
            });

        }


    }


}
