package Dao;

import java.util.List;

import Entities.ChatContentEntity;
import Entities.ChatHeaderEntity;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;


@Dao
public interface ChatContentDao {

    @Insert
    void insertRow(ChatContentEntity chatContentEntity);

    @Query("SELECT * FROM ChatContent WHERE uniqueID IN (:uniqueID) ")
    List<ChatContentEntity> fetchChats(String uniqueID);

    @Query("SELECT * FROM ChatContent WHERE rowID IN (:rowID) ")
    ChatContentEntity fetchChat(int rowID);


}
