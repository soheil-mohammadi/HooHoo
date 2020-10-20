package Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import FrgPresenter.BaseFrgPresenter;
import FrgPresenter.SplashPresenter;
import FrgView.BaseFrgView;
import FrgView.SplashView;
import Listeners.OnFrgDestroy;
import servers.monitor.fastest.hoohoonew.App;
import servers.monitor.fastest.hoohoonew.R;

/**
 * Created by soheilmohammadi on 9/23/18.
 */

public class SplashFrg extends BaseFragment {

    private static final String TAG = "MainFrg";

    public static SplashFrg newInstance() {
        Bundle args = new Bundle();
        SplashFrg fragment = new SplashFrg();
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((SplashPresenter) presenter).onAttach(context);
    }

    @Override
    int idResFrame() {
        return R.id.frame_activity_main;
    }

    @Override
    BaseFrgPresenter getPresenter() {
        return new SplashPresenter();
    }

    @Override
    BaseFrgView getViewArc() {
        return new SplashView();
    }

    @Override
    public void onClick(View view) {
        viewArc.onClick(view);
    }

    @Override
    void onReback() {
        ((SplashPresenter) presenter).onReback();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ((SplashPresenter) presenter).onActivityResult(requestCode , resultCode , data);
    }

    @Override
    public void onBackPressed() {
        ((SplashPresenter) presenter).onBackPressed();
    }

    @Override
    public void onStop() {
        super.onStop();
        ((SplashPresenter) presenter).onStop();
    }

    @Override
    OnFrgDestroy destroy() {
        return new OnFrgDestroy() {
            @Override
            public void onDestroy() {
                App.getInstance().getCurrentActivity().finish();
            }
        };
    }
}
