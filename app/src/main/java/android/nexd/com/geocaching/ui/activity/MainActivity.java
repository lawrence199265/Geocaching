package android.nexd.com.geocaching.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.nexd.com.geocaching.MorePointOverly;
import android.nexd.com.geocaching.R;
import android.nexd.com.geocaching.model.TargetModel;
import android.nexd.com.geocaching.persenter.MainPresenter;
import android.nexd.com.geocaching.ui.iView.IMainView;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qr_codescan.MipcaActivityCapture;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import cn.nexd.map.rendering.SVGMapViewListener;
import cn.nexd.map.rendering.core.componet.SVGMapView;

public class MainActivity extends BaseActivity<MainPresenter> implements IMainView, View.OnClickListener, SVGMapViewListener {

    private static final int SCANNIN_GREQUEST_CODE = 0x1002;

    private static final int INPUT_USERNAME_NUMBER = 0x1003;

    private boolean isStarted = false;

    private boolean isFinished = false;

    private boolean isNexdMap = false;

    // 任务列表
    ListView beaconList;

    // 任务列表数据源
    List<TargetModel> beacons;

    // 开始任务和结束任务南牛
    Button startEnd;

    // 显示二维码的布局
    ImageView qrCodeImageView;

    // 任务列表 adapter
    BeaconAdapter adapter;

    // 当前任务对象
    TargetModel targetModel;

    // 任务列表的父类布局
    RelativeLayout taskListLayout;

    // map view 的父类布局
    RelativeLayout nexdMapViewLayout;

    // nexd map 地图
    SVGMapView svgMapView;

    // 地图的图层  用来标记各种点
    MorePointOverly morePointOverly;

    // double 数据格式化
    DecimalFormat df = new DecimalFormat("######0.00");

    @Override
    protected int getLayoutResId() {
        return R.layout.target_task;
    }

    @Override
    protected void initPresenter() {
        presenter = new MainPresenter(this, this);
        presenter.init();
    }

    @Override
    public void initView() {
        taskListLayout = (RelativeLayout) findViewById(R.id.task_view_layout);
        nexdMapViewLayout = (RelativeLayout) findViewById(R.id.map_view_layout);
        taskListLayout.setVisibility(View.GONE);
        nexdMapViewLayout.setVisibility(View.VISIBLE);

        beaconList = (ListView) findViewById(R.id.list);
        startEnd = (Button) findViewById(R.id.start_end);
        qrCodeImageView = (ImageView) findViewById(R.id.showImage);
        startEnd.setOnClickListener(this);


        // 初始化任务列表
        beacons = new ArrayList<>();
        adapter = new BeaconAdapter();
        beaconList.setAdapter(adapter);

        beacons.add(new TargetModel("线索一", "0117C596A701", 100D, false, true/*, "0117C596150D\n您已获得线索一\n\n"*/));
        beacons.add(new TargetModel("线索二", "0117C596E2EB", 100D, false, true/*, "0117C5969B91\n您已获得线索二\n\n"*/));
        beacons.add(new TargetModel("线索三", "0117C596F6B3", 100D, false, true/*, "0117C5963E07\n您已获得线索三\n\n"*/));
        beacons.add(new TargetModel("线索四", "0117C5964883", 100D, false, true/*, "0117C596A7CA\n您已获得线索四\n\n"*/));

        presenter.getTaskList(beacons);


        // 初始化地图信息
        svgMapView = (SVGMapView) findViewById(R.id.svgMapView);
        assert svgMapView != null;
        svgMapView.registerMapViewListener(this);
        String filePath = Environment.getExternalStorageDirectory().getPath() + File.separator + "nexd/map/10108061/101080610001.svg";
        try {
            svgMapView.loadMap(new FileInputStream(filePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.activity_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings:

                if (isNexdMap) {
                    taskListLayout.setVisibility(View.GONE);
                    nexdMapViewLayout.setVisibility(View.VISIBLE);
                    item.setTitle(getString(R.string.change_task));
                    isNexdMap = false;
                } else {
                    nexdMapViewLayout.setVisibility(View.GONE);
                    taskListLayout.setVisibility(View.VISIBLE);
                    item.setTitle(getString(R.string.change));
                    isNexdMap = true;
                }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void updateImageView(String filePath) {
        qrCodeImageView.setImageBitmap(BitmapFactory.decodeFile(filePath));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SCANNIN_GREQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    String content = bundle.getString("result");
                    if (content != null) {
                        String[] split = content.split("\n");
                        if (targetModel != null) {
                            if (split[0].equals(targetModel.getMacId())) {
                                targetModel.setFinish(true);
                                targetModel.setAnswer(content);
                                updateAdapter();
                            } else {
                                showToast("信息不匹配,请继续寻找!");
                            }
                        }
                    }
                }
                break;

            case INPUT_USERNAME_NUMBER:
                Bundle bundle = data.getExtras();
                String userName = bundle.getString("userName");
                String telNumber = bundle.getString("telNumber");
                presenter.createQECode(beacons, userName, telNumber);
                isStarted = false;
                isFinished = true;
                startEnd.setText(R.string.end);
                presenter.stopLooking();
                break;
        }
    }


    @Override
    protected void onResume() {
//        presenter.startLooking();
        super.onResume();
    }

    @Override
    protected void onStop() {
        presenter.stopLooking();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        presenter.stopLooking();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        presenter.stopLooking();
        super.onPause();
    }

    private void scanQRCode(TargetModel targetModel) {
        if (!isStarted) return;
        this.targetModel = targetModel;
        Intent intent = new Intent();
        intent.setClass(this, MipcaActivityCapture.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
    }

    @Override
    public void updateAdapter() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_end:
                if (isFinished) return;
                if (isStarted) {
                    int index = 0;
                    for (TargetModel beacon : beacons) {
                        if (beacon.isFinish()) {
                            index++;
                        } else {
                            showToast("当前任务未完成,无法结束");
                            break;
                        }
                    }

                    if (index == beacons.size()) {
                        Intent intent = new Intent(this, InputTypeActivity.class);
                        startActivityForResult(intent, INPUT_USERNAME_NUMBER);
                    }
                } else {
                    isStarted = true;
                    startEnd.setText("寻宝中...");
                    presenter.startLooking();
                }
        }
    }

    @Override
    public void onMapLoadComplete(long l, String s) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                showToast("加载完成");
                if (morePointOverly == null) {
                    morePointOverly = new MorePointOverly(MainActivity.this);
                    morePointOverly.setRes(R.drawable.box);
                }

                List<PointF> boxs = new ArrayList<>();

                boxs.add(new PointF(16.935181f, 6.908142f));
                boxs.add(new PointF(33.466125f, 27.577637f));
                boxs.add(new PointF(33.466125f, 6.908142f));
                boxs.add(new PointF(16.935181f, 27.577637f));
                morePointOverly.setPositions(boxs);
                svgMapView.addOverlay(morePointOverly, 1);

            }
        });
    }

    @Override
    public void onMapLoadError() {
        showToast("加载失败");
    }

    @Override
    public void onGetCurrentMap(Bitmap bitmap) {

    }

    private class BeaconAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return beacons.size();
        }

        @Override
        public TargetModel getItem(int position) {
            return beacons.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(MainActivity.this, R.layout.list_item, null);
                holder = new ViewHolder();
                holder.macId = (TextView) convertView.findViewById(R.id.mac_id);
                holder.distance = (TextView) convertView.findViewById(R.id.distance);
                holder.scanQRCode = (Button) convertView.findViewById(R.id.scan_qr_code);
                holder.scanQRCode.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        scanQRCode(getItem(position));
                    }
                });
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            TargetModel beacon = getItem(position);
            holder.macId.setText(beacon.getName());
            if (!beacon.isFinish()) {
                if (beacon.isApproach()) {
                    holder.distance.setText((df.format(beacon.getDistance()) + getString(R.string.distance)));
                } else {
                    holder.distance.setText(R.string.far_away_100);
                }
            } else {
                holder.distance.setText(R.string.acquired_clues);
            }
            return convertView;
        }
    }

    class ViewHolder {
        TextView macId;

        TextView distance;

        Button scanQRCode;
    }
}
