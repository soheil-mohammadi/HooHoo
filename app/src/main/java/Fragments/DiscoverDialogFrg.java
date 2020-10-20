package Fragments;

import android.content.DialogInterface;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import FrgPresenter.BaseDialogFrgPresenter;
import FrgPresenter.DiscoverDialogPresenter;
import FrgView.BaseDialogFrgView;
import FrgView.DiscoverDialogView;
import Listeners.OnFrgDestroy;

public class DiscoverDialogFrg extends BaseDialogFragment {

    private static final String TAG = "DiscoverDialogFrg";

    public static DiscoverDialogFrg newInstance() {
        Bundle args = new Bundle();
        DiscoverDialogFrg fragment = new DiscoverDialogFrg();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    boolean isBlur() {
        return true;
    }

    @Override
    boolean isCancelableDialog() {
        return false;
    }

    @Override
    DialogInterface.OnDismissListener onDismiss() {
        return new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                ((DiscoverDialogPresenter)presenter).onDismissed();
            }
        };
    }

    @Override
    BaseDialogFrgPresenter getPresenter() {
        return new DiscoverDialogPresenter();
    }

    @Override
    BaseDialogFrgView getViewArc() {
        return new DiscoverDialogView();
    }

    @Override
    public void onClick(View view) {
        viewArc.onClick(view);
    }


    @Override
    OnFrgDestroy destroy() {
        return null;
    }
}
