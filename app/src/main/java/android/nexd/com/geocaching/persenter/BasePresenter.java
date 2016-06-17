package android.nexd.com.geocaching.persenter;

import android.content.Context;
import android.nexd.com.geocaching.ui.iView.IBaseView;


/**
 * the abstract presenter is the parent class
 *
 * Created by wangxu on 16/4/19.
 */
public abstract class BasePresenter<T extends IBaseView> {

    protected Context context;

    protected T iView;

    public BasePresenter(Context context, T iView) {
        this.context = context;
        this.iView = iView;
    }

    public void init() {
        iView.initView();
    }


    public abstract void release();
}
