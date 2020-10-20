package core;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

import Entities.UserEntity;
import Listeners.OnSendMsg;
import Listeners.OnThread;
import core.models.FileModel;
import core.types.HotspotManager;
import servers.monitor.fastest.hoohoonew.App;
import servers.monitor.fastest.hoohoonew.R;

public class SendMsg  {

    private static SendMsg instance ;

    private OutputStream outputStream;

    private long sendLength = 0;

    private ObjectOutputStream objectOutputStream ;


    private ArrayList<FileModel> files = new ArrayList<>();

    private static final String TAG = "SendMsg";

    public static SendMsg builder(OutputStream outputStream) {
        if(instance == null)
            instance = new SendMsg(outputStream);

        return instance;
    }


    public SendMsg(OutputStream outputStream) {

        if(outputStream != null) {
            try {
                this.outputStream = outputStream;
                this.objectOutputStream = new ObjectOutputStream(outputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    public static SendMsg builder() {
        if(instance == null)
            instance = new SendMsg(null);

        return instance;
    }


    public void setNotify(OnSendMsg onSendMsg) {
        EventSendMsg.builder().register(onSendMsg);
    }


    public void removeNotify(OnSendMsg onSendMsg) {
        EventSendMsg.builder().unregister(onSendMsg);
    }


    private void managePrgFile(FileModel fileModel) {
        long progress = (100 * sendLength) / fileModel.getFileLength() ;
        fileModel.setProgress(progress);
        EventSendMsg.builder().notifyProgress(fileModel);

        // Log.e(TAG, "managePrgFile: " + progress );
        if(progress >= 100) {
            sendLength = 0 ;
        }else {
            files.set(0 , fileModel);
        }
    }


    private void writeFile(FileModel file) {
        try {

            Log.e(TAG, "writeFile: " + file.getFilePath());
            InputStream in ;

            if(file.getType() != MsgType.APK)
                in = App.context.getContentResolver().openInputStream(Uri.parse(file.getFilePath()));
            else
                in = new FileInputStream(new File(file.getFilePath()));


            byte[] buf = new byte[Patterns.BUF_SIZE];
            int len;

            long time  = System.currentTimeMillis();


            while ((len = in.read(buf , 0 , Patterns.BUF_SIZE)) != -1) {


                // sendBytes(buf , len);
                try {
                    objectOutputStream.write(buf , 0 , len);
                    objectOutputStream.flush();
                    sendLength += len;
                    managePrgFile(file);
                    Log.e(TAG, "doInBackground: " + (sendLength / (1024 * 1024)) + " MB" );
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }



                //Log.e(TAG, "sending File !!" );

            }

            Log.e(TAG, "End Sending : "  +  (( Math.abs(System.currentTimeMillis() - time)) / 1000));
            in.close();
            Thread.sleep(500);

        } catch (FileNotFoundException e) {
            Log.e(TAG, "Exception File Not Found : " + e.getMessage() );
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }



    private void writeAvatar(Uri uri) {

        try {

            InputStream in = App.context.getContentResolver().openInputStream(uri);

            byte[] buf = new byte[Patterns.BUF_SIZE];
            int len;
            Log.e(TAG, "Start Sending Avatar !" );
            while ((len = in.read(buf)) != -1) {
                sendBytes(buf , len);
            }

            Log.e(TAG, "Send All Avatar !! "  );
            in.close();

        } catch (FileNotFoundException e) {
            App.getInstance().showCustomToast("Sending Avatar Problem : " + e.getMessage() , Toast.LENGTH_SHORT , R.color.colorRed);
            Log.e(TAG, "Exception : " + e.getMessage() );
            e.printStackTrace();
        } catch (IOException e) {
            App.getInstance().showCustomToast("Sending Avatar Problem : " + e.getMessage() , Toast.LENGTH_SHORT , R.color.colorRed);
            e.printStackTrace();
        }

    }



    public void sendText(String text , boolean isOnline) {

        if(isOnline) {
            App.getInstance().initThread(new OnThread() {
                @Override
                public void onReady() {

                    if(Patterns.builder().isMsgSafe(text)) {
                        sendBytes(Patterns.builder().generateTextMsg(text));
                        EventSendMsg.builder().notifyMembers(text);
                    }

                }
            });

        }else {
            App.getInstance().showCustomToast(App.getInstance().getResString(R.string.cant_send_msg_when_offline) ,
                    Toast.LENGTH_SHORT , R.color.colorRed);
        }


    }



    public void sendNoSpace(Long needBytes) {
        App.getInstance().initThread(new OnThread() {
            @Override
            public void onReady() {
                sendBytes(Patterns.builder().generateNoSpaceMsg(needBytes));
            }
        });
    }


    public void sendOkSpace() {
        App.getInstance().initThread(new OnThread() {
            @Override
            public void onReady() {
                sendBytes(Patterns.builder().generateOkSpaceMsg());
            }
        });
    }


    private void sendBytes(byte[] bytes) {
        try {
            objectOutputStream.write(bytes);
            objectOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void sendBytes(byte[] bytes , int length) {
        try {
            objectOutputStream.write(bytes , 0 , length);
            objectOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void sendUserInfo(UserEntity userInfo ) {

        App.getInstance().initThread(new OnThread() {
            @Override
            public void onReady() {
                try {

                    Thread.sleep(600);
                    sendBytes(Patterns.builder().generateUserInfoMsg(userInfo));
                    Thread.sleep(2500);

                    if(HotspotManager.isServer)
                        sendUserAvatar(Uri.parse(userInfo.userAvatar) , userInfo.getUniqueID());

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }


    public void sendUserAvatarByClient(Uri photo , String uniqueID) {

        App.getInstance().initThread(new OnThread() {
            @Override
            public void onReady() {
                try {
                    sendBytes(Patterns.builder().generateUserAvatarMsg(photo , uniqueID));
                    Thread.sleep(1200);
                    writeAvatar(photo);
                }  catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });


    }


    public void sendUserAvatar(Uri photo , String uniqueID) {

        try {
            sendBytes(Patterns.builder().generateUserAvatarMsg(photo , uniqueID));
            Thread.sleep(1200);
            writeAvatar(photo);
        }  catch (InterruptedException e) {
            e.printStackTrace();
        }

    }



    private void checkRecieverSpace(long needBytes) {
        App.getInstance().initThread(new OnThread() {
            @Override
            public void onReady() {
                sendBytes(Patterns.builder().generateNeedSpaceMsg(needBytes));
            }
        });
    }


    public void sendFile(MsgType fileType , ArrayList<Uri> files , boolean isOnline) {

        if(isOnline) {

            if(SendMsg.this.files.size() == 0 && !RecieveMsg.builder().isRecievingFiles()) {

                long needBytes = 0;

                for (Uri file : files) {

                    switch (fileType) {

                        case Image:
                            SendMsg.this.files.add(ImgMsg.builder().generator(file));
                            break;

                        case Audio:
                            SendMsg.this.files.add(AudioMsg.builder().generator(file));
                            break;

                        case Video:
                            SendMsg.this.files.add(VideoMsg.builder().generator(file));
                            break;

                        case APK:
                            SendMsg.this.files.add(ApkMsg.builder().generator(file));
                            break;

                        case Document:
                            SendMsg.this.files.add(DocMsg.builder().generator(file));
                            break;
                    }

                    needBytes += Patterns.builder().getFileLength(file);
                }

                checkRecieverSpace(needBytes);

            }else {

                if(SendMsg.this.files.size() != 0)
                    App.getInstance().showCustomToast(App.getInstance().getResString(R.string.cant_send_until_befores_send_completed) ,
                            Toast.LENGTH_SHORT , R.color.colorPrimaryRed);
                else
                    App.getInstance().showCustomToast(App.getInstance().getResString(R.string.cant_send_until_befores_recieve_completed) ,
                            Toast.LENGTH_SHORT , R.color.colorPrimaryRed);
            }

        }else {
            App.getInstance().showCustomToast(App.getInstance().getResString(R.string.cant_send_msg_when_offline) ,
                    Toast.LENGTH_SHORT , R.color.colorRed);
        }


    }


    public void sendPendingFiles() {


        if(files.size() > 0) {

            EventSendMsg.builder().notifyMembers(files.get(0).getType() , files);

            for (FileModel file : files ) {
                Log.e(TAG, "Starting ....  " + file.getFilePath());
            }

            App.getInstance().initThread(new OnThread() {
                @Override
                public void onReady() {

                    try {
                        sendFileDetails(files);
                        Thread.sleep(500);

                        for (FileModel file : files ) {
                            Log.e(TAG, "Starting ....  " + file.getFilePath());
                            writeFile(file);
                        }

                        files.clear();
                        Log.e(TAG, "cleared All !" );

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                }
            });
        }
    }

    private void sendFileDetails(ArrayList<FileModel> files) {

        switch (files.get(0).getType()) {

            case Image:
                sendBytes(Patterns.builder().generateImgMsg(files));
                break;

            case Audio:
                sendBytes(Patterns.builder().generateAudioMsg(files));
                break;

            case Video:
                sendBytes(Patterns.builder().generateVideoMsg(files));
                break;

            case APK:
                sendBytes(Patterns.builder().generateApkMsg(files));
                break;

            case Document:
                sendBytes(Patterns.builder().generateDocMsg(files));
                break;

        }


    }



    public void removePendingFiles() {
        files.clear();
    }



    public void destroy() {
        instance = null;
    }


}
