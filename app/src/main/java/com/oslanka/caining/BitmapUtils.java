package com.oslanka.caining;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.view.View;

/**
 * Created by caining on 16/3/28.
 */
public class BitmapUtils {
    private static BitmapUtils bitmapUtils;

    public static BitmapUtils getInstance() {
        if (null == bitmapUtils) bitmapUtils = new BitmapUtils();
        return bitmapUtils;
    }

    public  Bitmap getBitmapFromView(View getres) {
        getres.setDrawingCacheEnabled(true);
        getres.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        getres.layout(0, 0, getres.getMeasuredWidth(),
                getres.getMeasuredHeight());
        getres.buildDrawingCache();
        return getres.getDrawingCache();
    }
    public  Bitmap drawableToBitmap(Drawable drawable) {

        Bitmap bitmap = Bitmap.createBitmap(

                1,

                1,

                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888

                        : Bitmap.Config.RGB_565);

        Canvas canvas = new Canvas(bitmap);

        //canvas.setBitmap(bitmap);

        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());

        drawable.draw(canvas);

        return bitmap;

    }
}
