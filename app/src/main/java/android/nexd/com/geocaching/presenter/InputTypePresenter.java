package android.nexd.com.geocaching.presenter;

import android.content.Context;
import android.nexd.com.geocaching.ui.iView.IInputTypeView;

/**
 * Created by wangxu on 16/6/15.
 */
public class InputTypePresenter extends BasePresenter<IInputTypeView> {

    public InputTypePresenter(Context context, IInputTypeView iView) {
        super(context, iView);
    }

    @Override
    public void release() {

    }
}
