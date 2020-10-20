package Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import FrgPresenter.BaseFrgPresenter;
import FrgPresenter.InstalledAppsPresenter;
import FrgPresenter.SplashPresenter;
import FrgView.BaseFrgView;
import FrgView.InstalledAppsView;
import FrgView.SplashView;
import Listeners.OnFrgDestroy;
import servers.monitor.fastest.hoohoonew.App;
import servers.monitor.fastest.hoohoonew.R;

/**
 * Created by soheilmohammadi on 9/23/18.
 */

public class InstalledAppsFrg extends BaseFragment {

    private static final String TAG = "InstalledAppsFrg";

    public static InstalledAppsFrg newInstance() {
        Bundle args = new Bundle();
        InstalledAppsFrg fragment = new InstalledAppsFrg();
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    int idResFrame() {
        return R.id.frame_activity_main;
    }

    @Override
    BaseFrgPresenter getPresenter() {
        return new InstalledAppsPresenter();
    }

    @Override
    BaseFrgView getViewArc() {
        return new InstalledAppsView();
    }

    @Override
    public void onClick(View view) {
        viewArc.onClick(view);
    }

    @Override
    void onReback() {
        //TODO
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    OnFrgDestroy destroy() {
        return null;
    }
}
