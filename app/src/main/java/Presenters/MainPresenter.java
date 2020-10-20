package Presenters;


import Fragments.MainFrg;
import Fragments.SplashFrg;
import androidx.appcompat.app.AppCompatActivity;
import servers.monitor.fastest.hoohoonew.App;
import servers.monitor.fastest.hoohoonew.R;

/**
 * Created by soheilmohammadi on 10/5/18.
 */

public class MainPresenter  extends  BasePresenter{

    private static final String TAG = "MainPresenter";

    public MainPresenter(AppCompatActivity activity) {
        super(activity);

    }


    public void onCreate() {
        setSplash();
    }


    public void onResume() {
     //   checkConnection();
    }


    private void setSplash() {
        App.getInstance().showFrg(R.id.frame_activity_main , true , SplashFrg.newInstance());
    }


}
