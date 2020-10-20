package Listeners;

import android.net.wifi.p2p.WifiP2pDevice;

import java.util.ArrayList;
import java.util.Collection;

import Models.WifiDevice;

public interface OnDeviceNearbyFound {


    void onFound(ArrayList<WifiDevice> devices);
    void notFound();
}
