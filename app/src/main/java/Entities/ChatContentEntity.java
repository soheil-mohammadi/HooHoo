package Entities;


import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import core.MsgType;
import core.Patterns;

@Entity(tableName = "ChatContent")

public class ChatContentEntity {

    @PrimaryKey(autoGenerate = true)
    private int id ;

    private String uniqueID ;
    private int rowID ;
    private String value ;
    @TypeConverters(MsgType.class)
    private MsgType msgType ;
    private boolean isSender ;
    private long time ;

    public ChatContentEntity(int id, String uniqueID, int rowID, String value,
                             MsgType msgType, boolean isSender, long time) {
        this.id = id;
        this.uniqueID = uniqueID;
        this.rowID = rowID;
        this.value = value;
        this.msgType = msgType;
        this.isSender = isSender;
        this.time = time;
    }

    @Ignore
    public ChatContentEntity(String uniqueID, int rowID, String value,
                             MsgType msgType, boolean isSender, long time) {
        this.uniqueID = uniqueID;
        this.rowID = rowID;
        this.value = value;
        this.msgType = msgType;
        this.isSender = isSender;
        this.time = time;
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

    public String getValue() {
        return value;
    }

    public MsgType getMsgType() {
        return msgType;
    }

    public boolean isSender() {
        return isSender;
    }

    public long getTime() {
        return time;
    }

    public int getRowID() {
        return rowID;
    }
}
