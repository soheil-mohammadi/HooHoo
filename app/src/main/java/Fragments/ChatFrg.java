package Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import FrgPresenter.BaseFrgPresenter;
import FrgPresenter.ChatPresenter;
import FrgView.BaseFrgView;
import FrgView.ChatListView;
import FrgView.ChatView;
import Listeners.OnFrgDestroy;
import servers.monitor.fastest.hoohoonew.R;

public class ChatFrg extends BaseFragment {


    private static final String TAG = "ChatFrg";

    public static final String offlineKey = "isOffline";

    public static ChatFrg newInstance() {
        Bundle args = new Bundle();
        ChatFrg fragment = new ChatFrg();
        fragment.setArguments(args);
        return fragment;
    }


    public static ChatFrg newInstance(String uniqueID) {
        Bundle args = new Bundle();
        ChatFrg fragment = new ChatFrg();
        args.putString(offlineKey , uniqueID);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    int idResFrame() {
        return R.id.frame_activity_main;
    }

    @Override
    BaseFrgPresenter getPresenter() {
        return new ChatPresenter();
    }

    @Override
    BaseFrgView getViewArc() {
        return new ChatView();
    }

    @Override
    public void onClick(View view) {
        viewArc.onClick(view);
    }

    @Override
    void onReback() {
        ((ChatPresenter) presenter).onReback();
    }

    @Override
    public void onBackPressed() {
        ((ChatPresenter)presenter).onBackPressed();
    }

    @Override
    OnFrgDestroy destroy() {
        return null;
    }
}
