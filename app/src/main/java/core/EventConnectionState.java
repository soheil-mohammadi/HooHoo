package core;


import android.net.ConnectivityManager;

import java.util.HashSet;

import Listeners.OnConnectionState;

public class EventConnectionState {


    private static EventConnectionState instance ;
    private final HashSet<OnConnectionState> mListeners = new HashSet<OnConnectionState>();

    public static EventConnectionState builder() {
        if(instance == null)
            instance = new EventConnectionState();

        return instance;
    }


    public void register(OnConnectionState listener)
    {

        if(!mListeners.contains(listener))
            mListeners.add(listener);
    }


    public void unregister(OnConnectionState listener)
    {
        mListeners.remove(listener);
    }

    public boolean isEmptyListener() {
        return mListeners.isEmpty();
    }



    public void notifyMembers(ConnectionManager.State state  ) {
        for(OnConnectionState onConnectionState : mListeners) {

            switch (state) {

                case CONNECTED:
                    onConnectionState.onConnected();
                    break;


                case CONNECTING:
                    onConnectionState.onConnecting();
                    break;


                case DISCONNECTED:
                    onConnectionState.onDisconnected();
                    break;


                case FAILED:
                    onConnectionState.onFailed(0);
                    break;


                case NOT_FOUND_KEY:
                    onConnectionState.notFoundKey();
                    break;


                case BARCODE_SCAN:
                    onConnectionState.onBarcodeScan();
                    break;

            }
        }
    }

}
