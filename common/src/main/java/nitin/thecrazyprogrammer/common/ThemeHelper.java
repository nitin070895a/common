package nitin.thecrazyprogrammer.common;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.TypedValue;

public class ThemeHelper {

    public int fetchAccentColor(Context context) {
        TypedValue typedValue = new TypedValue();

        TypedArray a = context.obtainStyledAttributes(typedValue.data, new int[] { R.attr.colorAccent });
        int color = a.getColor(0, 0);

        a.recycle();

        return color;
    }
    public int fetchTextColorPrimary(Context context) {
        TypedValue typedValue = new TypedValue();

        TypedArray a = context.obtainStyledAttributes(typedValue.data, new int[] { android.R.attr.textColorPrimary });
        int color = a.getColor(0, 0);

        a.recycle();

        return color;
    }
    public int fetchTextColorSecondary(Context context) {
        TypedValue typedValue = new TypedValue();

        TypedArray a = context.obtainStyledAttributes(typedValue.data, new int[] { android.R.attr.textColorSecondary });
        int color = a.getColor(0, 0);

        a.recycle();

        return color;
    }
    public int fetchPrimaryColor(Context context) {
        TypedValue typedValue = new TypedValue();

        TypedArray a = context.obtainStyledAttributes(typedValue.data, new int[] { R.attr.colorPrimary });
        int color = a.getColor(0, 0);

        a.recycle();

        return color;
    }
    public int fetchPrimaryDarkColor(Context context) {
        TypedValue typedValue = new TypedValue();

        TypedArray a = context.obtainStyledAttributes(typedValue.data, new int[] { R.attr.colorPrimaryDark });
        int color = a.getColor(0, 0);

        a.recycle();

        return color;
    }
    public int fetchBackgroundColor(Context context) {
        TypedValue typedValue = new TypedValue();

        TypedArray a = context.obtainStyledAttributes(typedValue.data, new int[] { android.R.attr.colorBackground });
        int color = a.getColor(0, 0);

        a.recycle();

        return color;
    }
    public int fetchHighlightColor(Context context) {
        TypedValue typedValue = new TypedValue();
        int color = 0;
        TypedArray a;

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                a = context.obtainStyledAttributes(typedValue.data, new int[] { android.R.attr.colorControlHighlight });
                color = a.getColor(0, 0);
                a.recycle();
            }
            else
                throw new Exception();
        }
        catch (Exception e){
            color = context.getResources().getColor(R.color.gray_highlight);
            e.printStackTrace();
        }


        return color;
    }
}
