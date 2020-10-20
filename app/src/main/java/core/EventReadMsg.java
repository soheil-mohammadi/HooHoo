package core;


import android.util.Log;

import java.util.ArrayList;
import java.util.HashSet;

import Entities.UserEntity;
import Listeners.OnReadMsg;
import core.models.FileModel;

public class EventReadMsg {


    private static EventReadMsg instance ;
    private final HashSet<OnReadMsg> mListeners = new HashSet<OnReadMsg>();

    public static EventReadMsg builder() {
        if(instance == null)
            instance = new EventReadMsg();

        return instance;
    }


    public void register(OnReadMsg listener)
    {
        if(!mListeners.contains(listener))
            mListeners.add(listener);
    }


    public void unregister(OnReadMsg listener)
    {
        mListeners.remove(listener);
    }


    private static final String TAG = "EventReadMsg";

    public void notifyMembers(MsgType msgType , Object value) {

        for(OnReadMsg onReadMsg : mListeners) {
            switch (msgType) {

                case USER_INFO:
                    onReadMsg.onReceiveUserInfo((UserEntity) value);
                    break;

                case USER_AVATAR:
                    onReadMsg.onReceiveUserAvatar((FileModel) value);
                    break;

                case Text:
                    onReadMsg.onReceiveText((String) value);
                    break;

                case Image:
                    onReadMsg.onReceiveImg((ArrayList<FileModel>) value);
                    break;

                case Audio:
                    onReadMsg.onReceiveAudio((ArrayList<FileModel>) value);
                    break;

                case Video:
                    onReadMsg.onReceiveVideo((ArrayList<FileModel>) value);
                    break;

                case APK:
                    onReadMsg.onReceiveApk((ArrayList<FileModel>) value);
                    break;

                case Document:
                    onReadMsg.onReceiveDocument((ArrayList<FileModel>) value);
                    break;
            }
        }
    }


    public void notifyNoSpace(long needBytes) {

        for(OnReadMsg onReadMsg : mListeners) {
            onReadMsg.onReceiveNoSpace(needBytes);
        }
    }


    public void notifyProgress(FileModel fileModel) {
        for(OnReadMsg onReadMsg : mListeners) {

            switch (fileModel.getType()) {

                case USER_AVATAR:
                    if(fileModel.getProgress() == 100)
                        onReadMsg.onReceiveUserAvatar(fileModel);
                    break;

                case Image:
                    onReadMsg.onReceivePrgImg(fileModel , fileModel.getProgress());
                    break;

                case Audio:
                    onReadMsg.onReceivePrgAudio(fileModel , fileModel.getProgress());
                    break;

                case Video:
                    onReadMsg.onReceivePrgVideo(fileModel , fileModel.getProgress());
                    break;

                case APK:
                    onReadMsg.onReceivePrgApk(fileModel , fileModel.getProgress());
                    break;

                case Document:
                    onReadMsg.onReceivePrgDocument(fileModel , fileModel.getProgress());
                    break;
            }
        }
    }

}
