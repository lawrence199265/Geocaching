package android.nexd.com.geocaching.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 二维码生成工具类
 * <p/>
 * Created by wangxu on 16/6/15.
 */
public class QRCodeUtil {
    /**
     * @param content   内容
     * @param widthPix  图片宽度
     * @param heightPix 图片高度
     * @param logoBm    二维码中心logo,可以为null
     * @param filePath  生成二维码的保存地址
     * @return 是否保存文件成功
     */
    public static boolean createQRImage(String content, int widthPix, int heightPix, Bitmap logoBm, String filePath) {

        try {
            if (content == null || "".equals(content)) {
                return false;
            }

            // 配置参数
            Map hints = new HashMap();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            // 容错级别
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            // 设置空白边距的宽度
            hints.put(EncodeHintType.MARGIN, 2); //default is 4

            // 图像数据转换,使用了矩阵转换
            BitMatrix bitMatrix = new QRCodeWriter().encode(
                    content, BarcodeFormat.QR_CODE, widthPix, heightPix, hints);
            int[] pixels = new int[widthPix * heightPix];
            // 这里按照二维码的算法,逐个生成二维码图片
            // 两个for循环是图片横列扫描的结果
            for (int y = 0; y < heightPix; y++) {
                for (int x = 0; x < widthPix; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * widthPix + x] = 0xff000000;
                    } else {
                        pixels[y * widthPix + x] = 0xffffffff;
                    }
                }
            }

            // 生成二维码图片的格式,使用argb-8888
            Bitmap bitmap = Bitmap.createBitmap(widthPix, heightPix, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, widthPix, 0, 0, widthPix, heightPix);


            if (logoBm != null) {
                bitmap = addLogo(bitmap, logoBm);
            }

            // 必须使用 compress 方法将 bitmap 保存到文件中,在进行读取. 直接返回的 bitmap 是没有任何压缩的, 内存消耗巨大!

            return bitmap != null && bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(FileUtil.buildFile(filePath, false)));

        } catch (WriterException | FileNotFoundException e) {
            e.printStackTrace();
        }

        return false;
    }


    /**
     * 在二维码中间添加 Logo 图案
     *
     * @param srcBitmap 二维码
     * @param logoBm    logo
     * @return 合并的二维码
     */
    private static Bitmap addLogo(Bitmap srcBitmap, Bitmap logoBm) {
        if (srcBitmap == null) {
            return null;
        }

        if (logoBm == null) {
            return srcBitmap;
        }

        // 获取 bitmap/logo 宽高
        int srcBitmapWidth = srcBitmap.getWidth();
        int srcBitmapHeight = srcBitmap.getHeight();
        int logoBmWidth = logoBm.getWidth();
        int logoBmHeight = logoBm.getHeight();

        if (srcBitmapWidth == 0 || srcBitmapHeight == 0) {
            return null;
        }

        if (logoBmHeight == 0 || logoBmWidth == 0) {
            return srcBitmap;
        }

        float scaleFactor = srcBitmapWidth * 1.0f / 5 / logoBmWidth;
        Bitmap bitmap = Bitmap.createBitmap(srcBitmapWidth, srcBitmapHeight, Bitmap.Config.ARGB_8888);
        try {
            Canvas canvas = new Canvas(bitmap);
            canvas.drawBitmap(bitmap, 0, 0, null);
            canvas.scale(scaleFactor, scaleFactor, srcBitmapWidth / 2, srcBitmapHeight / 2);
            canvas.drawBitmap(logoBm, (srcBitmapWidth - logoBmWidth) / 2, (srcBitmapHeight - logoBmHeight) / 2, null);

            canvas.save(Canvas.ALL_SAVE_FLAG);
            canvas.restore();

        } catch (Exception e) {
            bitmap = null;
            e.getStackTrace();
        }

        return bitmap;

    }
}

