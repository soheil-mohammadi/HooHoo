package core.models;

import java.io.Serializable;
import java.util.UUID;

import core.MsgType;
import core.Patterns;

public class FileModel implements Serializable {

    private int uniqueID ;
    private String filePath ;
    private long fileLength ;
    private long progress ;
    private MsgType type ;
    private String fileName ;

    public FileModel( String filePath, long fileLength , MsgType type  , long progress) {
        this.uniqueID = Patterns.builder().generateUniqueId();
        this.filePath = filePath;
        this.type = type;
        this.fileLength = fileLength;
        this.progress = progress;
    }

    public FileModel( String filePath , String fileName , long fileLength , MsgType type  , long progress) {
        this.uniqueID = Patterns.builder().generateUniqueId();
        this.filePath = filePath;
        this.fileName = fileName;
        this.type = type;
        this.fileLength = fileLength;
        this.progress = progress;
    }


    public int getUniqueID() {
        return uniqueID;
    }

    public long getFileLength() {
        return fileLength;
    }

    public String getFilePath() {
        return filePath;
    }

    public long getProgress() {
        return progress;
    }

    public MsgType getType() {
        return type;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setProgress(long progress) {
        this.progress = progress;
    }

    public void setType(MsgType type) {
        this.type = type;
    }


    public String getFileName() {
        return fileName;
    }

    public void setFileLength(long fileLength) {
        this.fileLength = fileLength;
    }

    public void setUniqueID(int uniqueID) {
        this.uniqueID = uniqueID;
    }
}
