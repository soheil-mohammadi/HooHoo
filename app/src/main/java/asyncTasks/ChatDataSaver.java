package asyncTasks;

import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;

import DateController.ChatContentManager;
import DateController.ChatHeaderManager;
import Entities.ChatHeaderEntity;
import Entities.UserEntity;
import Models.ChatValueModel;
import Models.ChatsFakeModel;
import core.MsgType;
import core.models.FileModel;

public class ChatDataSaver extends AsyncTask<Void , Void , Void> {

    private static final String TAG = "ChatDataSaver";

    private ArrayList<ChatValueModel> chatData ;
    private String onlineUniqueID;
    private String lastMsg;
    private UserEntity recieverUserInfo ;


    public ChatDataSaver(ArrayList<ChatValueModel> chatData , String onlineUniqueID , String lastMsg ,
                         UserEntity recieverUserInfo) {
        this.chatData = chatData;
        this.onlineUniqueID = onlineUniqueID;
        this.recieverUserInfo = recieverUserInfo;
        this.lastMsg = lastMsg;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        saveChatData();
        updateChatHeader();

        return null;
    }

    private void updateChatHeader() {
        if(recieverUserInfo != null)
            ChatHeaderManager.builder().insertNewChat(new ChatHeaderEntity(recieverUserInfo.getUniqueID() ,
                    recieverUserInfo.getUserName() , recieverUserInfo.getUserBio() , lastMsg ,
                    recieverUserInfo.getUserAvatar()
                    ,recieverUserInfo.getUserSex() , System.currentTimeMillis() ));
    }


    private void saveChatData() {

        for (ChatValueModel chatValue : chatData) {

            if (chatValue.getMsgType() == MsgType.Text)
                ChatContentManager.builder().saveRowChat(onlineUniqueID, chatValue.getUniqueID(),
                        (String) chatValue.getValue(), chatValue.getMsgType(), chatValue.isSender(), chatValue.getTime());
            else {
                if (chatValue.getMsgType() != MsgType.Text)
                    if (((FileModel) chatValue.getValue()).getProgress() == 100)
                        ChatContentManager.builder().saveRowChat(onlineUniqueID, chatValue.getUniqueID(),
                                (FileModel) chatValue.getValue(), chatValue.getMsgType(),
                                chatValue.isSender(), chatValue.getTime());

            }

        }

    }



}
