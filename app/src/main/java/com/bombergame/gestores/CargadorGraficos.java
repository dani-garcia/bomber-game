package com.bombergame.gestores;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.SparseArray;

public class CargadorGraficos {

    private static SparseArray<Drawable> drawables = new SparseArray<>();
    private static SparseArray<Bitmap> bitmaps = new SparseArray<>();

    public static Drawable cargarDrawable(Context context, int id) {
        Drawable drawable = drawables.get(id);
        if (drawable != null)
            return drawable;

        drawable = ContextCompat.getDrawable(context, id);
        drawables.put(id, drawable);
        return drawable;
    }

    public static Bitmap cargarBitmap(Context context, int id) {
        Bitmap bitmap = bitmaps.get(id);
        if (bitmap != null)
            return bitmap;

        bitmap = ((BitmapDrawable) ContextCompat.getDrawable(context, id)).getBitmap();
        bitmaps.put(id, bitmap);
        return bitmap;
    }

}
