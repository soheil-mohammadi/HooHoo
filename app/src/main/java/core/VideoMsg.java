package core;

import android.net.Uri;

import java.io.File;
import java.util.UUID;

import core.models.FileModel;
import servers.monitor.fastest.hoohoonew.App;

public class VideoMsg {

    private static VideoMsg instance ;
    private final String pathVideoDir = "Videos";

    public static VideoMsg builder() {
        if(instance == null)
            instance = new VideoMsg();

        return instance;
    }

    public VideoMsg() {
       File imgDir =  new File(App.getInstance().getExtPath() + pathVideoDir);
       if(!imgDir.exists())
           imgDir.mkdir();

    }

    private String getDirPath() {
        return App.getInstance().getExtPath() + pathVideoDir + "/" ;
    }

    public String pathGenerator(String mimeType) {
       return getDirPath() +  UUID.randomUUID().toString() + mimeType;
    }


    public FileModel generator(String mimeType , long fileLength) {
       return new FileModel(pathGenerator(mimeType) , fileLength , MsgType.Video ,0);
    }


    public FileModel generator(Uri video) {
        return new FileModel(video.toString(), Patterns.builder().getFileLength(video)
                ,MsgType.Video ,0);

    }
}
