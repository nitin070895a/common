package nitin.thecrazyprogrammer.common;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Nitin on 12/06/18.
 */

public class HelperFunctions {

    public static void showLog(String msg){
        Log.e("Hurray", msg);
    }

    public static void showToast(Context context, String msg){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
