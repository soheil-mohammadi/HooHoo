package core;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import android.content.Context;
import android.util.Base64InputStream;
import android.util.Base64OutputStream;
import android.util.Log;

import servers.monitor.fastest.hoohoonew.App;

public class SerializeObject {

    private final static String TAG = "SerializeObject";

    private static  SerializeObject instance ;

    private final int intObj = 20020;

    public static SerializeObject builder() {
        if(instance == null)
            instance = new SerializeObject();

        return instance;
    }


    public  String objectToString(Serializable object) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            new ObjectOutputStream(out).writeObject(object);
            byte[] data = out.toByteArray();
            out.close();

            out = new ByteArrayOutputStream();
            Base64OutputStream b64 = new Base64OutputStream(out,0);
            b64.write(data);
            b64.close();
            out.close();

            return new String(out.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public  void saveObject(Object object , String fileName) {
        ObjectOutputStream os = null;
        try {
            os = new ObjectOutputStream(new FileOutputStream(new File(App.WORKPATH + "/" + fileName)));
            os.writeInt(intObj);
            os.writeObject(object);
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public  Object readObject(String fileName) {
        ObjectInputStream os = null;
        try {
            os = new ObjectInputStream(new FileInputStream(new File(App.WORKPATH + "/"  + fileName)));
            os.readInt();
            Object objFinal = os.readObject();
            os.close();
            return objFinal;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;

    }

    public  Object readObject(InputStream inputStream) {
        ObjectInputStream os = null;
        try {
            os = new ObjectInputStream(inputStream);
            os.readInt();
            Object objFinal = os.readObject();
            os.close();
            return objFinal;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;

    }

    public  Object stringToObject(String encodedObject) {
        try {
            return new ObjectInputStream(new Base64InputStream(
                    new ByteArrayInputStream(encodedObject.getBytes()), 0)).readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void WriteSettings(Context context, String data, String filename){
        FileOutputStream fOut = null;
        OutputStreamWriter osw = null;

        try{
            fOut = context.openFileOutput(filename, Context.MODE_PRIVATE);
            osw = new OutputStreamWriter(fOut);
            osw.write(data);
            osw.flush();
            //Toast.makeText(context, "Settings saved",Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            // Toast.makeText(context, "Settings not saved",Toast.LENGTH_SHORT).show();
        }
        finally {
            try {
                if(osw!=null)
                    osw.close();
                if (fOut != null)
                    fOut.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static String ReadSettings(Context context, String filename){
        StringBuffer dataBuffer = new StringBuffer();
        try{
            // open the file for reading
            InputStream instream = context.openFileInput(filename);
            // if file the available for reading
            if (instream != null) {
                // prepare the file for reading
                InputStreamReader inputreader = new InputStreamReader(instream);
                BufferedReader buffreader = new BufferedReader(inputreader);

                String newLine;
                // read every line of the file into the line-variable, on line at the time
                while (( newLine = buffreader.readLine()) != null) {
                    // do something with the settings from the file
                    dataBuffer.append(newLine);
                }
                // close the file again
                instream.close();
            }

        } catch (java.io.FileNotFoundException f) {
            // do something if the myfilename.txt does not exits
            Log.e(TAG, "FileNot Found in ReadSettings filename = " + filename);
            try {
                context.openFileOutput(filename, Context.MODE_PRIVATE);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            Log.e(TAG, "IO Error in ReadSettings filename = " + filename);
        }

        return dataBuffer.toString();
    }

}