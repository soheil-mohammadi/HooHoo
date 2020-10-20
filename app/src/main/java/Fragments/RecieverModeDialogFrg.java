package Fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import FrgPresenter.BaseDialogFrgPresenter;
import FrgPresenter.DiscoverDialogPresenter;
import FrgPresenter.RecieverModeDialogPresenter;
import FrgView.BaseDialogFrgView;
import FrgView.DiscoverDialogView;
import FrgView.RecieverModeDialogView;
import Listeners.OnFrgDestroy;

public class RecieverModeDialogFrg extends BaseDialogFragment {

    private static final String TAG = "RecieverModeDialogFrg";

    public static RecieverModeDialogFrg newInstance() {
        Bundle args = new Bundle();
        RecieverModeDialogFrg fragment = new RecieverModeDialogFrg();
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
                ((RecieverModeDialogPresenter)presenter).onDismissed();
            }
        };
    }

    @Override
    BaseDialogFrgPresenter getPresenter() {
        return new RecieverModeDialogPresenter();
    }

    @Override
    BaseDialogFrgView getViewArc() {
        return new RecieverModeDialogView();
    }

    @Override
    public void onClick(View view) {
        viewArc.onClick(view);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((RecieverModeDialogPresenter)presenter).onResume();
    }

    @Override
    OnFrgDestroy destroy() {
        return null;
    }
}
