package Utils;

import android.content.SharedPreferences;

import servers.monitor.fastest.hoohoonew.App;

import static android.content.Context.MODE_PRIVATE;

public class SharedManager {

    private static SharedManager instance ;

    private SharedPreferences sharedPreferences ;


    public SharedManager() {
        sharedPreferences = App.context.getSharedPreferences("HooHooNew"
                + App.context.getPackageName() , MODE_PRIVATE);
    }

    public static SharedManager builder() {
        if(instance == null)
            instance = new SharedManager();

        return instance;
    }


    public void set(String key , String value) {
        sharedPreferences.edit().putString(key , value).apply();
    }

    public String get(String key , String defValue) {
        return sharedPreferences.getString(key , defValue);
    }

    public void set(String key , int value) {
        sharedPreferences.edit().putInt(key , value).apply();
    }

    public int get(String key , int defValue) {
        return sharedPreferences.getInt(key , defValue);
    }

    public void set(String key , boolean value) {
        sharedPreferences.edit().putBoolean(key , value).apply();
    }

    public void setL(String key , long value) {
        sharedPreferences.edit().putLong(key , value).apply();
    }

    public long getL(String key , long value) {
        return sharedPreferences.getLong(key , value);
    }

    public boolean get(String key , boolean defValue) {
        return sharedPreferences.getBoolean(key , defValue);
    }
}
