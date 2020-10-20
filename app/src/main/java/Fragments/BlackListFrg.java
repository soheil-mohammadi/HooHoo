package Fragments;

import android.os.Bundle;
import android.view.View;

import FrgPresenter.BaseFrgPresenter;
import FrgPresenter.BlackListPresenter;
import FrgPresenter.ChatListPresenter;
import FrgView.BaseFrgView;
import FrgView.BlackListView;
import FrgView.ChatListView;
import Listeners.OnFrgDestroy;
import servers.monitor.fastest.hoohoonew.R;

public class BlackListFrg extends BaseFragment {

    public static BlackListFrg newInstance() {
        Bundle args = new Bundle();
        BlackListFrg fragment = new BlackListFrg();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    int idResFrame() {
        return R.id.frame_activity_main;
    }

    @Override
    BaseFrgPresenter getPresenter() {
        return new BlackListPresenter();
    }

    @Override
    BaseFrgView getViewArc() {
        return new BlackListView();
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
