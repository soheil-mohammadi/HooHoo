package core;


import java.util.ArrayList;
import java.util.HashSet;

import Listeners.OnReadMsg;
import Listeners.OnSendMsg;
import core.models.FileModel;

public class EventSendMsg {


    private static EventSendMsg instance ;
    private final HashSet<OnSendMsg> mListeners = new HashSet<OnSendMsg>();

    public static EventSendMsg builder() {
        if(instance == null)
            instance = new EventSendMsg();

        return instance;
    }


    public void register(OnSendMsg listener) {

        if(!mListeners.contains(listener))
            mListeners.add(listener);
    }


    public void unregister(OnSendMsg listener)
    {
        mListeners.remove(listener);
    }




    public void notifyMembers(String msg) {
        for(OnSendMsg onSendMsg : mListeners) {
            onSendMsg.onSendText(msg);
        }
    }


    public void notifyMembers(MsgType msgType ,ArrayList<FileModel> files) {

        for(OnSendMsg onSendMsg : mListeners) {

            switch (msgType) {

                case Image:
                    onSendMsg.onSendImg(files);
                    break;

                case Audio:
                    onSendMsg.onSendAudio(files);
                    break;

                case Video:
                    onSendMsg.onSendVideo(files);
                    break;

                case APK:
                    onSendMsg.onSendApk(files);
                    break;

                case Document:
                    onSendMsg.onSendDocument(files);
                    break;
            }
        }
    }


    public void notifyProgress(FileModel fileModel) {
        for(OnSendMsg onSendMsg : mListeners) {

            switch (fileModel.getType()) {

                case Image:
                    onSendMsg.onSendPrgImg(fileModel , fileModel.getProgress());
                    break;

                case Audio:
                    onSendMsg.onSendPrgAudio(fileModel , fileModel.getProgress());
                    break;

                case Video:
                    onSendMsg.onSendPrgVideo(fileModel , fileModel.getProgress());
                    break;

                case APK:
                    onSendMsg.onSendPrgApk(fileModel , fileModel.getProgress());
                    break;

                case Document:
                    onSendMsg.onSendPrgDocument(fileModel , fileModel.getProgress());
                    break;
            }
        }
    }

    public void notifyNoSpace(long needBytes) {
        for(OnSendMsg onSendMsg : mListeners) {
            onSendMsg.onSendNoSpace(needBytes);
        }
    }
}
