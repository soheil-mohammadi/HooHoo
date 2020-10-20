package asyncTasks;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import core.ConnectionManager;
import core.Patterns;
import core.SendMsg;
import servers.monitor.fastest.hoohoonew.App;

public class Server extends AsyncTask<Void , Void , Void> {


    private final int port = 7960;


    public  ServerSocket welcomeSocket;
    public  Socket socket;
    public  OutputStream outputStream;
    public  InputStream inputStream;

    private static final String TAG = "Server";


    @Override
    protected Void doInBackground(Void... voids) {


        try {

            Log.e(TAG, "Waiting For Server !!" );

            ServerSocket welcomeSocket = new ServerSocket();
            welcomeSocket.setReuseAddress(true);
            welcomeSocket.bind(new InetSocketAddress(8888));
            Socket socket = welcomeSocket.accept();


            Log.e(TAG, "Accepted Server ----> Client  :  " + socket.getInetAddress().getHostAddress());


            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();

            //SendMsg.builder(outputStream);

            //ConnectionManager.builder().redirectToChat();

//			readData();

            //InputStream in = App.context.getContentResolver().openInputStream(Uri.parse("/sdcard/Download/"));
            InputStream in = new FileInputStream(new File("/sdcard/HooHooNew/Videos/93f28d90-99e9-439c-aaee-7d155d63d718.mp4"));
            //in = App.context.getContentResolver().openInputStream(Uri.parse("content://servers.monitor.fastest.hoohoonew.greentoad.turtlebody.mediaprovider/media/DCIM/Camera/20200324_001318.mp4"));


            int len;

            long time  = System.currentTimeMillis();


            byte buffer[] = new byte[Patterns.BUF_SIZE];

            int sendLength = 0 ;

            while ((len = in.read(buffer)) != -1) {

                outputStream.write(buffer , 0 , len);
               // outputStream.flush();
                sendLength += len;
                Log.e(TAG, "writeFile: " + (sendLength / (1024 * 1024) ) + " MB");
            }

            Log.e(TAG, "End Sending : "  +  (( Math.abs(System.currentTimeMillis() - time)) / 1000));
            in.close();


            try {

                welcomeSocket.close();

                socket.close();

                Log.e(TAG, "Closed  Out put !" );
                outputStream.flush();
                outputStream.close();


                Log.e(TAG, "Closed  In put !" );
                inputStream.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;

        } catch (IOException e) {
            Log.e(TAG, "Ex 1:  " +e.getMessage() );
            return null;
        }

        catch(Exception e)
        {
            Log.e(TAG, "Ex2 : " + e.getMessage() );
            return null ;
        }

    }


    private Void writeFile() {

        try {


            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());


            InputStream in ;

            in = App.context.getContentResolver().openInputStream(Uri.parse("content://servers.monitor.fastest.hoohoonew.greentoad.turtlebody.mediaprovider/media/DCIM/Camera/VID_20191209_220024.mp4"));
            //in = App.context.getContentResolver().openInputStream(Uri.parse("content://servers.monitor.fastest.hoohoonew.greentoad.turtlebody.mediaprovider/media/DCIM/Camera/20200324_001318.mp4"));


            byte[] buf = new byte[8192];
            int len;

            long time  = System.currentTimeMillis();


            int sendLength = 0 ;
            //	GZIPOutputStream gzipOutputStream = new GZIPOutputStream(os , Patterns.BUF_SIZE , true);
//			BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream ,
//					Patterns.BUF_SIZE);




            while ((len = in.read(buf)) != -1) {


                objectOutputStream.write(buf , 0 , len);
                objectOutputStream.flush();

                //   bufferedOutputStream.flush();
                //  sendBytes(buf , len);
                sendLength += len;
                Log.e(TAG, "writeFile: " + (sendLength / (1024 * 1024) ) + " MB");
                // Log.e(TAG, "writeFile: ");
                //managePrgFile(file);


                //Log.e(TAG, "sending File !!" );

            }

            Log.e(TAG, "End Sending : "  +  (( Math.abs(System.currentTimeMillis() - time)) / 1000));
            objectOutputStream.close();
            in.close();


            destroy();
            return null;
            //Thread.sleep(500);

        } catch (FileNotFoundException e) {
            Log.e(TAG, "Exception File Not Found : " + e.getMessage() );
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            destroy();
            Log.e(TAG, "Ex Server : " + e.getMessage() );
            e.printStackTrace();
            return null;
        }

    }



    public  void destroy() {

        cancel(true);
        ConnectionManager.builder().setConnected(false);

        try {

            if(welcomeSocket != null) {
                welcomeSocket.close();
            }

            if(socket != null) {
                socket.close();
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
        }

    }


}
