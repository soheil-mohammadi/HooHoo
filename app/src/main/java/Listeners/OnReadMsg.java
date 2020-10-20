package Listeners;

import java.util.ArrayList;

import Entities.UserEntity;
import core.models.FileModel;

public interface OnReadMsg {

    void onReceiveUserInfo(UserEntity userInfo);
    void onReceiveUserAvatar(FileModel avatar);
    void onReceiveText(String message);
    void onReceiveImg(ArrayList<FileModel> imgs);
    void onReceiveAudio(ArrayList<FileModel> audios);
    void onReceiveVideo(ArrayList<FileModel> audios);
    void onReceiveApk(ArrayList<FileModel> apks);
    void onReceiveDocument(ArrayList<FileModel> audios);
    void onReceivePrgImg(FileModel img , long progress);
    void onReceivePrgVideo(FileModel video , long progress);
    void onReceivePrgApk(FileModel apk , long progress);
    void onReceivePrgAudio(FileModel audio , long progress);
    void onReceivePrgDocument(FileModel document , long progress);
    void onReceiveNoSpace(long needBytes);

}
