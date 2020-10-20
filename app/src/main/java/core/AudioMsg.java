package core;

import android.net.Uri;

import java.io.File;
import java.util.UUID;

import core.models.FileModel;
import servers.monitor.fastest.hoohoonew.App;

public class AudioMsg {

    private static AudioMsg instance ;
    private final String pathAudioDir = "Audios";

    public static AudioMsg builder() {
        if(instance == null)
            instance = new AudioMsg();

        return instance;
    }

    public AudioMsg() {
       File imgDir =  new File(App.getInstance().getExtPath() + pathAudioDir);
       if(!imgDir.exists())
           imgDir.mkdir();

    }

    private String getDirPath() {
        return App.getInstance().getExtPath() + pathAudioDir + "/" ;
    }

    public String pathGenerator(String mimeType) {
       return getDirPath() +  UUID.randomUUID().toString() + mimeType;
    }


    public FileModel generator(String mimeType  , long fileLength) {
       return new FileModel(pathGenerator(mimeType) , fileLength , MsgType.Audio ,0);
    }


    public FileModel generator(Uri audio) {
        return new FileModel(audio.toString(), Patterns.builder().getFileLength(audio)
                ,MsgType.Audio ,0);

    }
}
