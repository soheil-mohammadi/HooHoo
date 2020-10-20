package core;

import android.content.Context;
import android.content.Intent;

import Dialogs.WarningDialog;
import Listeners.OnCustomClick;
import Utils.PermissionManager;
import servers.monitor.fastest.hoohoonew.App;
import servers.monitor.fastest.hoohoonew.R;

public class LocationManager {

    private static final String TAG = "LocationManager";
    private static LocationManager instance ;

    public static LocationManager builder() {
        if(instance == null)
            instance = new LocationManager();

        return instance;
    }



    public boolean isEnabled() {

        boolean isLocationGranted = PermissionManager.builder().isGranted(PermissionManager.LOCATION);

        if(isLocationGranted) {

            final android.location.LocationManager manager = (android.location.LocationManager) App.context
                    .getSystemService(Context.LOCATION_SERVICE);

            if (manager!= null ) {

                if(!manager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {

                    WarningDialog.builder().show(App.getInstance().getResString(R.string.enable_location),
                            App.getInstance().getResString(R.string.enable_location_desc), R.raw.location, new OnCustomClick() {
                                @Override
                                public void onClicked(Object object) {
                                    Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    App.context.startActivity(intent);
                                }
                            }, null , false);

                    return false;

                }else {

                    return true;
                }


            }

            return false;

        } else  {
            return  false;
        }


    }
}
