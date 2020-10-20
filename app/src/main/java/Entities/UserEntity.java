package Entities;


import java.io.Serializable;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "UserInfo")

public class UserEntity implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id ;

    private String userName;
    public String userBio ;
    public String userAvatar ;
    private int userSex;
    public String uniqueID;


    public UserEntity(int id, String userName, String userBio, String userAvatar, String uniqueID , int userSex) {
        this.id = id;
        this.userName = userName;
        this.userBio = userBio;
        this.userAvatar = userAvatar;
        this.uniqueID = uniqueID;
        this.userSex = userSex;
    }


    @Ignore
    public UserEntity( String userName, String userBio, String userAvatar, String uniqueID , int userSex) {
        this.userName = userName;
        this.userBio = userBio;
        this.userAvatar = userAvatar;
        this.uniqueID = uniqueID;
        this.userSex = userSex;
    }


    public String getUserAvatar() {
        return userAvatar;
    }


    public String getUniqueID() {
        return uniqueID;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserBio(String userBio) {
        this.userBio = userBio;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public void setUserSex(int userSex) {
        this.userSex = userSex;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserBio() {
        return userBio;
    }

    public int getUserSex() {
        return userSex;
    }
}
