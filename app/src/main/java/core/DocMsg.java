package core;

import android.net.Uri;

import java.io.File;
import java.util.UUID;

import core.models.FileModel;
import servers.monitor.fastest.hoohoonew.App;

public class DocMsg {

    private static DocMsg instance ;
    private final String pathDocDir = "Documents";

    public static DocMsg builder() {
        if(instance == null)
            instance = new DocMsg();

        return instance;
    }

    public DocMsg() {
       File imgDir =  new File(App.getInstance().getExtPath() + pathDocDir);
       if(!imgDir.exists())
           imgDir.mkdir();

    }

    private String getDirPath() {
        return App.getInstance().getExtPath() + pathDocDir + "/" ;
    }

    public String pathGenerator(String mimeType) {
       return getDirPath() +  UUID.randomUUID().toString() + mimeType;
    }


    public FileModel generator(String mimeType ,long fileLength) {
       return new FileModel(pathGenerator(mimeType) , fileLength ,  MsgType.Document ,0);
    }

    public FileModel generator(Uri document) {
        return new FileModel(document.toString(), Patterns.builder().getFileLength(document)
                ,MsgType.Document ,0);

    }
}
