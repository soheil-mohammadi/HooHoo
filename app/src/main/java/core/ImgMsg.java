package core;

import android.net.Uri;

import java.io.File;
import java.util.UUID;

import core.models.FileModel;
import servers.monitor.fastest.hoohoonew.App;

public class ImgMsg {

    private static ImgMsg instance ;
    private final String pathImgDir = "Images";

    public static ImgMsg builder() {
        if(instance == null)
            instance = new ImgMsg();

        return instance;
    }

     private ImgMsg() {
       File imgDir =  new File(App.getInstance().getExtPath() + pathImgDir);
       if(!imgDir.exists())
           imgDir.mkdir();

    }

    private String getDirPath() {
        return App.getInstance().getExtPath() + pathImgDir + "/" ;
    }

     public String pathGenerator(String mimeType) {
       return getDirPath() +  UUID.randomUUID().toString() + mimeType;
    }


    public FileModel generator(String mimeType , long fileLength) {
       return new FileModel(pathGenerator(mimeType) , fileLength , MsgType.Image,0);
    }


   public FileModel generator(Uri photo) {
        return new FileModel(photo.toString(), Patterns.builder().getFileLength(photo)
                ,MsgType.Image,0);

    }
}
