package service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import Listeners.OnThread;
import core.ConnectionManager;
import core.EventConnectionState;
import servers.monitor.fastest.hoohoonew.App;

public class P2PShutService extends IntentService {

	private static final String TAG = "P2PShutService";


	public P2PShutService() {
		super("P2PShutService");
	}


	@Override
	protected void onHandleIntent(Intent intent) {
		shutDown();
	}

	private void shutDown() {

		WifiManager wifi = (WifiManager) App.context.getSystemService(Context.WIFI_SERVICE);

//		if(wifi != null)
//			wifi.setWifiEnabled(false);

		ConnectionManager.builder().setConnected(false);
		EventConnectionState.builder().notifyMembers(ConnectionManager.State.DISCONNECTED);
	}


	public void onDestroy() {
		stopSelf();
	}

}