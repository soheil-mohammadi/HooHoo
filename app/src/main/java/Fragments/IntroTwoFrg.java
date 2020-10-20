package Fragments;

import android.os.Bundle;
import android.view.View;

import FrgPresenter.BaseFrgPresenter;
import FrgPresenter.IntroTwoPresenter;
import FrgView.BaseFrgView;
import FrgView.IntroTwoView;
import Listeners.OnFrgDestroy;
import servers.monitor.fastest.hoohoonew.R;

public class IntroTwoFrg extends BaseFragment {

    public static IntroTwoFrg newInstance() {
        Bundle args = new Bundle();
        IntroTwoFrg fragment = new IntroTwoFrg();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    int idResFrame() {
        return R.id.frame_activity_main;
    }

    @Override
    BaseFrgPresenter getPresenter() {
        return new IntroTwoPresenter();
    }

    @Override
    BaseFrgView getViewArc() {
        return new IntroTwoView();
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    void onReback() {

    }

    @Override
    OnFrgDestroy destroy() {
        return null;
    }
}
