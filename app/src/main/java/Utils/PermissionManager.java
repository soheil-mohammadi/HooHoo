package Utils;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import Listeners.OnCustomClick;
import Listeners.OnCustomDialog;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import life.sabujak.roundedbutton.RoundedButton;
import servers.monitor.fastest.hoohoonew.App;
import servers.monitor.fastest.hoohoonew.R;

public class PermissionManager {


    private static final String TAG = "PermissionManager";

    public Map<Integer , OnPermissionResult> permissions = new LinkedHashMap<>();

    public static final int STAOREG = 10 ;
    public static final int LOCATION = 12 ;
    public static final int CAMERA = 14 ;

    private static PermissionManager instance ;

    public static PermissionManager builder() {
        if(instance == null)
            instance = new PermissionManager();

        return instance ;
    }

    public boolean isGranted(int type) {

        String permission = "";

        switch (type) {

            case STAOREG :
                permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
                break;

            case LOCATION :
                permission = Manifest.permission.ACCESS_COARSE_LOCATION;
                break;

            case CAMERA :
                permission = Manifest.permission.CAMERA;
                break;

        }


        return ContextCompat.checkSelfPermission(App.getInstance().getCurrentActivity() ,
                permission) == PackageManager.PERMISSION_GRANTED ;
    }


    public interface  OnPermissionResult  {

        void onAccepted();
        void onDeny();
    }



    public void grant(int type , OnPermissionResult onPermissionResult) {

        if(permissions.size() > 0 ) {

            if(!permissions.containsKey(type)) {
                permissions.put(type , onPermissionResult);
            }


        } else {
            permissions.put(type , onPermissionResult);

            switch (type) {

                case STAOREG:
                    grantStorage();
                    break;


                case LOCATION:
                    grantLocation();
                    break;

                case CAMERA:
                    grantCamera();
                    break;
            }

        }


    }


    private void checkPendingPermissions() {

        if(permissions.size() > 0) {
            int type = permissions.entrySet().iterator().next().getKey();
            switch (type) {

                case STAOREG:
                    grantStorage();
                    break;


                case LOCATION:
                    grantLocation();
                    break;


                case CAMERA:
                    grantCamera();
                    break;
            }
        }
    }

    private void showInfoDialog(String desc , OnCustomClick onAccept , OnCustomClick onDenyClick) {

        App.getInstance().showCustomDialog(R.layout.dialog_permission, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, false, false, new OnCustomDialog() {
                    @Override
                    public void doSomeThing(Dialog dialog) {
                        TextView txt_dialog_permission = dialog.findViewById(R.id.txt_dialog_permission);
                        RoundedButton btn_accept_permission_dialog = dialog.findViewById(R.id.btn_accept_permission_dialog);
                        RoundedButton btn_deny_permission_dialog = dialog.findViewById(R.id.btn_deny_permission_dialog);

                        txt_dialog_permission.setText(desc);
                        btn_accept_permission_dialog.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                onAccept.onClicked(null);
                                dialog.dismiss();
                            }
                        });


                        btn_deny_permission_dialog.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                onDenyClick.onClicked(null);
                                dialog.dismiss();
                            }
                        });
                    }
                });
    }


    public void grantStorage () {
        if(ContextCompat.checkSelfPermission(App.getInstance().getCurrentActivity() ,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            showInfoDialog(App.getInstance().getResString(R.string.hoohoo_need_storage_permission),
                    new OnCustomClick() {
                        @Override
                        public void onClicked(Object object) {
                            ActivityCompat.requestPermissions(App.getInstance().getCurrentActivity() ,
                                    new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE} , STAOREG);
                        }
                    }, new OnCustomClick() {
                        @Override
                        public void onClicked(Object object) {
                            notifyResultDeny(STAOREG);
                        }
                    });


        }else {
            notifyResultAccepted(STAOREG);
        }
    }


    private void remove(int type) {
        permissions.remove(type);
        checkPendingPermissions();
    }


    private void grantLocation () {
        if(ContextCompat.checkSelfPermission(App.getInstance().getCurrentActivity() ,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            showInfoDialog(App.getInstance().getResString(R.string.hoohoo_need_location_permission),
                    new OnCustomClick() {
                        @Override
                        public void onClicked(Object object) {
                            ActivityCompat.requestPermissions(App.getInstance().getCurrentActivity() ,
                                    new String[] {Manifest.permission.ACCESS_COARSE_LOCATION} , LOCATION);
                        }
                    }, new OnCustomClick() {
                        @Override
                        public void onClicked(Object object) {
                            notifyResultDeny(LOCATION);
                        }
                    });


        }else {
            notifyResultAccepted(LOCATION);
        }
    }


    private void grantCamera() {
        if(ContextCompat.checkSelfPermission(App.getInstance().getCurrentActivity() ,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(App.getInstance().getCurrentActivity() ,
                    new String[] {Manifest.permission.CAMERA} , CAMERA);

        }else {
            notifyResultAccepted(CAMERA);
        }
    }


    private void notifyResultAccepted(int type) {
        OnPermissionResult onPermissionResult = permissions.get(type);

        if(onPermissionResult != null) {
            onPermissionResult.onAccepted();
            remove(type);
        }
    }

    private void notifyResultDeny(int type) {
        OnPermissionResult onPermissionResult = permissions.get(type);

        if(onPermissionResult != null) {
            onPermissionResult.onDeny();
            remove(type);
        }
    }


    public void onResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {



        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            switch (requestCode) {

                case STAOREG :
                    notifyResultAccepted(requestCode);
                    break;


                case LOCATION :
                    notifyResultAccepted(requestCode);
                    break;


                case CAMERA :
                    notifyResultAccepted(requestCode);
                    break;
            }

        } else {
            notifyResultDeny(requestCode);
        }
    }


}