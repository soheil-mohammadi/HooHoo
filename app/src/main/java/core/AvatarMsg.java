package core;

import android.Manifest;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.util.UUID;

import core.models.FileModel;
import servers.monitor.fastest.hoohoonew.App;
import servers.monitor.fastest.hoohoonew.R;

public class AvatarMsg {

    private static AvatarMsg instance ;
    private final String pathAvatarDir = "Avatars";

    private static final String TAG = "AvatarMsg";

    public static AvatarMsg builder() {
        if(instance == null)
            instance = new AvatarMsg();

        return instance;
    }

    private AvatarMsg() {
        File avatarDir =  new File(App.getInstance().getExtPath() + pathAvatarDir);
        if(!avatarDir.exists()) {
            avatarDir.mkdir();
        }


    }

    private String getDirPath() {
        return App.getInstance().getExtPath() + pathAvatarDir + "/" ;
    }

    String pathGenerator(String fileName) {
        File avatar = new File(getDirPath() + fileName);
        if(avatar.exists()) {
            Log.e(TAG, "Remove Before Avatar: " + avatar.delete() );
        }
        return getDirPath() +  fileName ;
    }


    FileModel generator(String filePath , long fileLength) {
        File avatarFile = new File(filePath);
        if(avatarFile.exists())
            avatarFile.delete();
        return new FileModel(pathGenerator(filePath) , fileLength , MsgType.USER_AVATAR,0);
    }

}
