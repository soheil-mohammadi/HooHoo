package servers.monitor.fastest.hoohoonew;


import Dao.ChatContentDao;
import Dao.ChatHeaderDao;
import Dao.UserDao;
import Entities.ChatContentEntity;
import Entities.ChatHeaderEntity;
import Entities.UserEntity;
import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = { UserEntity.class ,
        ChatHeaderEntity.class , ChatContentEntity.class}, exportSchema = false, version = 1)

public abstract class AppDatabase extends RoomDatabase {

   public abstract UserDao userDao();
   public abstract ChatHeaderDao chatHeaderDao();
   public abstract ChatContentDao chatContentDao();
}