package Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.VpnService;
import android.os.Bundle;
import android.view.View;

import FrgPresenter.BaseFrgPresenter;
import FrgPresenter.MainPresenter;
import FrgView.BaseFrgView;
import FrgView.MainView;
import Listeners.OnFrgDestroy;
import core.ConnectionManager;
import servers.monitor.fastest.hoohoonew.App;
import servers.monitor.fastest.hoohoonew.R;


/**
 * Created by soheilmohammadi on 9/23/18.
 */

public class MainFrg extends BaseFragment {

    private static final String TAG = "MainFrg";


    public static MainFrg newInstance() {
        Bundle args = new Bundle();
        MainFrg fragment = new MainFrg();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    int idResFrame() {
        return R.id.frame_activity_main;
    }

    @Override
    BaseFrgPresenter getPresenter() {
        return new MainPresenter();
    }

    @Override
    BaseFrgView getViewArc() {
        return new MainView();
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
    OnFrgDestroy destroy() {
        return new OnFrgDestroy() {
            @Override
            public void onDestroy() {
                App.getInstance().getCurrentActivity().finish();
            }
        };
    }
    

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ((MainPresenter) presenter).onActivityResult(requestCode ,  resultCode , data);
    }

    @Override
    public void onStop() {
        super.onStop();
        ((MainPresenter) presenter).onStop();
    }

    @Override
    public void onDestroy() {
        ((MainPresenter) presenter).onDestroy();
        super.onDestroy();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((MainPresenter) presenter).onAttach(context);
    }



}
