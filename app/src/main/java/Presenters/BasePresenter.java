package Presenters;

import android.view.View;

import Views.BaseView;
import androidx.appcompat.app.AppCompatActivity;
import servers.monitor.fastest.hoohoonew.App;

/**
 * Created by soheilmohammadi on 9/4/18.
 */

public class BasePresenter {

    AppCompatActivity activity;
    View view ;
    BaseView viewArc;


    public BasePresenter(AppCompatActivity activity) {
        this.activity = activity;
    }

    public void onCreate(BaseView view){
        this.view = App.getInstance().getCurrentActivity().getWindow().getDecorView();
        this.viewArc = view;
    }


}
