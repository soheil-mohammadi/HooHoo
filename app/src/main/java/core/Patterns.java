package core;

import android.content.ContentResolver;
import android.content.res.AssetFileDescriptor;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.UUID;

import Dao.UserDao;
import Entities.UserEntity;
import androidx.room.TypeConverter;
import core.models.FileModel;
import okio.Utf8;
import servers.monitor.fastest.hoohoonew.App;
import servers.monitor.fastest.hoohoonew.R;

public class Patterns {


    public static final int BUF_SIZE = 2048;  // ---> 10 KB Reading / Writing  ....
    public static final String ENDER_ID = "%#@!!";
    public static final String USER_BIO_DEFAULT = "Empty Bio HooHoo";
    private static final String TAG = "Patterns";

    private final String MARK = "HoHo";
    private final String SPLIT = "@#@";
    private final String TEXT_USER_INFO_MSG = "HUI";
    private final String TEXT_USER_AVATAR_MSG = "HUA";
    private final String TEXT_MSG = "HT";
    private final String TEXT_NEED_SPACE_MSG = "HNES";
    private final String TEXT_NO_SPACE_MSG = "HNS";
    private final String TEXT_OK_SPACE_MSG = "HOS";
    private final String IMG_MSG = "HI";
    private final String VIDEO_MSG = "HV";
    private final String APK_MSG = "HAP";
    private final String AUDIO_MSG = "HA";
    private final String DOC_MSG = "HD";

    public static final String UNKNOWN = "Unknown";

    private static Patterns instance ;


    public static final String hotspotPassword  = "288137555";


    public long getFileLength(Uri uri) {
        try {
            AssetFileDescriptor afd = App.context.getContentResolver().openAssetFileDescriptor(uri,"r");
            long fileSize = afd.getLength();
            afd.close();
            return fileSize;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return 0;

    }


    public byte[] generateTextMsg(String text) {
        return (TEXT_MSG + SPLIT + text + ENDER_ID).getBytes();
    }

    public byte[] generateNeedSpaceMsg(long needBytes) {
        return (TEXT_NEED_SPACE_MSG + SPLIT + needBytes + ENDER_ID).getBytes();
    }

    public byte[] generateNoSpaceMsg(long needBytes) {
        return (TEXT_NO_SPACE_MSG + SPLIT + needBytes + ENDER_ID).getBytes();
    }

    public byte[] generateOkSpaceMsg() {
        return (TEXT_OK_SPACE_MSG + SPLIT + ENDER_ID).getBytes();
    }


    public byte[] generateUserInfoMsg(UserEntity userEntity) {
        return (TEXT_USER_INFO_MSG + SPLIT + userEntity.getUniqueID() + SPLIT +  userEntity.getUserName()
                + SPLIT + userEntity.getUserSex() + SPLIT + userEntity.getUserBio() + ENDER_ID).getBytes();
    }


    public byte[] generateUserAvatarMsg(Uri userAvatar , String uniqueID) {
        return (TEXT_USER_AVATAR_MSG  + SPLIT + uniqueID + getMimeType(userAvatar) + SPLIT +
                getFileLength(userAvatar) + ENDER_ID).getBytes();
    }


    public byte[] generateImgMsg(Uri photo) {
        return (IMG_MSG  + SPLIT + getMimeType(photo) + SPLIT + getFileLength(photo)).getBytes();
    }


    public byte[] generateImgMsg(ArrayList<FileModel> photos) {

        StringBuilder msg = new StringBuilder() ;

        msg.append(IMG_MSG  + SPLIT);

        Log.e(TAG, "Start ImgMsg: " + photos.size());

        for (FileModel photo : photos) {

            msg.append(getMimeType(Uri.parse(photo.getFilePath())) + "#" +
                    getFileLength(Uri.parse(photo.getFilePath())) + "/");

        }

        msg.append(ENDER_ID);
        return msg.toString().getBytes();
    }



    public byte[] generateVideoMsg(Uri video) {
        return (VIDEO_MSG  + SPLIT + getMimeType(video) + SPLIT + getFileLength(video)).getBytes();
    }



    public byte[] generateVideoMsg(ArrayList<FileModel> videos) {

        StringBuilder msg = new StringBuilder() ;

        msg.append(VIDEO_MSG  + SPLIT);


        for (FileModel video : videos) {
            msg.append(getMimeType(Uri.parse(video.getFilePath())) + "#" +
                    getFileLength(Uri.parse(video.getFilePath())) + "/"); //---> 20
        }

        msg.append(ENDER_ID);
        return msg.toString().getBytes();
    }


    public byte[] generateApkMsg(ArrayList<FileModel> apks) {

        StringBuilder msg = new StringBuilder() ;

        msg.append(APK_MSG  + SPLIT);

        for (FileModel apk : apks) {
            msg.append(apk.getFileLength() + "#" + apk.getFileName()  + "/");
        }

        msg.append(ENDER_ID);
        return msg.toString().getBytes();
    }


    public byte[] generateAudioMsg(Uri audio) {
        return (AUDIO_MSG  + SPLIT + getMimeType(audio) + SPLIT + getFileLength(audio)).getBytes();
    }


    public byte[] generateAudioMsg(ArrayList<FileModel> audios) {

        StringBuilder msg = new StringBuilder() ;

        msg.append(AUDIO_MSG  + SPLIT);

        for (FileModel audio : audios) {
            msg.append(getMimeType(Uri.parse(audio.getFilePath())) + "#" +
                    getFileLength(Uri.parse(audio.getFilePath())) + "/"); //---> 20
        }

        msg.append(ENDER_ID);
        return msg.toString().getBytes();
    }


    public byte[] generateDocMsg(Uri doc) {
        return (DOC_MSG  + SPLIT + getMimeType(doc) + SPLIT + getFileLength(doc)).getBytes();
    }


    public byte[] generateDocMsg(ArrayList<FileModel> docs) {

        StringBuilder msg = new StringBuilder() ;

        msg.append(DOC_MSG  + SPLIT);

//        for (Uri uri : uris) {
//            String path = uri.getPath();
//            Log.e(TAG, "Docs Picked: " +  path + " .... " +
//                    uri.getLastPathSegment());
//        }

        //Maximum : 40

        for (FileModel doc : docs) {
            msg.append(getMimeType(Uri.parse(doc.getFilePath())) + "#" +
                    getFileLength(Uri.parse(doc.getFilePath())) + "/"); //---> 20
        }

        msg.append(ENDER_ID);
        return msg.toString().getBytes();
    }


    public String readTextMsg(byte[] text) {
        return new String(text).split(SPLIT)[1].trim();
    }


    public UserEntity readUserInfoMsg(byte[] text) {
        String[] userInfo = new String(text).trim().split(SPLIT);
        return new UserEntity(userInfo[2] , userInfo[4] , null ,
                userInfo[1] , Integer.parseInt(userInfo[3]));
    }


    public FileModel readAvatarMsg(byte[] text) {
        String content = new String(text).trim();
        String filePath = content.split(SPLIT)[1] ;
        long length = Long.parseLong(content.split(SPLIT)[2]) ;
        return AvatarMsg.builder().generator(filePath , length) ;
    }


    public long readNoSpaceMsg(byte[] text) {
        return Long.parseLong(new String(text).split(SPLIT)[1].trim());
    }

    public long readNeedSpaceMsg(byte[] text) {
        String content = new String(text).trim();
        return Long.parseLong(content.split(SPLIT)[1]);
    }


//    public FileModel readImgMsg(byte[] text) {
//        String content = new String(text).trim();
//        String mimeType = content.split(SPLIT)[1] ;
//        long length = Long.parseLong(content.split(SPLIT)[2]) ;
//        return ImgMsg.builder().generator(mimeType , length) ;
//    }
//
//


    public ArrayList<FileModel> readImgMsg(byte[] text) {
        ArrayList<FileModel> photos = new ArrayList<>();
        String content = new String(text).trim();

        String[] files = content.split(SPLIT)[1].split("/") ;

        for (int i = 0; i <files.length ; i++) {
            String[] details = files[i].split("#");
            photos.add(ImgMsg.builder().generator(details[0] , Long.parseLong(details[1]) ));
        }

        return photos;
    }


    public long getAvailableDeviceSpace() {
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        return (long) stat.getAvailableBlocks() * (long) stat.getBlockSize();
    }

//    public FileModel readVideoMsg(byte[] text) {
//        String content = new String(text).trim();
//        String mimeType = content.split(SPLIT)[1] ;
//        long length = Long.parseLong(content.split(SPLIT)[2]) ;
//        return VideoMsg.builder().generator(mimeType , length);
//    }



    public ArrayList<FileModel> readVideoMsg(byte[] text) {
        ArrayList<FileModel> videos = new ArrayList<>();
        String content = new String(text).trim();

        String[] files = content.split(SPLIT)[1].split("/") ;

        for (int i = 0; i <files.length ; i++) {
            String[] details = files[i].split("#");
            videos.add(VideoMsg.builder().generator(details[0] , Long.parseLong(details[1]) ));
        }

        return videos;
    }




    public ArrayList<FileModel> readApkMsg(byte[] text) {
        ArrayList<FileModel> apks = new ArrayList<>();
        String content = new String(text).trim();

        String[] files = content.split(SPLIT)[1].split("/") ;

        for (int i = 0; i <files.length ; i++) {
            String[] details = files[i].split("#");
            apks.add(ApkMsg.builder().generator(Long.parseLong(details[0]) , details[1] ));
        }

        return apks;
    }



//    public FileModel readAudioMsg(byte[] text) {
//        String content = new String(text).trim();
//        String mimeType = content.split(SPLIT)[1] ;
//        long length = Long.parseLong(content.split(SPLIT)[2]) ;
//        return AudioMsg.builder().generator(mimeType ,length);
//    }




    public ArrayList<FileModel> readAudioMsg(byte[] text) {
        ArrayList<FileModel> audios = new ArrayList<>();
        String content = new String(text).trim();

        String[] files = content.split(SPLIT)[1].split("/") ;

        for (int i = 0; i <files.length ; i++) {
            String[] details = files[i].split("#");
            audios.add(AudioMsg.builder().generator(details[0] , Long.parseLong(details[1]) ));
        }

        return audios;
    }

//    public FileModel readDocMsg(byte[] text) {
//        String content = new String(text).trim();
//        String mimeType = content.split(SPLIT)[1] ;
//        long length = Long.parseLong(content.split(SPLIT)[2]) ;
//        return DocMsg.builder().generator(mimeType , length);
//    }


    public ArrayList<FileModel> readDocMsg(byte[] text) {
        ArrayList<FileModel> docs = new ArrayList<>();
        String content = new String(text).trim();

        String[] files = content.split(SPLIT)[1].split("/") ;

        for (int i = 0; i <files.length ; i++) {
            String[] details = files[i].split("#");
            docs.add(DocMsg.builder().generator(details[0] , Long.parseLong(details[1]) ));
        }

        return docs;
    }


    public int generateUniqueId() {
        UUID idOne = UUID.randomUUID();
        String str=""+idOne;
        int uid=str.hashCode();
        String filterStr=""+uid;
        str=filterStr.replaceAll("-", "");
        return Integer.parseInt(str);
    }


    public String getMimeType(Uri uri) {
        String path = uri.toString();
        return path.substring(path.lastIndexOf("."));
    }


    public boolean isMsgSafe(String msg) {
        String ms = msg.trim();
        if(ms.equals(TEXT_USER_INFO_MSG) || ms.equals(TEXT_USER_AVATAR_MSG)
                || ms.equals(TEXT_MSG) || ms.equals(IMG_MSG) || ms.equals(VIDEO_MSG)
                || ms.equals(AUDIO_MSG) || ms.equals(DOC_MSG) ) {

            App.getInstance().showCustomToast(App.getInstance().getResString(R.string.cant_send_special_chars) ,
                    Toast.LENGTH_SHORT , R.color.colorRed);

            return false ;
        }

        return true;
    }

    public MsgType getTypeMsg(byte[] bytes ) {
        String msg = new String(bytes).trim();
        Log.e(TAG, "getTypeMsg: " + msg );

        if(msg.contains(SPLIT)) {
            String type =  msg.split(SPLIT)[0];

            switch (type) {

                case TEXT_NEED_SPACE_MSG :
                    return MsgType.NEED_SPACE;


                case TEXT_NO_SPACE_MSG :
                    return MsgType.NO_SPACE;


                case TEXT_OK_SPACE_MSG :
                    return MsgType.OK_SPACE;


                case TEXT_USER_INFO_MSG :
                    return MsgType.USER_INFO;


                case TEXT_USER_AVATAR_MSG :
                    return MsgType.USER_AVATAR;


                case TEXT_MSG :
                    return MsgType.Text;


                case IMG_MSG :
                    return MsgType.Image;


                case APK_MSG :
                    return MsgType.APK;


                case VIDEO_MSG :
                    return MsgType.Video;


                case AUDIO_MSG :
                    return MsgType.Audio;


                case DOC_MSG :
                    return MsgType.Document;


            }
        }

        return null;
    }

    public String bytesToMb(long bytes) {
        return  String.format("%.3f" ,  bytes / (1024f * 1024f));
    }



    public static Patterns builder() {
        if(instance == null)
            instance = new Patterns();

        return instance;
    }

    public String getDeviceName() {
        UserDao userDao  = App.getInstance().getAppDatabase().userDao();
        UserEntity userEntity = userDao.fetchUser();

        return MARK + userEntity.getUserName() + SPLIT +
                userEntity.getUserSex()  + SPLIT + UUID.randomUUID().toString().split("-")[0];
    }






    public UserEntity manageDeviceInfo(String generatedName) {

        if(generatedName.contains(MARK)){

            String[] deviceInfo = generatedName.replace(MARK ,"").split(SPLIT);
            return new UserEntity(deviceInfo[0] , "" , null ,
                    deviceInfo[2] , Integer.valueOf(deviceInfo[1]));
        }else {
            return null;
        }
    }

}
