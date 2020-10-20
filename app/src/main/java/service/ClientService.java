package service;

import android.app.IntentService;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;
import java.util.zip.GZIPInputStream;

import Listeners.OnThread;
import core.ConnectionManager;
import core.Patterns;
import core.RecieveMsg;
import core.SendMsg;
import core.types.HotspotManager;
import servers.monitor.fastest.hoohoonew.App;
import servers.monitor.fastest.hoohoonew.R;

public class ClientService extends IntentService {


    private static final String TAG = "ClientService";

    private final int port = 8888;

    private byte buffer[] ;

    private Socket socket ;

    private InputStream inputStream ;
    private OutputStream outputStream ;


    public ClientService() {
        super("ClientService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {

        try {

            buffer = new byte[Patterns.BUF_SIZE];
            InetAddress inetAddress  = (InetAddress) intent.getExtras().get("ip");

            socket = new Socket();
            socket.bind(null);
            socket.connect((new InetSocketAddress(inetAddress, port)), 15000);


            HotspotManager.isServer = false;

            inputStream  = socket.getInputStream();
            outputStream = socket.getOutputStream();

            SendMsg.builder(outputStream);

            ConnectionManager.builder().redirectToChat();

            readData();


        } catch (IOException e) {
            Log.e(TAG, "onHandleIntent: " + e.getMessage() );
            destroy();
            return;
        }

        catch(Exception e)
        {
            Log.e(TAG, "onHandleIntent11: " + e.getMessage() );
            destroy();
            return;
        }


    }


    private void readData() {

        try {

            int read ;

            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);


            while (ConnectionManager.isConnected()) {

                read =  objectInputStream.read(buffer , 0 , buffer.length);

                String msg = new String(buffer , 0 , read);

                String[] content = null;

                if(msg.contains(Patterns.ENDER_ID)) {
                    content = msg.split(Patterns.ENDER_ID);
                    //Log.e(TAG, "run: " + content.length );
                }


                if(content != null)
                    for (int i = 0; i <content.length ; i++) {
                       // handleReadingData(content[i].getBytes() , read);
                        handleReadingData(content[i].getBytes() , content[i].length());
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



    private void handleReadingData(byte[] bytes , int readLength) {

        RecieveMsg.ReaderState readerState = RecieveMsg.builder().handleMsg(bytes, readLength);

        if(readerState == RecieveMsg.ReaderState.MSG_READING
                || readerState == RecieveMsg.ReaderState.FILE_COMPLETED)
            buffer = new byte[Patterns.BUF_SIZE];
    }



    public void destroy() {

        try {

            if(socket != null) {
                socket.close();
            }


            if(inputStream != null) {
                inputStream.close();
            }


            if(outputStream != null) {
                outputStream.flush();
                outputStream.close();
            }

        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            stopSelf();
            ConnectionManager.builder().disconnect(false);
            Log.e(TAG, "Stop Client Service !" );
        }
    }



    public void onDestroy() {
        stopSelf();
    }

}
