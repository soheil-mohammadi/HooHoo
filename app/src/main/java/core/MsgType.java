package core;

import java.io.Serializable;

import androidx.room.TypeConverter;

public enum MsgType implements Serializable {

    NEED_SPACE (0) ,
    OK_SPACE  (1),
    NO_SPACE (2),
    USER_INFO (3),
    Text (4),
    USER_AVATAR (5),
    Image (6),
    Video (7),
    Audio (8),
    Document (9) ,
    APK (10) ;


    private int code;

    MsgType(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }


    @TypeConverter
    public static MsgType getType(Integer numeral){
        for(MsgType ds : values()){
            if(ds.code == numeral){
                return ds;
            }
        }
        return null;
    }

    @TypeConverter
    public static Integer getTypeInt(MsgType status){

        if(status != null)
            return status.code;

        return  null;
    }

}
