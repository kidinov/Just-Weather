package org.kidinov.just_weather.util.remote;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

public class UIUtil {
    public static Drawable getDrawableByFileName(Context context, String iconNum) {
        Resources resources = context.getResources();
        final int resourceId = resources.getIdentifier(iconNum, "drawable", context.getPackageName());
        return ContextCompat.getDrawable(context, resourceId);
    }
}
