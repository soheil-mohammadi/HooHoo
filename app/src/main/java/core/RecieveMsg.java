package core;

import android.util.Log;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.util.ArrayList;

import Entities.UserEntity;
import Listeners.OnReadMsg;
import androidx.annotation.NonNull;
import core.models.FileModel;
import servers.monitor.fastest.hoohoonew.App;
import servers.monitor.fastest.hoohoonew.R;

public class RecieveMsg  {

    private static final String TAG = "RecieveMsg";

    private static RecieveMsg instance ;
    private long recievedLength = 0;

    private ArrayList<FileModel> files = new ArrayList<>();



    public static RecieveMsg builder() {
        if(instance == null)
            instance = new RecieveMsg();

        return instance;
    }


    public enum ReaderState {

        FILE_COMPLETED ,
        FILE_READING ,
        MSG_READING

    }


    public boolean isRecievingFiles() {
        return files.size() > 0 ;
    }


    private long time ;
    public void setNotify(OnReadMsg onReadMsg) {
        EventReadMsg.builder().register(onReadMsg);
    }

    public void removeNotify(OnReadMsg onReadMsg) {
        EventReadMsg.builder().unregister(onReadMsg);
    }

    public ReaderState handleMsg(byte[] bytes , int riddenLength) {

        if(files.size() > 0) {

            FileModel fileModel = files.get(0);

            writeToFile(fileModel , bytes , riddenLength);
            long progress = (100 * recievedLength) / fileModel.getFileLength() ;
            fileModel.setProgress(progress);
            notifyFilePrg(fileModel);
            if(progress == 100) {
                Log.e(TAG, "End Time : " + (( Math.abs(System.currentTimeMillis() - time)) / 1000));
                recievedLength = 0 ;
                files.remove(0);
                return ReaderState.FILE_COMPLETED;
            }else {
                files.set(0 , fileModel);
                return ReaderState.FILE_READING;
            }


        } else  {

            time =  System.currentTimeMillis();
            MsgType msgType = Patterns.builder().getTypeMsg(bytes);

            Log.e(TAG, "handleMsg: " + msgType );

            if(msgType != null) {

                switch (msgType) {

                    case NEED_SPACE:

                        long needSpaceBytes = Patterns.builder().readNeedSpaceMsg(bytes);

                        if(Patterns.builder().getAvailableDeviceSpace() > needSpaceBytes) {
                            SendMsg.builder().sendOkSpace();
                        }else {
                            SendMsg.builder().sendNoSpace(needSpaceBytes);
                            EventReadMsg.builder().notifyNoSpace(needSpaceBytes);
                        }

                        break;


                    case OK_SPACE:
                        SendMsg.builder().sendPendingFiles();
                        break;


                    case NO_SPACE:
                        EventSendMsg.builder().notifyNoSpace(Patterns.builder().readNoSpaceMsg(bytes));
                        SendMsg.builder().removePendingFiles();
                        break;

                    case USER_AVATAR:
                        FileModel userAvatar = Patterns.builder().readAvatarMsg(bytes) ;
                        files.add(userAvatar);
                        break;

                    case Image:
                        ArrayList<FileModel> photos = Patterns.builder().readImgMsg(bytes) ;
                        files.addAll(photos);
                        EventReadMsg.builder().notifyMembers(msgType , photos);
                        break;

                    case Audio:
                        ArrayList<FileModel> audios = Patterns.builder().readAudioMsg(bytes) ;
                        files.addAll(audios);
                        EventReadMsg.builder().notifyMembers(msgType , audios);
                        break;

                    case Video:
                        ArrayList<FileModel> videos = Patterns.builder().readVideoMsg(bytes) ;
                        files.addAll(videos);
                        EventReadMsg.builder().notifyMembers(msgType , videos);
                        break;

                    case APK:
                        ArrayList<FileModel> apks = Patterns.builder().readApkMsg(bytes) ;
                        files.addAll(apks);
                        EventReadMsg.builder().notifyMembers(msgType , apks);
                        break;

                    case Document:
                        ArrayList<FileModel> documents = Patterns.builder().readDocMsg(bytes) ;
                        files.addAll(documents);
                        EventReadMsg.builder().notifyMembers(msgType , documents);
                        break;


                    case USER_INFO:
                        UserEntity userEntity = Patterns.builder().readUserInfoMsg(bytes);
                        EventReadMsg.builder().notifyMembers(msgType ,userEntity );
                        break;


                    default:
                        EventReadMsg.builder().notifyMembers(msgType , Patterns.builder().readTextMsg(bytes));
                        break;

                }

            }

            return ReaderState.MSG_READING;
        }

    }


    private void notifyFilePrg(FileModel fileModel) {
        EventReadMsg.builder().notifyProgress(fileModel);
    }


    private void writeToFile(FileModel fileModel , byte[] buffer , int riddenLength) {
        try {
            OutputStream out = new FileOutputStream(fileModel.getFilePath() , true);
            out.write(buffer , 0, riddenLength);
            out.flush();
            out.close();
            recievedLength += riddenLength;

            Log.e(TAG, "writeToFile: " + buffer.length + " .... " + riddenLength );
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void destroy() {
        instance = null;
    }

}
