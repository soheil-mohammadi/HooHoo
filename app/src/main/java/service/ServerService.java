package service;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.service.quicksettings.Tile;
import android.util.Log;
import core.Patterns;
import core.RecieveMsg;
import core.SendMsg;
import core.ConnectionManager;
import core.types.HotspotManager;
import servers.monitor.fastest.hoohoonew.App;

public class ServerService extends IntentService {

	private static final String TAG = "ServerService";
	private  final int port = 8888;


	public final static String SHUT = "shut";


	private boolean isShut = false;

	private ServerSocket welcomeSocket ;
	private Socket socket ;

	private InputStream inputStream ;
	private OutputStream outputStream ;
	private byte buffer[] ;

	private Timer checkerClientConnection ;

	public ServerService() {
		super("ServerService");
	}


	@Override
	protected void onHandleIntent(Intent intent) {


		registerShutReciever();

		buffer = new byte[Patterns.BUF_SIZE];

		try {

			Log.e(TAG, "Waiting ....." );

			welcomeSocket = new ServerSocket();
			welcomeSocket.setReuseAddress(true);
			welcomeSocket.bind(new InetSocketAddress(port));
			socket = welcomeSocket.accept();



			HotspotManager.isServer = true;

			//Log.e(TAG, "Accepted Server ----> Client  :  " + socket.getInetAddress().getHostAddress());

			inputStream = socket.getInputStream();
			outputStream = socket.getOutputStream();

			SendMsg.builder(outputStream);

			ConnectionManager.builder().redirectToChat();
			checkClientConnection();
			readData();


		} catch (IOException e) {
			Log.e(TAG, "Ex 1:  " +e.getMessage() );
			destroy();
			return;
		}

		catch(Exception e)
		{
			Log.e(TAG, "Ex2 : " + e.getMessage() );
			destroy();
			return;
		}
	}

	private void registerShutReciever() {
	    isShut = false;
		ShutServiceBroadcast shutServiceBroadcast = new ShutServiceBroadcast();
		IntentFilter intentFilter = new IntentFilter(SHUT);
		App.context.registerReceiver(shutServiceBroadcast , intentFilter);
	}


	private class ShutServiceBroadcast extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			//Log.e(TAG, "onReceive Shut !!!" );
			isShut = true;
			destroy();
		}
	}


	private void handleReadingData(byte[] bytes , int readLength) {
		RecieveMsg.ReaderState readerState = RecieveMsg.builder().handleMsg(bytes, readLength);

		if(readerState == RecieveMsg.ReaderState.MSG_READING
				|| readerState == RecieveMsg.ReaderState.FILE_COMPLETED)
			buffer = new byte[Patterns.BUF_SIZE];
	}


	private void readData() {
		try {

			int read ;

			ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);


			while (ConnectionManager.isConnected()) {

				read =  objectInputStream.read(buffer , 0 , buffer.length);

				Log.e(TAG, "readData: "  + socket.isConnected() );
				String msg = new String(buffer , 0 , read);
				String[] content = null;


				if(msg.contains(Patterns.ENDER_ID)) {
					content = msg.split(Patterns.ENDER_ID);
					//Log.e(TAG, "run: " + content.length );
				}


				if(content != null)
					for (int i = 0; i <content.length ; i++) {
						//handleReadingData(content[i].getBytes() ,  read);
						handleReadingData(content[i].getBytes() ,  content[i].length());
					}

				else {
					handleReadingData(buffer , read);
				}

			}


		}catch (IOException e ) {

			destroy();
			return;
		}
	}



	private void checkClientConnection() {
		checkerClientConnection = new Timer();
		checkerClientConnection.schedule(new TimerTask() {
			@Override
			public void run() {
				if(socket != null) {
					try {
						if(!socket.getInetAddress().isReachable(4000))
							destroy();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}else {
					destroy();
				}
			}
		} , 6000 , 3000);
	}


	public  void destroy() {

		HotspotManager.serverServiceStarted = false;

		try {

			if(welcomeSocket != null) {
				welcomeSocket.close();
			}

			if(socket != null) {
				socket.close();
			}

			if(checkerClientConnection != null) {
				checkerClientConnection.cancel();
				checkerClientConnection = null;
			}

			if(outputStream != null) {
				Log.e(TAG, "Closed  Out put !" );
				outputStream.flush();
				outputStream.close();
			}

			if(inputStream != null) {
				Log.e(TAG, "Closed  In put !" );
				inputStream.close();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			stopSelf();

			if(!isShut)
				ConnectionManager.builder().disconnect(false);

			//Log.e(TAG, "Stop Server Service !" );
		}

	}


	public void onDestroy()
	{
		stopSelf();
	}

}
