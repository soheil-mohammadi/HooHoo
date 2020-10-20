package Listeners;

import core.models.FileModel;

public interface OnConnectionState {

    void onConnected();
    void onConnecting();
    void onDisconnected();
    void onFailed(int reason);
    void notFoundKey();
    void onBarcodeScan();
}
