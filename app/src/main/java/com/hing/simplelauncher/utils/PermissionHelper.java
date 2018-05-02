package com.hing.simplelauncher.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

import java.util.ArrayList;

/**
 * Created by HingTang on 5/1/18.
 */
public class PermissionHelper {
    public static boolean hasPermission(Context context, String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return (ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED);
        }
        return true;
    }

    public static ArrayList findUnAskedPermissions(Context context, ArrayList<String> permissionList) {
        ArrayList result = new ArrayList();
        for (String permission : permissionList) {
            if (!hasPermission(context, permission)) {
                result.add(permission);
            }
        }
        return result;
    }
}
