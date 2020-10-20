package Listeners;

import java.util.ArrayList;

import core.models.FileModel;

public interface OnSendMsg {

    void onSendText(String message);
    void onSendImg(ArrayList<FileModel> imgs);
    void onSendAudio(ArrayList<FileModel> audios);
    void onSendVideo(ArrayList<FileModel> videos);
    void onSendApk(ArrayList<FileModel> apks);
    void onSendDocument(ArrayList<FileModel> documents);
    void onSendPrgImg(FileModel img, long progress);
    void onSendPrgVideo(FileModel video, long progress);
    void onSendPrgApk(FileModel apk, long progress);
    void onSendPrgAudio(FileModel audio, long progress);
    void onSendPrgDocument(FileModel document, long progress);
    void onSendNoSpace(long needBytes);
}
