package android.nexd.com.geocaching.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.nexd.com.geocaching.R;
import android.nexd.com.geocaching.model.BoothMessage;
import android.nexd.com.geocaching.presenter.SeekPresenter;
import android.nexd.com.geocaching.ui.iView.ISeekPresenterActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qr_codescan.MipcaActivityCapture;

import cn.nexd.map.rendering.SVGMapViewListener;
import cn.nexd.map.rendering.core.componet.SVGMapView;

/**
 * Created by wangxu on 16/8/8.
 */
public class SeekPresentActivity extends BaseActivity<SeekPresenter> implements ISeekPresenterActivity, SVGMapViewListener {
    private static final int SCANNIN_GREQUEST_CODE = 0x1002;

    private static final int INPUT_USERNAME_NUMBER = 0x1003;

    SVGMapView mapView;

    ImageView imageView;

    TextView textView;
    BoothMessage boothMessage;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_seek_present;
    }

    @Override
    protected void initPresenter() {
        presenter = new SeekPresenter(this, this);
        presenter.init();
    }

    @Override
    public void initView() {
        mapView = (SVGMapView) findViewById(R.id.svgMapView);
        imageView = (ImageView) findViewById(R.id.mapView);
        textView = (TextView) findViewById(R.id.poiRoute);

        Intent intent = getIntent();
        boothMessage = intent.getParcelableExtra("message");

        imageView.setImageDrawable(getDrawable(boothMessage.getBoothCoordinate()));
        textView.setText(boothMessage.getPresentRoute());
    }


    private void scanQRCode() {
        Intent intent = new Intent();
        intent.setClass(this, MipcaActivityCapture.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
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
                        if (boothMessage != null) {
                            if (boothMessage.getConvertCode().equals(content)) {
                                this.finish();
                            }
                        } else {
                            showToast("信息不匹配,请继续寻找!");
                        }
                    }
                }
                break;
        }


    }

    private void showToast(String mess) {
        Toast.makeText(SeekPresentActivity.this, mess, Toast.LENGTH_SHORT).show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.scan_scan, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.scan_scan) {
            scanQRCode();
        }
//        switch (item.getItemId()) {
//            case R.menu.scan_scan:
//                // TODO: 16/8/8 wangxu  启动扫一扫按钮
//                scanQRCode();
//                break;
//        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onMapLoadComplete(long l, String s) {
        // 地图加载完成. 当前在子线程
    }

    @Override
    public void onMapLoadError() {
        // 地图加载错误, 当前在子线程
    }

    @Override
    public void onGetCurrentMap(Bitmap bitmap) {

    }
}
