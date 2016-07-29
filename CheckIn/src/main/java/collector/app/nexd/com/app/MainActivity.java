package collector.app.nexd.com.app;


import com.sensoro.cloud.SensoroManager;

import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.sensoro.beacon.kit.Beacon;
import com.sensoro.beacon.kit.BeaconManagerListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Filter;

import collector.app.nexd.com.app.model.TargetModel;

public class MainActivity extends AppCompatActivity implements BeaconManagerListener {
    private static final String TAG = "MainActivity";
    private SensoroManager sensorManager;
    private Map<String, String> whiteList = new HashMap<>();
    private Map<String, TargetModel> resultBeacon = new HashMap<>();
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
    private int finalSecond = 60;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        initView();
        bindData();
    }

    private void bindData() {
        newListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, newList);
        exitListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, exitList);
        lvNew.setAdapter(newListAdapter);
        lvExit.setAdapter(exitListAdapter);
    }


    @Override
    protected void onStop() {
        super.onStop();
        sensorManager.stopService();
    }


    private void initView() {
        footView = LayoutInflater.from(this).inflate(R.layout.foot_layout, null);
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
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                lvNew.addFooterView(footView);
            }
        });

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
                filterUtil.enterNewBeacon(FilterUtil.whiteList.get(beacon.getMacAddress()));
                synchronized (filterUtil.enterQueue) {
                    filterUtil.enterQueue.add(FilterUtil.whiteList.get(beacon.getMacAddress()));
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
                    temp.add(FilterUtil.whiteList.get(arrayList.get(i).getMacAddress()));
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
