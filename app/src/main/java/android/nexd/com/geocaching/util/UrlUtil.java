package android.nexd.com.geocaching.util;

import android.text.TextUtils;

/**
 * Created by lawrence on 2015/9/22.
 */
public class UrlUtil {

//    public static final String BASE_URL = "http://192.168.1.251:8080/";
    public static final String BASE_URL = "http://api.nexdtech.com/";


    // 请求结果校验
    public static final String WEB_API_APP_SUCCESS = "1.1.1.1";//  信息获取完成
    public static final String WEB_API_APP_NULL_PARAMETER = "1.1.1.2";//  参数为空
    public static final String WEB_API_APP_NON_RESULT = "1.1.1.3";// 没有对应的信息
    public static final String WEB_API_APP_UN_SUCCESS = "1.1.1.4";// 获取信息异常
    public static final String WEB_API_APP_NEED_UPDATE_PROP = "1.1.1.5";//需要更新配置文件
    public static final String WEB_API_APP_NON_NEED_UPDATE_PROP = "1.1.1.6";//不需要更新配置文件


    public static String getCodeMessage(String msgCode) {
        String codeMessage = "未知异常";
        if (!TextUtils.isEmpty(msgCode)) {
            switch (msgCode) {
                case WEB_API_APP_SUCCESS:
                    codeMessage = "信息获取成功";
                    break;
                case WEB_API_APP_NULL_PARAMETER:
                    codeMessage = "参数为空";
                    break;
                case WEB_API_APP_NON_RESULT:
                    codeMessage = "没有对应的信息";
                    break;
                case WEB_API_APP_UN_SUCCESS:
                    codeMessage = "获取信息异常";
                    break;
                case WEB_API_APP_NEED_UPDATE_PROP:
                    codeMessage = "需要更新配置文件";
                    break;
                case WEB_API_APP_NON_NEED_UPDATE_PROP:
                    codeMessage = "不需要更新配置文件";
                    break;
                default:
                    codeMessage = "";
                    break;
            }
            return codeMessage;
        } else {
            return codeMessage;
        }
    }
}
