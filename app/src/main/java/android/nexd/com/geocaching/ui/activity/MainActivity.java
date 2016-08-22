package android.nexd.com.geocaching.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.nexd.com.geocaching.BoothPresentAdapter;
import android.nexd.com.geocaching.MorePointOverly;
import android.nexd.com.geocaching.R;
import android.nexd.com.geocaching.model.BoothMessage;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import cn.nexd.map.rendering.core.componet.SVGMapView;

public class MainActivity extends Activity {

    private boolean isNexdMap = true;

    // 任务列表
    ListView beaconList;

    // 任务列表数据源
    List<BoothMessage> beacons;

    // 开始任务和结束任务南牛
    Button startEnd;

    // 显示二维码的布局
    ImageView qrCodeImageView;

    // 任务列表 adapter
    BoothPresentAdapter adapter;

    // 当前任务对象
    BoothMessage boothMessage;

    // 任务列表的父类布局
    RelativeLayout taskListLayout;

    // map view 的父类布局
    RelativeLayout nexdMapViewLayout;

    // nexd map 地图
    SVGMapView svgMapView;

    // 地图的图层  用来标记各种点
    MorePointOverly morePointOverly;

    private static final String TAG = "MainActivity";

    // double 数据格式化
    DecimalFormat df = new DecimalFormat("######0.00");

    private int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.target_task);
        initView();
    }


    //    @Override
    private void initView() {
        taskListLayout = (RelativeLayout) findViewById(R.id.task_view_layout);

        beaconList = (ListView) findViewById(R.id.list);
        startEnd = (Button) findViewById(R.id.start_end);
        qrCodeImageView = (ImageView) findViewById(R.id.showImage);

        Log.d(TAG, "initView: " + ++index);

        // 初始化任务列表
        beacons = new ArrayList<>();
        adapter = new BoothPresentAdapter(this);
        beaconList.setAdapter(adapter);

        beacons.add(new BoothMessage("宁磊（上海）服饰有限公司", "白日依山尽", "海贼王手办", R.drawable.route_plan_1, " 直行3m\n 右转\n 绕中央区域环形至 B 区展位\n "));
        beacons.add(new BoothMessage("A & C Company Limited", "黄河入海流", "珍美礼品", R.drawable.route_plan_2, " 直行5m\n 右转\n 绕中央区域环形至 D 区展位\n "));
        beacons.add(new BoothMessage("友宁贸易有限公司", "欲穷千里目", "自拍杆", R.drawable.route_plan_3, " 直行5m\n 右转\n 绕中央区域环形至 C 区展位\n "));
        beacons.add(new BoothMessage("Allied Asia Enterprise (PVT) Ltd", "更上一层楼", "小米WiFi", R.drawable.route_plan_4, " 直行5m\n 右转\n 绕中央区域环形至 A 区展位\n "));
        adapter.setLists(beacons);
        Log.d(TAG, "initView: " + ++index);
        beaconList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BoothMessage message = adapter.getItem(position);
                Intent intent = new Intent(MainActivity.this, SeekPresentActivity.class);
                intent.putExtra("message", message);
                startActivity(intent);
            }
        });

    }

//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//
//        MenuInflater menuInflater = getMenuInflater();
//        menuInflater.inflate(R.menu.activity_main, menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.menu_settings:
//
//                if (isNexdMap) {
//                    taskListLayout.setVisibility(View.GONE);
//                    nexdMapViewLayout.setVisibility(View.VISIBLE);
//                    item.setTitle(getString(R.string.change_task));
//                    isNexdMap = false;
//                } else {
//                    nexdMapViewLayout.setVisibility(View.GONE);
//                    taskListLayout.setVisibility(View.VISIBLE);
//                    item.setTitle(getString(R.string.change));
//                    isNexdMap = true;
//                }
//        }
//        return super.onOptionsItemSelected(item);
//    }

    public void updateImageView(String filePath) {
        qrCodeImageView.setImageBitmap(BitmapFactory.decodeFile(filePath));
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    public void updateAdapter() {
//        adapter.notifyDataSetChanged();
    }

    public void showToast(String msg) {
        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

}
