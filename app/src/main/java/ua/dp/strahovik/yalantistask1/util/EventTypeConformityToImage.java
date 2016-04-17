package ua.dp.strahovik.yalantistask1.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;

import java.util.HashMap;
import java.util.Map;

import ua.dp.strahovik.yalantistask1.R;

public class EventTypeConformityToImage {
    private static final Map<String, Drawable> sConformityMap = new HashMap<String, Drawable>();

    private static void initMap(Context context) {
        String[] keys = context.getResources().getStringArray(R.array.EventType_array);
        TypedArray values = context.getResources().obtainTypedArray(R.array.EventType_conforming_image_array);
        for (int counter = 0; counter < Math.min(keys.length, values.length()); counter++) {
            sConformityMap.put(keys[counter], values.getDrawable(counter));
        }
        values.recycle();
    }

    public static Map<String, Drawable> getConformityMap(Context context) {
        if (sConformityMap.isEmpty()) {
            initMap(context);
        }
        return sConformityMap;
    }
}
