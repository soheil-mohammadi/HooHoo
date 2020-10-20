package activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import Presenters.BasePresenter;
import Views.BaseView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import servers.monitor.fastest.hoohoonew.App;

/**
 * Created by soheilmohammadi on 9/4/18.
 */

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {

    BasePresenter presenter;
    BaseView view ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getInstance().setCurrentActivity(this);
        App.getInstance().setLocale(App.getInstance().getCurrentLang());
        doSomeThing();
        presenter = getPresenter();
        view = getView();
        view.onCreate(presenter);
        presenter.onCreate(view);

    }


    @Override
    protected void onResume() {
        super.onResume();
        App.getInstance().setCurrentActivity(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        App.getInstance().setCurrentActivity(this);
    }

    abstract BasePresenter getPresenter();
    abstract BaseView getView();
    abstract void doSomeThing();

}
