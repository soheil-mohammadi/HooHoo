package Models;

import java.util.ArrayList;
import java.util.Random;

import servers.monitor.fastest.hoohoonew.App;
import servers.monitor.fastest.hoohoonew.R;

public class ChatsFakeModel {

    private ArrayList<Integer> resBitmaps = new ArrayList<>();
    private static ChatsFakeModel instance ;


    public static ChatsFakeModel builder() {
        if(instance == null)
            instance = new ChatsFakeModel();

        return instance;
    }



    public ChatsFakeModel() {
        resBitmaps.add(R.drawable.ic_man_avatar);
        resBitmaps.add(R.drawable.ic_girl_avatar);
        resBitmaps.add(R.drawable.ic_char_1);
        resBitmaps.add(R.drawable.ic_char_2);
        resBitmaps.add(R.drawable.ic_char_3);
        resBitmaps.add(R.drawable.ic_char_4);
        resBitmaps.add(R.drawable.ic_char_5);
        resBitmaps.add(R.drawable.ic_char_6);
        resBitmaps.add(R.drawable.ic_char_7);
        resBitmaps.add(R.drawable.ic_char_8);
        resBitmaps.add(R.drawable.ic_char_9);
        resBitmaps.add(R.drawable.ic_char_10);
    }


    public int getRandomAvatar(boolean isMale) {

         int min = isMale ? 1 : 5;
         int max = isMale ? 6 : 10;

         return App.context.getResources().getIdentifier("ic_char_" +
                         App.getInstance().getRandom(min , max) ,
                 "drawable"  , App.context.getPackageName());
    }

    public String getRandomAvatarName(boolean isMale) {

         int min = isMale ? 1 : 6;
         int max = isMale ? 6 : 10;
         return "ic_char_" + App.getInstance().getRandom(min , max) ;
    }


    public ArrayList<Integer> list() {
        return  resBitmaps ;
    }
}