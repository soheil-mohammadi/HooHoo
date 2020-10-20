package Fragments;

import android.os.Bundle;
import android.view.View;

import FrgPresenter.BaseFrgPresenter;
import FrgPresenter.IntroOnePresenter;
import FrgPresenter.IntroTwoPresenter;
import FrgView.BaseFrgView;
import FrgView.IntroOneView;
import FrgView.IntroTwoView;
import Listeners.OnFrgDestroy;
import servers.monitor.fastest.hoohoonew.R;

public class IntroOneFrg extends BaseFragment {

    public static IntroOneFrg newInstance() {
        Bundle args = new Bundle();
        IntroOneFrg fragment = new IntroOneFrg();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    int idResFrame() {
        return R.id.frame_activity_main;
    }

    @Override
    BaseFrgPresenter getPresenter() {
        return new IntroOnePresenter();
    }

    @Override
    BaseFrgView getViewArc() {
        return new IntroOneView();
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
