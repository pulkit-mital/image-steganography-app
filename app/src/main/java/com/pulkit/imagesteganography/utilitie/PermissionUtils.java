package com.pulkit.imagesteganography.utilitie;

import android.content.Context;
import android.content.pm.PackageManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class PermissionUtils {

    private AppCompatActivity context;
    private List<String> ungrantedPermissions;

    public PermissionUtils(AppCompatActivity context) {
        this.context = context;
        this.ungrantedPermissions = new ArrayList<>();
    }

    public boolean isPermissionGranted(String[] permissions) {
        checkUnGrantedPermissions(permissions);
        return ungrantedPermissions.size() == 0;
    }

    private void checkUnGrantedPermissions(String... permissions) {
        ungrantedPermissions.clear();
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    ungrantedPermissions.add(permission);
                }
            }
        }
    }

    public void showPermissionDialog(int requestCode) {
        if (ungrantedPermissions != null && ungrantedPermissions.size() != 0) {
            ActivityCompat.requestPermissions(context, ungrantedPermissions.toArray(new String[ungrantedPermissions.size()]), requestCode);
        }
    }
}
