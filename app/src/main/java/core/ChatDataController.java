package core;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import Models.ChatValueModel;

public class ChatDataController {

    private static final String TAG = "ChatDataController";

    private final Map<Integer, ChatValueModel> indexObj = new HashMap<>();
   // private final Map<Integer, Integer> keyIndex = new HashMap<>();
    private final ArrayList <Integer> keyIndex = new ArrayList<>();

    private static ChatDataController instance ;

    public static ChatDataController builder() {
        if(instance == null)
            instance =  new ChatDataController();

        return instance;
    }


    public boolean add(ChatValueModel chat) {

        int key = chat.getUniqueID();

        if (!keyIndex.contains(key)) {
            keyIndex.add(key);
            indexObj.put(key, chat);
            return true;
        }

        return false;

    }

    public void addAll(ArrayList<ChatValueModel> chats) {

        for (ChatValueModel chat : chats) {
            int key = chat.getUniqueID();

            if (!keyIndex.contains(key)) {
                keyIndex.add(key);
                indexObj.put(key, chat);
            }

        }

    }

    public ChatValueModel get(int uniqueID) {
        return indexObj.get(uniqueID);
    }

    public int getPosition(int uniqueID) {
        return keyIndex.indexOf(uniqueID);
    }


    public ArrayList<ChatValueModel> getAll() {
        return new ArrayList<>(indexObj.values());
    }


    public void destroy() {
        indexObj.clear();
        keyIndex.clear();
    }
}