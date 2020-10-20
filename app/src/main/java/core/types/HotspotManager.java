package core.types;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import Entities.UserEntity;
import Listeners.OnDeviceNearbyFound;
import Listeners.OnHotspotReady;
import Models.WifiDevice;
import androidx.annotation.Nullable;
import core.ConnectionManager;
import core.EventConnectionState;
import core.HotspotClients;
import core.HotspotDetails;
import core.Patterns;
import servers.monitor.fastest.hoohoonew.App;
import service.HotspotShutService;
import service.ServerService;

public class HotspotManager {


    private WifiConfiguration defaultConfig ;

    private static boolean isRecieverStarted = false;
    private static boolean isFinderStarted = false;


    public static boolean isServer = true;
    private OnDeviceNearbyFound onDeviceNearbyFound ;

    private Handler handler ;


    private BluetoothAdapter btAdapter;
    private BTBroadCast btBroadCast ;
    private boolean isNotFoundBT = false ;
    private boolean isAutoDiscoverBlt = false;
    private Timer timerTryToFindKey ;

    private Timer connectionTimeout ;
    private WifiManager.LocalOnlyHotspotReservation hotspotAndroidO;

    private WifiScanReceiver wifiScanReceiver ;
    private WifiConnectionReceiver wifiConnectionReceiver ;


    private HashMap<String , String> btDevicies = new HashMap<>();

    public static  boolean serverServiceStarted = false;

    private Timer findingCycle;

    private Timer discoverTimeout ;
    private Boolean foundDevicies  = false;

    private static final String TAG = "HotspotManager";

    private WifiConfiguration wifiCon;
    private WifiManager mWifiManager;

    private static HotspotManager instance ;


    private String currentConnectingDevice = null;


    public static HotspotManager builder() {
        if(instance == null)
            instance =  new HotspotManager();

        return instance;
    }

    public HotspotManager(){
        this.mWifiManager = (WifiManager) App.context.getSystemService(Context.WIFI_SERVICE);
        handler = new Handler(Looper.getMainLooper());
    }


    public void startBlt() {
        isAutoDiscoverBlt = true;
        discoverBluetoothDevices();
    }


    public void start(@Nullable OnDeviceNearbyFound onDeviceNearbyFound) {

        if (!ConnectionManager.isConnected()) {
            ConnectionManager.setConnectionType(ConnectionManager.ConnectionType.HOTSPOT);
            this.onDeviceNearbyFound = onDeviceNearbyFound;
            asFinder();
            Log.e(TAG, "As Finder");

        }

    }


    public void start(@Nullable OnHotspotReady onHotspotReady) {

        if(!ConnectionManager.isConnected()) {
            ConnectionManager.setConnectionType(ConnectionManager.ConnectionType.HOTSPOT);

            asReciever(onHotspotReady);
            Log.e(TAG, "As Reciever " );
        }


    }

    public void asReciever(OnHotspotReady onHotspotReady) {

         isRecieverStarted = true;
        setHotspot(new HotspotDetails(Patterns.builder().getDeviceName() ,
                Patterns.hotspotPassword) , onHotspotReady );

    }

    public void registerServerReciever() {
        if(!serverServiceStarted) {
            serverServiceStarted = true;
            ConnectionManager.builder().startServer();
        }
    }

    public void unRegisterServerReciever() {
        shutServerService();
    }


    public void asFinder() {
        isFinderStarted = true;
        setWifi();
    }

    private void setWifi() {

        if(mWifiManager != null && !mWifiManager.isWifiEnabled())
            mWifiManager.setWifiEnabled(true);

        try {
            if(wifiScanReceiver != null)
                App.context.unregisterReceiver(wifiScanReceiver);
        }catch (IllegalArgumentException e) {

        }

        wifiScanReceiver = new WifiScanReceiver();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        App.context.registerReceiver(wifiScanReceiver, intentFilter);


        try {
            if(wifiConnectionReceiver != null)
                App.context.unregisterReceiver(wifiConnectionReceiver);
        }catch (IllegalArgumentException e) {

        }


        wifiConnectionReceiver = new WifiConnectionReceiver();

        IntentFilter intentFilterConnection = new IntentFilter();
        intentFilterConnection.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
        App.context.registerReceiver(wifiConnectionReceiver, intentFilterConnection);

        isAutoDiscoverBlt = false;

        discoverBluetoothDevices();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
               // Log.e(TAG, "Start Finding ..." );
                startFindingCycle();
            }
        } , 3000);

    }


    private void scanRecievers() {
        if (!mWifiManager.startScan()) {
            scanFailure();
        }
    }



    public void getClientList(final boolean onlyReachables, final int reachableTimeout){
        Runnable runnable = new Runnable() {
            public void run() {

                BufferedReader br = null;
                final ArrayList<HotspotClients> result = new ArrayList<HotspotClients>();

                try {
                    br = new BufferedReader(new FileReader("/proc/net/arp"));
                    String line;
                    while ((line = br.readLine()) != null) {
                        String[] splitted = line.split(" +");

                        if ((splitted != null) && (splitted.length >= 4)) {
                            // Basic sanity check
                            String mac = splitted[3];

                            if (mac.matches("..:..:..:..:..:..")) {
                                boolean isReachable = InetAddress.getByName(splitted[0]).isReachable(reachableTimeout);

                                if (!onlyReachables || isReachable) {
                                    result.add(new HotspotClients(splitted[0], splitted[3], splitted[5], isReachable));
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                } finally {
                    try {
                        br.close();
                    } catch (IOException e) {
                        Log.e(TAG, e.getMessage());
                    }
                }


                try {
                    Thread.sleep(3500);
                    getClientList(onlyReachables , reachableTimeout);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }
        };

        Thread mythread = new Thread(runnable);
        mythread.start();
    }



    private void startFindingCycle() {

        if(findingCycle == null) {
            findingCycle = new Timer();
        }else  {
            findingCycle.cancel();
            findingCycle = null;
            startFindingCycle();
        }

        startTimeOutDiscover();

        findingCycle.schedule(new TimerTask() {
            @Override
            public void run() {
                if(!ConnectionManager.isConnected()){
                    scanRecievers();
                }else {
                    findingCycle.cancel();
                    findingCycle = null;
                }
            }
        } , 1500, Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ? 15000 : 9000);
    }



    private void changeBltName(String name) {

        if (btAdapter == null)
            btAdapter = BluetoothAdapter.getDefaultAdapter();

        final long lTimeToGiveUp_ms = System.currentTimeMillis() + 10000;
        if (btAdapter != null)
        {
            final Handler myTimerHandler = new Handler();
            myTimerHandler.postDelayed(
                    new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            if (btAdapter.isEnabled())
                            {
                                btAdapter.setName(name);
                                if (name.equalsIgnoreCase(btAdapter.getName()))
                                {
                                    Log.e(TAG, "Updated BT Name to " + btAdapter.getName());
                                }
                            }
                            if ((!name.equalsIgnoreCase(btAdapter.getName())) && (System.currentTimeMillis() < lTimeToGiveUp_ms))
                            {
                                myTimerHandler.postDelayed(this, 100);
                                if (btAdapter.isEnabled())
                                    Log.e(TAG, "Update BT Name: waiting on BT Enable");
                                else
                                    Log.e(TAG, "Update BT Name: waiting for Name (" + name + ") to set in");
                            }
                        }
                    } , 100);
        }

    }


    private void discoverBluetoothDevices() {
        if (btAdapter == null)
            btAdapter = BluetoothAdapter.getDefaultAdapter();

        if(!btAdapter.isEnabled())
            btAdapter.enable();

        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        btBroadCast = new BTBroadCast();
        App.context.registerReceiver(btBroadCast, filter);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                btAdapter.startDiscovery();
            }
        } , 1300);

    }


    private void discoverableBltDevice(String name) {
        if (btAdapter == null)
            btAdapter = BluetoothAdapter.getDefaultAdapter();

        btAdapter.enable();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    Method method = BluetoothAdapter.class.getMethod("setScanMode", Integer.TYPE);
                    method.invoke(btAdapter  , BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE);
                    Log.e(TAG, "Saaammm !" );

                } catch (Throwable th) {
                    th.printStackTrace();
                }
            }
        } , 1300);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                changeBltName(name);
            }
        } , 1800);


    }


    private void shutBT() {
        if(btBroadCast != null)
            try {
                App.context.unregisterReceiver(btBroadCast);
            }catch (IllegalArgumentException e) {

            }

        if (btAdapter != null) {
            btAdapter.disable();
        }



        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                isAutoDiscoverBlt = true;
                discoverBluetoothDevices();
            }
        } , 600);

    }


    private class BTBroadCast extends BroadcastReceiver {

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                Log.e(TAG, "Started !!!  " + isNotFoundBT + " ... " + isAutoDiscoverBlt);
//                if(!isNotFoundBT && !isAutoDiscoverBlt)
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            Log.e(TAG, "Start Finding ..." );
//                            startFindingCycle();
//                        }
//                    } , 4000);
                //discovery starts, we can show progress dialog or perform other tasks
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                //discovery finishes, dismis progress dialog
              //  Log.e(TAG, "Finished!" );

            } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                //bluetooth device found
                BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                if(device.getName() != null && device.getName().contains("#")) {
                    String[] btName =  device.getName().split("#");
                    btDevicies.put(btName[0] , btName[1]);
                }

                Log.e(TAG, "bt Found : " + device.getName()  );

            }
        }
    }



    private void setHotspotAndroidOAbove(OnHotspotReady onHotspotReady) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mWifiManager.startLocalOnlyHotspot(new WifiManager.LocalOnlyHotspotCallback() {

                @Override
                public void onStarted(WifiManager.LocalOnlyHotspotReservation reservation) {
                    super.onStarted(reservation);
                    Log.e(TAG, "Wifi Hotspot is on now  : " + reservation.getWifiConfiguration().preSharedKey);
                    hotspotAndroidO = reservation;
                    String SSID  = hotspotAndroidO.getWifiConfiguration().SSID;
                    String PASSWORD = hotspotAndroidO.getWifiConfiguration().preSharedKey;

                    discoverableBltDevice(SSID + "#" + PASSWORD);
                    onHotspotReady.onStarted(SSID , PASSWORD);

                }

                @Override
                public void onStopped() {
                    super.onStopped();
                    Log.e(TAG, "onStopped: ");
                }

                @Override
                public void onFailed(int reason) {
                    super.onFailed(reason);
                    onHotspotReady.onFailed();
                }
            }, new Handler());
        }

    }

    private void setHotspot(HotspotDetails hotspotDetails , OnHotspotReady onHotspotReady) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT <= Build.VERSION_CODES.N) {


            boolean apstatus = false;

            saveDefaultConfig();

            wifiCon = new WifiConfiguration();
            wifiCon.SSID = hotspotDetails.getSsid();
            wifiCon.preSharedKey = hotspotDetails.getPassword();
            wifiCon.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
            wifiCon.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
            wifiCon.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            wifiCon.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            try {
                Method setWifiApMethod = mWifiManager.getClass().getMethod("setWifiApEnabled",
                        WifiConfiguration.class, boolean.class);
                apstatus = (Boolean) setWifiApMethod.invoke(mWifiManager, wifiCon, true);
            } catch (Exception e) {
                Log.e(TAG , "", e);
                onHotspotReady.onFailed();
            }

            isRecieverStarted = apstatus;
            onHotspotReady.onStarted(Patterns.builder().manageDeviceInfo(hotspotDetails.getSsid()).getUserName() ,
                    Patterns.hotspotPassword);


        } else {
            setHotspotAndroidOAbove(onHotspotReady);
        }


    }

    private void saveDefaultConfig() {
        Method[] methods = mWifiManager.getClass().getDeclaredMethods();
        for (Method m: methods) {
            if (m.getName().equals("getWifiApConfiguration")) {
                try {
                    defaultConfig = (WifiConfiguration)m.invoke(mWifiManager);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }

                break;
            }
        }
    }


    private class WifiScanReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context c, Intent intent) {
            boolean success = intent.getBooleanExtra(
                    WifiManager.EXTRA_RESULTS_UPDATED, false);
            if (success) {
                scanSuccess();
            } else {
                scanFailure();
            }
        }
    }


    private class WifiConnectionReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (WifiManager.SUPPLICANT_STATE_CHANGED_ACTION .equals(action)) {
                SupplicantState state = intent.getParcelableExtra(WifiManager.EXTRA_NEW_STATE);
                if (SupplicantState.isValidState(state)
                        && state == SupplicantState.COMPLETED) {

                    boolean connected = checkConnectedToDesiredWifi();

                    if(connected)
                        ConnectionManager.builder().startClient();

                    Log.e(TAG, "WifiConnectionReceiver: " + connected );
                }
            }
        }


        private boolean checkConnectedToDesiredWifi() {
            boolean connected = false;

            WifiInfo wifi = mWifiManager.getConnectionInfo();
            if (wifi != null) {
                String ssid = wifi.getSSID();
                connected = ssid.equals("\"" + currentConnectingDevice + "\"");
            }

            return connected;
        }
    }


    private void shutServerService() {
        Intent intent = new Intent();
        intent.setAction(ServerService.SHUT);
        App.context.sendBroadcast(intent);
    }



    public void connectWithBarcode(String barcodeValue) {

        String[] barcodeDetails = barcodeValue.split("#");
        String deviceName = barcodeDetails[0];
        String password = barcodeDetails[1];

        startConnection(deviceName , password);

    }

    public void connect(String deviceName) {

        String password ;

        Log.e(TAG, "connect With Name : " + deviceName );

        if(deviceName.startsWith("AndroidShare")) {

            if(btDevicies.containsKey(deviceName)) {
                password = btDevicies.get(deviceName);
                Log.e(TAG, "connect: " + password );

            } else  {

                Log.e(TAG, "connect Get Password Failed !!");

                if(!isNotFoundBT) {

                    btDevicies.clear();
                    isNotFoundBT = true;
                    isAutoDiscoverBlt = true;
                    discoverBluetoothDevices();
                    EventConnectionState.builder().notifyMembers(ConnectionManager.State.NOT_FOUND_KEY);

                    timerTryToFindKey = new Timer();
                    timerTryToFindKey.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Log.e(TAG, "Connect twice for blt ...." );
                                    connect(deviceName);
                                }
                            });
                        }
                    } , 8000);


                } else {
                    isNotFoundBT = false;
                    isAutoDiscoverBlt = false;
                    EventConnectionState.builder().notifyMembers(ConnectionManager.State.BARCODE_SCAN);
                }

                return;
            }


        } else  {
            password = Patterns.hotspotPassword;
        }


        startConnection(deviceName , password);

    }


    private void startConnection(String deviceName , String password) {
        shutServerService();
        EventConnectionState.builder().notifyMembers(ConnectionManager.State.CONNECTING);
        startTimeOutConnection();

        btDevicies.clear();

        try {

            WifiConfiguration conf = new WifiConfiguration();

            conf.SSID = "\"" + deviceName + "\"";
            conf.preSharedKey = "\"" + password + "\"";

            conf.status = WifiConfiguration.Status.ENABLED;
            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);

            mWifiManager.addNetwork(conf);

            List<WifiConfiguration> list = mWifiManager.getConfiguredNetworks();
            for( WifiConfiguration i : list ) {
                if(i.SSID != null && i.SSID.equals("\"" + deviceName + "\"")) {
                    mWifiManager.disconnect();
                    mWifiManager.enableNetwork(i.networkId, true);
                    mWifiManager.reconnect();

                    break;
                }
            }

            currentConnectingDevice = deviceName;
            //return true;
        } catch (Exception ex) {
            // return false;
        }
    }



    private void startTimeOutDiscover() {

        if(discoverTimeout == null) {
            discoverTimeout = new Timer();
        }else  {
            discoverTimeout.cancel();
            discoverTimeout = null;
            startTimeOutDiscover();
        }

        discoverTimeout.schedule(new TimerTask() {
            @Override
            public void run() {
                //Log.e(TAG, "Discover : " + foundDevicies + " ... " + onDeviceNearbyFound );
                if(!foundDevicies)
                    if(onDeviceNearbyFound != null)
                        onDeviceNearbyFound.notFound();
            }
        } , 25000);
    }



    private void startTimeOutConnection() {

        if(connectionTimeout == null) {
            connectionTimeout = new Timer();
        }else  {
            connectionTimeout.cancel();
            connectionTimeout = null;
            startTimeOutConnection();
        }

        connectionTimeout.schedule(new TimerTask() {
            @Override
            public void run() {

                if(!ConnectionManager.isConnected()) {
                    EventConnectionState.builder().notifyMembers(ConnectionManager.State.FAILED);
                    shutDownConnection();
                }
            }
        } , 16000);
    }



    public void cancelTimeOutConnection() {
        if(connectionTimeout != null)  {
            connectionTimeout.cancel();
            connectionTimeout = null;
        }
    }


    private void scanSuccess() {
        List<ScanResult> results = mWifiManager.getScanResults();
        ArrayList<WifiDevice> devices = new ArrayList<>();

        for (ScanResult device : results) {

            if(device.SSID.trim().equals("")) {
                continue;
            }

            UserEntity user =  Patterns.builder().manageDeviceInfo(device.SSID);
            if(user != null) {
                devices.add(new WifiDevice(device.SSID ,  user.getUserName() , user.getUserSex() , user.getUniqueID()));
            }else if(device.SSID.contains("AndroidShare")) {
                devices.add(new WifiDevice(device.SSID , device.SSID  ,
                        App.getInstance().getRandom(0 ,1) , ""));
            }
        }


        if(devices.size() > 0) {
            foundDevicies = true;
            onDeviceNearbyFound.onFound(devices);
        }
    }

    private void scanFailure() {
        List<ScanResult> results = mWifiManager.getScanResults();
        ArrayList<WifiDevice> devices = new ArrayList<>();
        for (ScanResult device : results) {

            if(device.SSID.trim().equals("")) {
                continue;
            }

            UserEntity user =  Patterns.builder().manageDeviceInfo(device.SSID);
            if(user != null) {
                devices.add(new WifiDevice(device.SSID ,  user.getUserName() , user.getUserSex() , user.getUniqueID()));
            }else if(device.SSID.contains("AndroidShare")) {
                devices.add(new WifiDevice(device.SSID , device.SSID  ,
                        App.getInstance().getRandom(0 ,1) , ""));
            }
        }

        if(devices.size() > 0) {
            foundDevicies = true;
            onDeviceNearbyFound.onFound(devices);
        }
    }



    private void forgetNetwork(String deviceName) {
        if(deviceName != null) {
            List<WifiConfiguration> list = mWifiManager.getConfiguredNetworks();
            if(list != null)
                for( WifiConfiguration i : list ) {
                    if(i.SSID != null && i.SSID.equals("\"" + deviceName + "\"")) {
                        Log.e(TAG, "forgetNetwork: !!!" );
                        mWifiManager.removeNetwork(i.networkId);
                        mWifiManager.saveConfiguration();
                        break;
                    }
                }
        }
    }


    private void restoreConfigs() {
        isRecieverStarted = false;
        isFinderStarted = false;
        foundDevicies  = false;
        isNotFoundBT = false;
        forgetNetwork(currentConnectingDevice);
    }


    public void clear() {

        if(timerTryToFindKey != null)  {
            timerTryToFindKey.cancel();
            timerTryToFindKey = null;
        }

        restoreConfigs();
        shutBT();
        turnOffHotspot();
    }

    private void turnOffHotspot() {

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                    && Build.VERSION.SDK_INT <= Build.VERSION_CODES.N) {
                WifiManager mWifiManager = (WifiManager) App.context.getSystemService(Context.WIFI_SERVICE);
                Method setWifiApMethod = mWifiManager.getClass().getMethod("setWifiApEnabled",
                        WifiConfiguration.class, boolean.class);
                setWifiApMethod.invoke(mWifiManager, wifiCon, false);
            }else {

                if (hotspotAndroidO != null) {
                    hotspotAndroidO.close();
                    hotspotAndroidO = null;
                }
            }

        }catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }


    public void shutDownConnection() {
        restoreConfigs();
        ConnectionManager.builder().setConnected(false);
        EventConnectionState.builder().notifyMembers(ConnectionManager.State.DISCONNECTED);
        turnOffHotspot();
    }

    public void destroy() {
        restoreConfigs();

        if(mWifiManager != null)
            mWifiManager.setWifiEnabled(false);

        try {
            if(wifiScanReceiver != null)
                App.context.unregisterReceiver(wifiScanReceiver);
        }catch (IllegalArgumentException e) {

        }

        try {
            if(wifiConnectionReceiver != null)
                App.context.unregisterReceiver(wifiConnectionReceiver);
        }catch (IllegalArgumentException e) {

        }

        Intent intent =  new Intent(App.context , HotspotShutService.class) ;
        intent.putExtra("defConfig" , defaultConfig);
        App.context.startService(intent);
    }


}