package Entities;


import java.io.Serializable;

import Models.ChatsFakeModel;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "ChatHeader")

public class ChatHeaderEntity  implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id ;

    public String uniqueID ;
    public String chatWithName;
    public String bio ;
    public String lastMsg;
    public String avatar ;
    public int isMale ;
    public long date ;


    public ChatHeaderEntity(int id, String uniqueID, String chatWithName, String bio, String lastMsg,
                            String avatar, int isMale ,long date) {
        this.id = id;
        this.uniqueID = uniqueID;
        this.chatWithName = chatWithName;
        this.bio = bio;
        this.lastMsg = lastMsg;
        this.avatar = avatar;
        this.isMale = isMale;
        this.date = date;
    }

    @Ignore
    public ChatHeaderEntity(String uniqueID, String chatWithName, String bio, String lastMsg,
                            String avatar,int isMale , long date) {
        this.uniqueID = uniqueID;
        this.chatWithName = chatWithName;
        this.bio = bio;
        this.lastMsg = lastMsg;
        this.avatar = avatar;
        this.isMale = isMale;
        this.date = date;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getUniqueID() {
        return uniqueID;
    }

    public String getChatWithName() {
        return chatWithName;
    }

    public String getBio() {
        return bio;
    }

    public String getLastMsg() {
        return lastMsg;
    }

    public String getAvatar() {
        return avatar;
    }

    public long getDate() {
        return date;
    }

    public int getIsMale() {
        return isMale;
    }
}
