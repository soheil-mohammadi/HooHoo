package Dao;

import java.util.ArrayList;
import java.util.List;

import Entities.ChatHeaderEntity;
import Entities.UserEntity;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


@Dao
public interface ChatHeaderDao {

    @Insert
    void insertChat(ChatHeaderEntity chatHeaderEntity);

    @Query("SELECT * FROM ChatHeader")
    List<ChatHeaderEntity> fetchChats();

    @Query("SELECT * FROM ChatHeader WHERE uniqueID IN (:uniqueID) ")
    ChatHeaderEntity fetchChat(String uniqueID);

    @Query("UPDATE ChatHeader SET chatWithName = :chatWithName , " +
            "bio= :bio , lastMsg = :lastMsg , avatar = :avatar  , date = :date WHERE uniqueID IN (:uniqueID) ")
    void updateChat(String uniqueID , String chatWithName , String bio ,
                    String lastMsg , String avatar , long date);

}
