package core.types;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import Entities.UserEntity;
import Listeners.OnDeviceNearbyFound;
import Models.WifiDevice;
import androidx.annotation.Nullable;
import core.ConnectionManager;
import core.EventConnectionState;
import core.Patterns;
import servers.monitor.fastest.hoohoonew.App;
import service.P2PShutService;

import static android.os.Looper.getMainLooper;

public class P2PManager {


    private static final String TAG = "P2PManager";

    private static P2PManager instance;

    private WifiManager wifi;
    private WifiP2pManager wifiP2pManager;
    private WifiP2pManager.Channel wifichannel;


    private Timer discoverTimeout ;
    private Timer connectionTimeout ;
    private Timer discoverCycle ;

    private OnDeviceNearbyFound onDeviceNearbyFound ;
    private Boolean foundDevicies  = false;


    private BroadcastReceiver wifiServerReceiver;
    private IntentFilter wifiServerReceiverIntentFilter;


    public static P2PManager builder() {
        if(instance == null)
            instance = new P2PManager();

        return instance;
    }


    public P2PManager() {
        this.wifi = (WifiManager) App.context.getSystemService(Context.WIFI_SERVICE);
        this.wifiP2pManager = (WifiP2pManager) App.context.getSystemService(Context.WIFI_P2P_SERVICE);
        this.wifichannel = wifiP2pManager.initialize(App.context, getMainLooper(), null);
    }




    public void start(@Nullable OnDeviceNearbyFound onDeviceNearbyFound) {

        ConnectionManager.setConnectionType(ConnectionManager.ConnectionType.P2P);

        stopDiscoverCycle();

        if(wifi != null && !wifi.isWifiEnabled())
            wifi.setWifiEnabled(true);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {


                changeDeviceName(Patterns.builder().getDeviceName());


                discoverPeers(onDeviceNearbyFound);

                try {
                    if(wifiServerReceiver != null)
                    App.context.unregisterReceiver(wifiServerReceiver);
                }catch (IllegalArgumentException e) {

                }


                wifiServerReceiver = new WiFiServerBroadcastReceiver();

                wifiServerReceiverIntentFilter = new IntentFilter();
                wifiServerReceiverIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
                wifiServerReceiverIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
                wifiServerReceiverIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
                wifiServerReceiverIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

                App.context.registerReceiver(wifiServerReceiver, wifiServerReceiverIntentFilter);
                Log.e(TAG, "run: !!!!!" );
            }
        } , 1500);
    }

    private void changeDeviceName(String devName) {
        try {

            Class[] paramTypes = new Class[3];
            paramTypes[0] = WifiP2pManager.Channel.class;
            paramTypes[1] = String.class;
            paramTypes[2] = WifiP2pManager.ActionListener.class;
            Method setDeviceName = wifiP2pManager.getClass().getMethod(
                    "setDeviceName", paramTypes);
            setDeviceName.setAccessible(true);

            Object arglist[] = new Object[3];
            arglist[0] = wifichannel;
            arglist[1] = devName;
            arglist[2] = new WifiP2pManager.ActionListener() {

                @Override
                public void onSuccess() {
                    // Log.d("setDeviceName succeeded", "true");
                }

                @Override
                public void onFailure(int reason) {
                    //  Log.d("setDeviceName failed", "true");
                }
            };

            setDeviceName.invoke(wifiP2pManager, arglist);

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }


    public class WiFiServerBroadcastReceiver extends BroadcastReceiver {


        public WiFiServerBroadcastReceiver() {
            super();
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
                // Check to see if Wi-Fi is enabled and notify appropriate activity
                int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);

                if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                    //Log.e(TAG, "onReceive: !!" );
                    // activity.setServerWifiStatus("Wifi Direct is enabled");
                } else {

//                    if(ConnectionManager.isConnected()) {
//                        shutDownConnection();
//                    }
                    //  activity.setServerWifiStatus("Wifi Direct is not enabled");
                }

            } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {

                //Log.e(TAG, "onReceive: !!" );
                wifiP2pManager.requestPeers(wifichannel, new WifiP2pManager.PeerListListener() {
                    @Override
                    public void onPeersAvailable(WifiP2pDeviceList wifiP2pDeviceList) {

                      //  Log.e(TAG, "onPeersAvailable: " + wifiP2pDeviceList.getDeviceList().size() );
                        if(!ConnectionManager.isConnected()) {
                            if(onDeviceNearbyFound != null)
                                if(wifiP2pDeviceList.getDeviceList().size() > 0) {

                                    ArrayList<WifiDevice> targets = new ArrayList<>();

                                    for (WifiP2pDevice wifiP2pDevice : wifiP2pDeviceList.getDeviceList()) {

                                        UserEntity userEntity =  Patterns.builder().manageDeviceInfo(wifiP2pDevice.deviceName);

                                        if(userEntity != null) {
                                            targets.add(new WifiDevice(wifiP2pDevice.deviceAddress ,
                                                    userEntity.getUserName() , userEntity.getUserSex() ,
                                                    userEntity.getUniqueID()));
                                        }else {
                                            targets.add(new WifiDevice(wifiP2pDevice.deviceAddress ,
                                                    wifiP2pDevice.deviceName , 1 ,
                                                    String.valueOf( Patterns.builder().generateUniqueId())));
                                        }

                                    }

                                    if(targets.size() > 0 ) {
                                        foundDevicies = true;
                                        onDeviceNearbyFound.onFound(targets);
                                    }

                                }
                        }

                    }
                });

            } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {

                NetworkInfo networkState = intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);
                WifiP2pInfo wifiInfo = intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_INFO);

                if( networkState != null && networkState.isConnected()) {


                    if(wifiInfo != null) {

                        //Log.e(TAG, "onReceive: " + ConnectionManager.isConnected() );

                        if(!ConnectionManager.isConnected()) {

                            if(!wifiInfo.isGroupOwner)  {
                                ConnectionManager.builder().startClient(wifiInfo);
                            }else {
                                ConnectionManager.builder().startServer();
                            }

                        }


                    }else {

                        //EventConnectionState.builder().notifyMembers(ConnectionManager.State.FAILED);
                        shutDownConnection();
                    }


                    // activity.setServerStatus("Connection Status: Connected");
                } else {
                    //  activity.setServerStatus("Connection Status: Disconnected");

                    if(ConnectionManager.isConnected()) {
                        ConnectionManager.builder().setConnected(false);
                        shutDownConnection();
                    }


                }

            } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
                // Respond to this device's wifi state changing
            }
        }
    }




    public void connectToPeer(final WifiDevice wifiDevice) {

        EventConnectionState.builder().notifyMembers(ConnectionManager.State.CONNECTING );
        WifiP2pConfig config = new WifiP2pConfig();
        config.deviceAddress = wifiDevice.Address;
      //  config.wps.setup = WpsInfo.PBC;

        startTimeOutConnection();
        wifiP2pManager.connect(wifichannel, config, new WifiP2pManager.ActionListener()  {
            public void onSuccess() {
                // Log.e(TAG, "Connected !!" );
                //stopServer();
            }

            public void onFailure(int reason) {
                //TODO
            }
        });

    }


    private void discoverPeers(OnDeviceNearbyFound onDeviceNearbyFound) {

        this.onDeviceNearbyFound = onDeviceNearbyFound;

        foundDevicies = false;

        startTimeOutDiscover();

//        if(onDeviceNearbyFound == null)
//            startDiscoverCycle();
//        else {
//            discoverPeers();
//        }


        startDiscoverCycle();



    }


    private void discoverPeers() {

        if(wifi != null && !wifi.isWifiEnabled())
            wifi.setWifiEnabled(true);

        wifiP2pManager.discoverPeers(wifichannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                //TODO
            }

            @Override
            public void onFailure(int i) {

            }
        });
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
                if(!foundDevicies)
                    if(onDeviceNearbyFound != null)
                        onDeviceNearbyFound.notFound();
            }
        } , 18000);
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
        } , 15000);
    }



    public void cancelTimeOutConnection() {
        if(connectionTimeout != null)  {
            connectionTimeout.cancel();
            connectionTimeout = null;
        }
    }


    public void shutDownConnection() {


        wifiP2pManager.removeGroup(wifichannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.e(TAG, "onSuccess: 222" );
            }

            @Override
            public void onFailure(int i) {

            }
        });


        Intent intent =  new Intent(App.context , P2PShutService.class) ;
        App.context.startService(intent);

        Log.e(TAG, "shutDownConnection: !!!" );



    }

    private void stopDiscoverCycle() {
        if(discoverCycle != null) {
            discoverCycle.cancel();
            discoverCycle = null;
        }
    }

    private void startDiscoverCycle() {

        if(discoverCycle == null) {
            discoverCycle = new Timer();
        }else  {
            discoverCycle.cancel();
            discoverCycle = null;
            startDiscoverCycle();
        }

        discoverCycle.schedule(new TimerTask() {
            @Override
            public void run() {
                if(!ConnectionManager.isConnected()) {
                    discoverPeers();
                }else {
                    discoverCycle.cancel();
                    discoverCycle = null;
                }
            }
        } , 0 , 3500);
    }



    public void destroy() {
        shutDownConnection();
    }


}
