package Dao;



import Entities.UserEntity;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


@Dao
public interface UserDao {

    @Insert
    void insertUser(UserEntity userEntity);

    @Query("SELECT * FROM UserInfo")
    UserEntity fetchUser();

    @Update
    void updateUser(UserEntity userEntity);

}
