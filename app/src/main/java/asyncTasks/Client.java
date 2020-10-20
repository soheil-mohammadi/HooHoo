package asyncTasks;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

import core.ConnectionManager;
import core.Patterns;

public class Client extends AsyncTask<Void , Void , Void> {


    private InetAddress inetAddress;

    private  int port = 7960;

    private  InputStream inputStream ;
    private  OutputStream outputStream ;

    private   Socket clientSocket ;
    private byte[] buffer ;

    private static final String TAG = "Client";

    public Client(InetAddress inetAddress) {
        this.inetAddress = inetAddress;
    }

    @Override
    protected Void doInBackground(Void... voids) {


        try {

            buffer = new byte[Patterns.BUF_SIZE];

            Log.e(TAG, "Start Client!!" );
            clientSocket = new Socket(inetAddress , port);

            inputStream  = clientSocket.getInputStream();
            outputStream = clientSocket.getOutputStream();
//
//            SendMsg.builder(os);

            ConnectionManager.builder().setConnected(true);
        //    ConnectionManager.builder().redirectToChat();

            int read ;

            //      DataInputStream in = new DataInputStream(is);
//
            //BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream , Patterns.BUF_SIZE);
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

            //  GZIPInputStream gzipInputStream = new GZIPInputStream(is , Patterns.BUF_SIZE);

            while (ConnectionManager.isConnected()) {

                read =  objectInputStream.read(buffer , 0 , buffer.length);
                Log.e(TAG, "doInBackground:  " + read );

                // read = in.read(buffer , 0 , Patterns.BUF_SIZE);

            }

            destroy();
            return null;

        } catch (IOException e) {
            destroy();
            Log.e(TAG, "Ex : " + e.getMessage() );
            return null;
        }
        catch(Exception e)
        {
            Log.e(TAG, "Ex2 : " + e.getMessage() );
            return null;
        }

    }




    public void destroy() {

        ConnectionManager.builder().setConnected(false);


        try {

            if(clientSocket != null) {
                Log.e(TAG, "closed Client Socket !" );
                clientSocket.close();
                clientSocket = null;
            }


            if(inputStream != null) {
                inputStream.close();
                inputStream = null ;
            }


            if(outputStream != null) {
                outputStream.flush();
                outputStream.close();
                outputStream = null;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
