package DateController;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;

import Dao.ChatContentDao;
import Entities.ChatContentEntity;
import Models.ChatValueModel;
import core.MsgType;
import core.SerializeObject;
import core.models.FileModel;
import servers.monitor.fastest.hoohoonew.App;

public class ChatContentManager {

    private static ChatContentManager instance ;

    private ChatContentDao chatContentDao ;

    private static final String TAG = "ChatContentManager";

    public static ChatContentManager builder() {
        if(instance == null)
            instance = new ChatContentManager();

        return instance;
    }


    public ChatContentManager() {
        chatContentDao = App.getInstance().getAppDatabase().chatContentDao();
    }


    public  void insertNewRow(ChatContentEntity chatContentEntity) {
        chatContentDao.insertRow(chatContentEntity);
    }

    public ArrayList<ChatContentEntity> getAllChats(String uniqueID) {
        return (ArrayList<ChatContentEntity>) chatContentDao.fetchChats(uniqueID);
    }



    public void saveRowChat(String uniqueID  , int rowID , String value ,
                             MsgType msgType , boolean isSender , long time) {

        if(chatContentDao.fetchChat(rowID) == null) {
            insertNewRow(new ChatContentEntity(uniqueID, rowID, value,
                    msgType, isSender, time));
        }
    }

    public void saveRowChat(String uniqueID  , int rowID , FileModel value ,
                             MsgType msgType , boolean isSender , long time) {


        if(chatContentDao.fetchChat(rowID) == null) {
            insertNewRow(new ChatContentEntity(uniqueID ,  rowID ,
                    SerializeObject.builder().objectToString( value) ,
                    msgType , isSender , time));
        }

    }
}
