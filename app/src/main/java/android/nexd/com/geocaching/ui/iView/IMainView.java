package android.nexd.com.geocaching.ui.iView;

/**
 * Created by wangxu on 16/6/14.
 */
public interface IMainView extends IBaseView {


    void updateImageView(String filePath);

    void updateAdapter();

    void showToast(String msg);
}
