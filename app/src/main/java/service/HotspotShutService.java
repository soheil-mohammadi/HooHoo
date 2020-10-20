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

public class HotspotShutService extends IntentService {


	private static final String TAG = "HotspotShutService";


	private WifiConfiguration wifiCon;


	public HotspotShutService() {
		super("HotspotShutService");
	}


	@Override
	protected void onHandleIntent(Intent intent) {
		ConnectionManager.builder().setConnected(false);
		EventConnectionState.builder().notifyMembers(ConnectionManager.State.DISCONNECTED);
		wifiCon = intent.getParcelableExtra("defConfig");
		if(wifiCon != null)
			restoreConfig();
	}



	private void restoreConfig() {

		App.getInstance().initThread(new OnThread() {
			@Override
			public void onReady() {
				try {
					Thread.sleep(1500);
					WifiManager mWifiManager = (WifiManager) App.context.getSystemService(Context.WIFI_SERVICE);
					Method setWifiApMethod = mWifiManager.getClass().getMethod("setWifiApEnabled",
							WifiConfiguration.class, boolean.class);
					setWifiApMethod.invoke(mWifiManager, wifiCon,true);
					Thread.sleep(3500);
					setWifiApMethod.invoke(mWifiManager, wifiCon, wifiCon.status == 13);
				}  catch (NoSuchMethodException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});

	}


	public void onDestroy() {
		stopSelf();
	}

}