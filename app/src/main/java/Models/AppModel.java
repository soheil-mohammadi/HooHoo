package Models;

import android.graphics.drawable.Drawable;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;


public class AppModel {

    private Drawable logo ;
    private String appName ;
    private String apkPath ;
    private boolean isChecked ;

    public AppModel(Drawable logo, String appName, String apkPath , boolean isChecked) {
        this.logo = logo;
        this.appName = appName;
        this.apkPath = apkPath;
        this.isChecked = isChecked;
    }


    public Drawable getLogo() {
        return logo;
    }

    public String getAppName() {
        return appName;
    }

    public String getApkPath() {
        return apkPath;
    }

    public boolean isChecked() {
        return isChecked;
    }


    public void setLogo(Drawable logo) {
        this.logo = logo;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public void setApkPath(String apkPath) {
        this.apkPath = apkPath;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
