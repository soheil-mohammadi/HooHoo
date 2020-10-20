package Models;

import core.MsgType;
import core.Patterns;

/**
 * Created by soheilmohammadi on 10/5/17.
 */

public class ChatValueModel {

    private int uniqueID ;
    private Object value ;
    private boolean isSender ;
    private long time ;
    private MsgType msgType ;

    public ChatValueModel(int uniqueID , Object value ,
                          boolean isSender , long time , MsgType msgType ) {

        this.uniqueID = uniqueID ;
        this.value = value ;
        this.isSender = isSender ;
        this.time = time ;
        this.msgType = msgType ;

    }


    public int getUniqueID() {
        return uniqueID;
    }

    public Object getValue() {
        return value;
    }

    public boolean isSender() {
        return isSender;
    }

    public long getTime() {
        return time;
    }

    public MsgType getMsgType() {
        return msgType;
    }


    public void setUniqueID(int uniqueID) {
        this.uniqueID = uniqueID;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public void setSender(boolean sender) {
        isSender = sender;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setMsgType(MsgType msgType) {
        this.msgType = msgType;
    }
}
