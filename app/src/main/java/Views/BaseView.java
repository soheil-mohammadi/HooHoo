package Views;

import android.view.View;

import Presenters.BasePresenter;
import androidx.appcompat.app.AppCompatActivity;
import servers.monitor.fastest.hoohoonew.App;


/**
 * Created by soheilmohammadi on 9/4/18.
 */

public abstract class BaseView {


    AppCompatActivity activity;
    View view ;
    BasePresenter presenter;


    public BaseView(AppCompatActivity activity) {
        this.activity = activity;
    }

    public void onCreate(BasePresenter presenter) {
        this.presenter = presenter;
        doBeforeLayoutSetting();
        App.getInstance().getCurrentActivity().setContentView(getLayout());
        view = App.getInstance().getCurrentActivity().getWindow().getDecorView();
        initLayout();
        doSomeThing();

    }


    abstract int getLayout();
    abstract void initLayout();
    public abstract void doSomeThing();
    abstract void doBeforeLayoutSetting();
    public abstract void onClick(View view);

}
