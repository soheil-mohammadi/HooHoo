package core;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pInfo;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;

import Fragments.ChatFrg;
import Fragments.MainFrg;
import Listeners.OnAlertActionsClicked;
import Listeners.OnConnectionState;
import Listeners.OnDeviceNearbyFound;
import Listeners.OnHotspotReady;
import Models.WifiDevice;
import Utils.PermissionManager;
import androidx.annotation.Nullable;
import core.types.HotspotManager;
import core.types.P2PManager;
import servers.monitor.fastest.hoohoonew.App;
import servers.monitor.fastest.hoohoonew.R;
import service.ClientService;
import service.ServerService;

public class ConnectionManager {


    private static final String TAG = "ConnectionManager";

    private static boolean isConnected = false;

    private Handler handler;

    private  static ConnectionType connectionType = ConnectionType.HOTSPOT;

    private static ConnectionManager instance ;


    public static  boolean isClient = true;

    private  Intent serverServiceIntent ;
    private  Intent clientServiceIntent ;

    private WifiManager wifiManager;



    public static ConnectionManager builder() {
        if(instance == null)
            instance = new ConnectionManager();

        return instance;
    }


    public ConnectionManager() {
        handler = new Handler(Looper.getMainLooper());
        wifiManager = (WifiManager) App.context.getSystemService(Context.WIFI_SERVICE);
    }


    @SuppressWarnings("deprecation")
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static boolean isAirplaneModeOn() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return Settings.System.getInt(App.context.getContentResolver(),
                    Settings.System.AIRPLANE_MODE_ON, 0) != 0;
        } else {
            return Settings.Global.getInt(App.context.getContentResolver(),
                    Settings.Global.AIRPLANE_MODE_ON, 0) != 0;
        }
    }



    public void destroyWifi() {
        if(wifiManager != null) {
            wifiManager.setWifiEnabled(false);
        }
    }


    public void startClient(WifiP2pInfo wifiInfo) {

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                clientServiceIntent = new Intent(App.context, ClientService.class);
                clientServiceIntent.putExtra("ip" , wifiInfo.groupOwnerAddress);
                App.context.startService(clientServiceIntent);

            }
        } , 2500);

    }


    public void startClient() {

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    clientServiceIntent = new Intent(App.context, ClientService.class);
                    clientServiceIntent.putExtra("ip" , InetAddress.getByName("192.168.43.1"));
                    App.context.startService(clientServiceIntent);
                    Log.e(TAG, "Client Started !!" );
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }

            }
        } , 2000);

    }


    public void stopClient() {

        if(clientServiceIntent != null)
        {
            App.context.stopService(clientServiceIntent);
        }

    }


    public void startServer() {

        //new Server().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        serverServiceIntent = new Intent(App.context, ServerService.class);
        App.context.startService(serverServiceIntent);
    }


    public void stopServer() {

        if(serverServiceIntent != null)
        {
            App.context.stopService(serverServiceIntent);
        }

    }



    public void disconnect(boolean isFromUser) {
        if(isFromUser) {
            App.getInstance().showAlertDialogBuilder(App.getInstance().getResString(R.string.leave_chat)
                    , App.getInstance().getResString(R.string.you_want_leave_chat), new OnAlertActionsClicked() {
                        @Override
                        public void onNegativeClicked(DialogInterface dialog) {
                            dialog.dismiss();
                        }

                        @Override
                        public void onPositiveClicked(DialogInterface dialog) {
                            shutDownConnection();
                            dialog.dismiss();
                            App.getInstance().removeAllFrgs();
                            App.getInstance().showFrg(R.id.frame_activity_main , true , MainFrg.newInstance());
                        }
                    });
        }else {
            shutDownConnection();
        }


    }

    private void shutDownConnection() {

        ConnectionManager.isClient = true;

        switch (getConnectionType()) {

            case P2P:
                P2PManager.builder().shutDownConnection();
                break;


            case HOTSPOT:
                HotspotManager.builder().shutDownConnection();
                break;
        }
    }

    public void connect(WifiDevice targetDevice) {
        switch (getConnectionType()) {

            case HOTSPOT:
                HotspotManager.builder().connect(targetDevice.Address);
                break;


            case P2P:
                P2PManager.builder().connectToPeer(targetDevice);
                break;
        }
    }


    public enum ConnectionType {

        P2P ,
        HOTSPOT

    }


    public static  void setConnectionType(ConnectionType type) {
        connectionType = type;
    }

    public static ConnectionType getConnectionType() {
        return connectionType;
    }

    public void start(@Nullable OnDeviceNearbyFound onDeviceNearbyFound) {
//
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//
//            if(onDeviceNearbyFound == null) {
//                P2PManager.builder().start(null);
//            }else {
//
//                PermissionManager.builder().grant(PermissionManager.LOCATION, new PermissionManager.OnPermissionResult() {
//                    @Override
//                    public void onAccepted() {
//                        if(LocationManager.builder().isEnabled())
//                            P2PManager.builder().start(onDeviceNearbyFound);
//                    }
//
//                    @Override
//                    public void onDeny() {
//
//                    }
//                });
//
//            }
//
//        }else {
//            P2PManager.builder().start(onDeviceNearbyFound);
//        }


//
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//
//            if(onDeviceNearbyFound == null) {
//                HotspotManager.builder().start(null);
//
//            }else {
//
//                PermissionManager.builder().grant(PermissionManager.LOCATION, new PermissionManager.OnPermissionResult() {
//                    @Override
//                    public void onAccepted() {
//                        if(LocationManager.builder().isEnabled())
//                            HotspotManager.builder().start(onDeviceNearbyFound);
////                            if(!isAirplaneModeOn())
////                                HotspotManager.builder().start(onDeviceNearbyFound);
////                            else
////                                P2PManager.builder().start(onDeviceNearbyFound);
//                    }
//
//                    @Override
//                    public void onDeny() {
//
//                    }
//                });
//
//            }
//
//        }else {
//
//            HotspotManager.builder().start(onDeviceNearbyFound);
////
////            if(!isAirplaneModeOn())
////                HotspotManager.builder().start(onDeviceNearbyFound);
////            else
////                P2PManager.builder().start(onDeviceNearbyFound);
//        }


    }


    public void setConnected(boolean isConnected) {
        this.isConnected = isConnected;
    }

    public void redirectToChat() {

        if(!isConnected()) {
            setConnected(true);
            App.getInstance().removeAllFrgs();

            HotspotManager.builder().cancelTimeOutConnection();
            //P2PManager.builder().cancelTimeOutConnection();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    App.getInstance().showFrg(R.id.frame_activity_main , true , ChatFrg.newInstance());
                }
            } , 500);
        }

    }


    public enum State {
        CONNECTED ,
        CONNECTING ,
        DISCONNECTED,
        FAILED ,
        NOT_FOUND_KEY ,
        BARCODE_SCAN
    }


    public void setNotify(OnConnectionState onConnectionState) {
        EventConnectionState.builder().register(onConnectionState);
    }


    public void removeNotify(OnConnectionState onConnectionState) {
        EventConnectionState.builder().unregister(onConnectionState);
    }


    public void destroy() {

        switch (getConnectionType()) {

            case P2P:
                P2PManager.builder().destroy();
                break;


            case HOTSPOT:
                HotspotManager.builder().destroy();
                break;
        }
    }


    public static boolean isConnected() {
        return isConnected;
    }
}
