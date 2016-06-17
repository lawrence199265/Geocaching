package android.nexd.com.geocaching;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.view.MotionEvent;

import cn.nexd.map.rendering.core.SVGMapBaseOverlay;

/**
 * Created by lawrence on 2015/12/28.
 */
public class MarkerOverlay extends SVGMapBaseOverlay {
    private PointF position;
    private Context context;
    private int res;

    public MarkerOverlay(Context context) {
        this.context = context;
        position = new PointF();
        this.showLevel = LOCATION_LEVEL - 1;
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {

    }


    public void setPosition(PointF position) {
        this.position = position;
    }


    @Override
    public void onTap(MotionEvent motionEvent) {

    }

    @Override
    public void draw(Canvas canvas, Matrix matrix, float v, float v1) {
        canvas.save();
        float[] point = new float[]{position.x, position.y};
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), res);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int newWidth = width / 2;
        matrix.mapPoints(point);
        canvas.drawBitmap(bitmap, point[0] - newWidth, point[1] - height, null);
        canvas.restore();
    }

    public void setRes(int res) {
        this.res = res;
    }
}
