package android.nexd.com.geocaching.util;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * 
 * com.pancou.sdk.utils
 * 
 * @author ZhaoYongchun(Bruce) << zhaoyongchun@pancou.com >>
 * @version v1.0
 * @since v1.0
 * */

public class ProgressDialogUtils {
	private static ProgressDialog progressDialog;
	private Context c;

	public static void showProgressDialog(Context context, String msg,
			String title, boolean cancel) {

		progressDialog = new ProgressDialog(context);
		// 圆形
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		if (title != null) {
			progressDialog.setTitle(title);
		}
		if (msg != null) {
			progressDialog.setMessage(msg);
		}
		progressDialog.setIndeterminate(false);
		progressDialog.setCancelable(cancel);
		progressDialog.show();
	}

	public static void dismissProgressDialog(Context context) {
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
	}

}
