package android.nexd.com.geocaching.ui.activity;

import android.nexd.com.geocaching.persenter.BasePresenter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import butterknife.ButterKnife;


/**
 * Created by wangxu on 16/4/19.
 */
public abstract class BaseActivity<T extends BasePresenter> extends AppCompatActivity {

    protected String TAG = this.getClass().getSimpleName();

    protected T presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        ButterKnife.bind(this);
        initPresenter();
        Log.i(TAG, "onCreate");
    }

    protected abstract int getLayoutResId();

    protected abstract void initPresenter();

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestory");
    }
}
