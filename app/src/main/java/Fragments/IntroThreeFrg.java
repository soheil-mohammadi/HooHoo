package Fragments;

import android.os.Bundle;
import android.view.View;

import FrgPresenter.BaseFrgPresenter;
import FrgPresenter.IntroThreePresenter;
import FrgPresenter.IntroTwoPresenter;
import FrgView.BaseFrgView;
import FrgView.IntroThreeView;
import FrgView.IntroTwoView;
import Listeners.OnFrgDestroy;
import servers.monitor.fastest.hoohoonew.R;

public class IntroThreeFrg extends BaseFragment {

    public static IntroThreeFrg newInstance() {
        Bundle args = new Bundle();
        IntroThreeFrg fragment = new IntroThreeFrg();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    int idResFrame() {
        return R.id.frame_activity_main;
    }

    @Override
    BaseFrgPresenter getPresenter() {
        return new IntroThreePresenter();
    }

    @Override
    BaseFrgView getViewArc() {
        return new IntroThreeView();
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
