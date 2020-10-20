package DateController;

import java.util.ArrayList;

import Dao.ChatHeaderDao;
import Entities.ChatHeaderEntity;
import servers.monitor.fastest.hoohoonew.App;

public class ChatHeaderManager {

    private static ChatHeaderManager instance ;

    private ChatHeaderDao chatHeaderDao ;

    public static ChatHeaderManager builder() {
        if(instance == null)
            instance = new ChatHeaderManager();

        return instance;
    }


    public ChatHeaderManager() {
        chatHeaderDao = App.getInstance().getAppDatabase().chatHeaderDao();
    }


    public  void insertNewChat(ChatHeaderEntity chatHeaderEntity) {

        if(chatHeaderDao.fetchChat(chatHeaderEntity.uniqueID) == null) {
            chatHeaderDao.insertChat(chatHeaderEntity);
        }else  {
            chatHeaderDao.updateChat(chatHeaderEntity.uniqueID , chatHeaderEntity.chatWithName ,
                    chatHeaderEntity.bio , chatHeaderEntity.lastMsg ,
                    chatHeaderEntity.avatar , chatHeaderEntity.date);
        }
    }

    public ArrayList<ChatHeaderEntity> getAllChats() {
        return (ArrayList<ChatHeaderEntity>) chatHeaderDao.fetchChats();
    }

    public ChatHeaderEntity getChat(String uniqueID) {
        return chatHeaderDao.fetchChat(uniqueID);
    }
}
