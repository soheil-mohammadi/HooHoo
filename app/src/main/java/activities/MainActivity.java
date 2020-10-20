package activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.jetbrains.annotations.NotNull;

import java.io.ObjectInputStream;
import java.util.ArrayList;

import Fragments.BaseDialogFragment;
import Fragments.BaseFragment;
import Fragments.ChatFrg;
import Fragments.IntroThreeFrg;
import FrgPresenter.ChatPresenter;
import FrgPresenter.IntroThreePresenter;
import Presenters.BasePresenter;
import Presenters.MainPresenter;
import Utils.PermissionManager;
import Views.BaseView;
import Views.MainView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import core.ConnectionManager;
import lv.chi.photopicker.PhotoPickerFragment;
import servers.monitor.fastest.hoohoonew.App;
import servers.monitor.fastest.hoohoonew.R;

public class MainActivity extends BaseActivity implements PhotoPickerFragment.Callback{

    private static final String TAG = "MainActivity";

    public static void start() {
        Intent intent = new Intent(App.context , MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        App.context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainPresenter)presenter).onCreate();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }


    @Override
    protected void onResume() {
        super.onResume();
        ((MainPresenter)presenter).onResume();
    }

    @Override
    BasePresenter getPresenter() {
        return new Presenters.MainPresenter(this);
    }

    @Override
    BaseView getView() {
        return new MainView(this);
    }

    @Override
    void doSomeThing() {

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //App.getInstance().getCurrentFrg(R.id.frame_activity_main).onActivityResult(requestCode , resultCode , data);
    }

    @Override
    public void onBackPressed() {
        Object object = App.getInstance().getCurrentFrg(R.id.frame_activity_main);

      //  Log.e(TAG, "onBackPressed: " + object );
        if(object instanceof BaseFragment)
            ((BaseFragment) object).onBackPressed();
        else if(object instanceof BaseDialogFragment)
            ((BaseDialogFragment) object).onBackPressed();
        else
            super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        Object object = App.getInstance().getCurrentFrg(R.id.frame_activity_main);
        if(object instanceof BaseFragment )
            ((BaseFragment) object) .onClick(v);
        else if(object instanceof BaseDialogFragment)
            ((BaseDialogFragment) object) .onClick(v);
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }


    @Override
    protected void onDestroy() {
        ConnectionManager.builder().destroy();
        super.onDestroy();
    }

    @Override
    public void onImagesPicked(@NotNull ArrayList<Uri> photos) {

        Object currentFrg = App.getInstance().getCurrentFrg(R.id.frame_activity_main);

        if(currentFrg instanceof ChatFrg)
            ((ChatPresenter)  ((ChatFrg) currentFrg).presenter).imagesPicked(photos);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.builder().onResult(requestCode , permissions ,  grantResults);
    }

}
