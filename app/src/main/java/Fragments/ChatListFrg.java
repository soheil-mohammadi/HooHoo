package Fragments;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import FrgPresenter.BaseFrgPresenter;
import FrgPresenter.ChatListPresenter;
import FrgView.BaseFrgView;
import FrgView.ChatListView;
import Listeners.OnFrgDestroy;
import Utils.PermissionManager;
import servers.monitor.fastest.hoohoonew.App;
import servers.monitor.fastest.hoohoonew.R;

public class ChatListFrg extends BaseFragment {

    public static ChatListFrg newInstance() {
        Bundle args = new Bundle();
        ChatListFrg fragment = new ChatListFrg();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        PermissionManager.builder().grant(PermissionManager.STAOREG, new PermissionManager.OnPermissionResult() {
            @Override
            public void onAccepted() {

            }

            @Override
            public void onDeny() {

            }
        });

    }



    @Override
    int idResFrame() {
        return R.id.frame_activity_main;
    }

    @Override
    BaseFrgPresenter getPresenter() {
        return new ChatListPresenter();
    }

    @Override
    BaseFrgView getViewArc() {
        return new ChatListView();
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
