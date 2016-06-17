package android.nexd.com.geocaching;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

import cn.nexd.map.rendering.core.SVGMapBaseOverlay;

/**
 * POI搜索图层
 * Created by lawrence on 2015/10/14.
 */
public class MorePointOverly extends SVGMapBaseOverlay {


    private List<PointF> positions;
    private Context context;
    private int res;

    public MorePointOverly(Context context) {
        this.context = context;
        positions = new ArrayList<>();
        this.showLevel = LOCATION_LEVEL - 1;
    }

    public void setRes(int res) {
        this.res = res;
    }

    public void setPositions(List<PointF> positions) {
        if (this.positions != null && this.positions.size() > 0) {
            this.positions.clear();
        }
        assert this.positions != null;
        this.positions.addAll(positions);
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

    @Override
    public void onTap(MotionEvent motionEvent) {

    }

    @Override
    public void draw(Canvas canvas, Matrix matrix, float v, float v1) {
        canvas.save();
        if (this.isVisible && this.positions != null) {
            for (PointF pointF : positions) {
                float[] point = new float[]{pointF.x, pointF.y};
                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), res);
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();
                int newWidth = width / 2;
                matrix.mapPoints(point);
                canvas.drawBitmap(bitmap, point[0] - newWidth, point[1] - height, null);
            }
        }
        canvas.restore();
    }
}
