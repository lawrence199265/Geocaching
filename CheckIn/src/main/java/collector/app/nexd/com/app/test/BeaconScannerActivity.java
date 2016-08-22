package collector.app.nexd.com.app.test;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.tencent.bugly.crashreport.CrashReport;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.nexd.sdk.collector.NexdCollectorAgent;
import cn.nexd.sdk.collector.NexdCollectorConfiguration;
import cn.nexd.sdk.collector.NexdCollectorResult;
import cn.nexd.sdk.collector.NexdCollectorResultListener;
import collector.app.nexd.com.app.R;

/**
 * Created by wangxu on 16/8/16.
 */
public class BeaconScannerActivity extends Activity {

    private static final String TAG = "BeaconScannerActivity";


    private List<String> whiteLists = new ArrayList<>();

    ListView viewById;
    private TextView _30Seconds;
    private TextView _60Seconds;

    private final List<Integer> _30SecondsLists = new ArrayList<>();
    private final List<Integer> _60SecondsLists = new ArrayList<>();

    NexdCollectorAgent collectorAgent;
    List<NexdCollectorResult> results;
    List<NexdCollectorResult> result = new ArrayList<>();
    int index = 0;

    private boolean isContain = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_beacon_scanner);
        initDebug();

        whiteLists.add("F1:CF:E6:50:61:04");

        this.viewById = (ListView) findViewById(R.id.listview_beacons);
        this._30Seconds = (TextView) findViewById(R.id._30second);
        this._60Seconds = (TextView) findViewById(R.id._60second);
        // 初始化采集业务
        collectorAgent = NexdCollectorAgent.getCollectorAgent(this);
        NexdCollectorConfiguration.Buidler buidler = new NexdCollectorConfiguration.Buidler();
        buidler.setAppkey("").setBeaconCollectorDelay(1000);
        buidler.setBeaconCollectorRate(1000);
        buidler.setBeaconCollectorEnable(true);
        buidler.setDebugMode(true);
        buidler.setWifiCollectorEnable(false);
        buidler.setBeaconResultCacheTime(3000);

        final BeaconAdapter adapter = new BeaconAdapter();

        // 开始采集,  采集中的临时集合尽量不要提取处理啊,有可能会影响业务
        collectorAgent.startCollector(buidler.build(), new NexdCollectorResultListener() {
            @Override
            public void onCollectorChanged(final List<NexdCollectorResult> list) {
                if (list.get(0).getStateCode() == NexdCollectorResult.ERROR_CODE_COLLECTOR_SUCCESS) {
                    Log.d(TAG, "onCollectorChanged: " + list.size());
                    for (NexdCollectorResult nexdCollectorResult : list) {
                        if (whiteLists.get(0).equals(nexdCollectorResult.getSingleSourceAddress())) {
                            result.clear();
                            result.add(nexdCollectorResult);

                            isContain = true;
                        }
                    }

                    index++;


                    if (!isContain) {
                        synchronized (_30SecondsLists) {
                            _30SecondsLists.add(0);
                        }
                        synchronized (_60SecondsLists) {
                            _60SecondsLists.add(1);
                        }
                        isContain = false;
                    } else {
                        synchronized (_30SecondsLists) {
                            _30SecondsLists.add(1);
                        }
                        synchronized (_60SecondsLists) {
                            _60SecondsLists.add(0);
                        }
                    }


                    if (index == 30) {
                        new Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        synchronized (_30SecondsLists) {
                                            if (_30SecondsLists.size() != 0) {
                                                _30SecondsLists.remove(0);
                                            }
                                            _30Seconds.setText(String.valueOf(counter_1(_30SecondsLists)));
                                        }
                                    }
                                });
                            }
                        }, 0, 1000);
                    }

                    if (index == 60) {
                        new Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        synchronized (_60SecondsLists) {
                                            if (_60SecondsLists.size() != 0) {
                                                _60SecondsLists.remove(0);
                                            }
                                            _60Seconds.setText(String.valueOf(counter_0(_60SecondsLists)));
                                        }
                                    }
                                });
                            }
                        }, 0, 1000);
                    }


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.setResults(result);

                        }
                    });
                }
            }
        });
        viewById.setAdapter(adapter);
    }

    private synchronized int counter_1(List<Integer> list) {
        int count = 0;
        for (Integer integer : list) {
            Log.d(TAG, "counter: " + integer);
            if (integer == 1) {
                count++;
            }
        }

        return count;
    }

    private synchronized int counter_0(List<Integer> list) {
        int count = 0;
        for (Integer integer : list) {
            Log.d(TAG, "counter: " + integer);
            if (integer == 0) {
                count++;
            }
        }

        return count;
    }


    private void initDebug() {
        CrashReport.initCrashReport(getApplicationContext());
    }


    class BeaconAdapter extends BaseAdapter {
        List<NexdCollectorResult> results = new ArrayList<>();

        public void setResults(List results) {
            this.results.clear();
            this.results.addAll(results);
            results.clear();
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return results.size();
        }

        @Override
        public NexdCollectorResult getItem(int position) {
            return results.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(BeaconScannerActivity.this).inflate(R.layout.layout_beacon_list_item, null);
                viewHolder = new ViewHolder();
                viewHolder.address = (TextView) convertView.findViewById(R.id.address);
                viewHolder.name = (TextView) convertView.findViewById(R.id.name);
                viewHolder.rssi = (TextView) convertView.findViewById(R.id.rssi);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.address.setText(getItem(position).getSingleSourceAddress());
            viewHolder.name.setText(getItem(position).getSingleSourceName());
            viewHolder.rssi.setText(String.valueOf(getItem(position).getSingleSourceStrength()));
            return convertView;
        }


        class ViewHolder {
            TextView address;
            TextView name;
            TextView rssi;
        }
    }
}
