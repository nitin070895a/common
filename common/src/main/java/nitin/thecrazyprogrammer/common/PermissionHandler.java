package nitin.thecrazyprogrammer.common;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;

/**
 * Permissions handler for activities and fragments
 * It will handle denial and force denial cases as well
 *
 * Created by Nitin on 20/07/18.
 */
public class PermissionHandler {
    private final int REQUEST_CODE_SETTINGS = 101;

    // if the permission requester is a fragment or an activity
    private Boolean isFragment;
    // fragment which is requesting the permissions
    private Fragment fragment;

    // activity instance of the requester
    private Activity activity;

    // the request code for requesting the permissions
    private int requestCode;

    private int dialogTheme, accentColor;
    private PermissionHandlerCallbacks callbacks;

    // all the necessary permissions without which the activity/fragment can't perform anything
    private String[] necessaryPermissions;

    // message to be shown in dialog when any of the necessary permission is denied
    private String necessaryPermissionsMsg;

    /**
     * Initializes the permission handler
     *
     * @param activity activity instance of the requester
     * @param dialogTheme the dialog theme in which the messages will be shown
     * @param accentColor the accent color of that activity
     * @param callbacks who is listening for permission handler results
     * @param necessaryPermissions all the necessary permissions without which the activity/fragment can't perform anything
     * @param necessaryPermissionsMsg message to be shown in dialog when any of the necessary permission is denied
     */
    public PermissionHandler(Activity activity, int dialogTheme, int accentColor, PermissionHandlerCallbacks callbacks,
                             String[] necessaryPermissions, String necessaryPermissionsMsg){
        this.activity = activity;
        this.dialogTheme = dialogTheme;
        this.accentColor = accentColor;
        this.callbacks = callbacks;
        this.necessaryPermissions = necessaryPermissions;
        this.necessaryPermissionsMsg = necessaryPermissionsMsg;
    }

    /**
     * Initiates the permission request, if the requester is an activity it uses
     * {@link ActivityCompat#requestPermissions(Activity, String[], int)},
     * if it's a fragment it uses {@link Fragment#requestPermissions(String[], int)}
     * if all the permissions are granted before it returns the callback method
     * {@link PermissionHandlerCallbacks#allPermissionsGranted()}
     *
     * @param permissions all the permissions that should be requested (may contain optional ones as well)
     * @param requestCode the request code for which the caller will be listening
     * @param isFragment if the requester is a fragment or an activity
     * @param fragment fragment which is requesting the permissions (if above param is true) otherwise null
     */
    public void askForPermissions(String[] permissions, int requestCode, boolean isFragment, Fragment fragment){
        this.requestCode = requestCode;
        this.isFragment = isFragment;
        this.fragment = fragment;

        boolean ask = false;
        for(String permission: permissions){
            if(ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED){
                ask = true;
                break;
            }
        }

        if(ask){
            if(!isFragment) ActivityCompat.requestPermissions(activity, permissions, requestCode);
            else fragment.requestPermissions(permissions, requestCode);
        }
        else
            callbacks.allPermissionsGranted();
    }

    public void handleResponse(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        if(Build.VERSION.SDK_INT < 23)
            return;

        if(requestCode == this.requestCode){
            if (grantResults.length > 0) {

                boolean allNecessaryGranted = true;
                for(String necessaryPermission: necessaryPermissions){
                    for(int i=0; i<permissions.length; i++){
                        if(necessaryPermission.equals(permissions[i])){
                            allNecessaryGranted = allNecessaryGranted && grantResults[i] == PackageManager.PERMISSION_GRANTED;
                        }
                    }
                }

                if (allNecessaryGranted) {
                    callbacks.allPermissionsGranted();
                }
                else {
                    // either one of the necessary permission is not granted, we will ask for permissions again if
                    // the user haven't force denied the permissions otherwise it will redirect the user to the settings
                    // page of our app's permissions

                    boolean shouldAskAgain = false;
                    for(String permission: necessaryPermissions)
                        shouldAskAgain = shouldAskAgain ||  activity.shouldShowRequestPermissionRationale(permission);

                    if (shouldAskAgain) {
                        // user didn't force denied either of the necessary permissions, ask necessary permissions again

                        showDialog(necessaryPermissionsMsg

                                , activity.getString(R.string.grant), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        if(!isFragment) ActivityCompat.requestPermissions(activity, necessaryPermissions,  PermissionHandler.this.requestCode);
                                        else fragment.requestPermissions(necessaryPermissions,  PermissionHandler.this.requestCode);
                                    }
                                }
                                , activity.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        closeCozNoPermission();
                                    }
                                });

                    } else {
                        // user force denied either of the necessary permissions, take him to settings

                        showDialog(activity.getString(R.string.msg_how_to_give_permission_from_settings)

                                , activity.getString(R.string.ok), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        Intent intent = new Intent();
                                        intent.setAction(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                                        intent.setData(uri);
                                        try {
                                            activity.startActivityForResult(intent, REQUEST_CODE_SETTINGS);
                                        } catch (ActivityNotFoundException e) {
                                            activity.startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), REQUEST_CODE_SETTINGS);
                                        }

                                        closeCozNoPermission();
                                    }
                                }
                                , activity.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        closeCozNoPermission();
                                    }
                                });
                    }

                }
            }
        }
    }

    /**
     * At least one of the necessary permissions has not be granted
     * so close the activity by showing a toast permissions required
     */
    private void closeCozNoPermission(){

        HelperFunctions.showToast(activity, activity.getString(R.string.permission_required));
        activity.finish();
    }

    /**
     * Show a dialog regarding the permissions like a dialog when user
     * deny a permission or when force deny it
     *
     * @param msg the message to be shown in the dialog
     * @param pos_string text of positive button
     * @param posClick click action of positive button
     * @param neg_string text of negative button
     * @param negclick click action of negative button
     */
    private void showDialog(String msg, String pos_string, DialogInterface.OnClickListener posClick,
                            String neg_string, DialogInterface.OnClickListener negclick){

        final AlertDialog dialog = new AlertDialog.Builder(new ContextThemeWrapper(activity, dialogTheme))
                .setCancelable(false)
                .setMessage(msg)
                .setPositiveButton(pos_string, posClick)
                .setNegativeButton(neg_string, negclick)
                .create();

        dialog.show();

        dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(accentColor);
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(accentColor);
    }

    /**
     * Callback that tells when all the permissions were granted
     */
    public interface PermissionHandlerCallbacks{

        /**
         * All necessary permissions that were passed in the constructor
         * have been granted, now you can do your loading work inside this
         * callback function.
         */
        void allPermissionsGranted();
    }
}
