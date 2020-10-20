package Fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import Entities.UserEntity;
import FrgPresenter.BaseDialogFrgPresenter;
import FrgPresenter.ChatProfileDialogPresenter;
import FrgPresenter.DiscoverDialogPresenter;
import FrgView.BaseDialogFrgView;
import FrgView.ChatProfileDialogView;
import FrgView.DiscoverDialogView;
import Listeners.OnFrgDestroy;

public class ChatProfileDialogFrg extends BaseDialogFragment {

    private static final String TAG = "ChatProfileDialogFrg";
    public static String USER_ARG = "userArg";

    public static ChatProfileDialogFrg newInstance(UserEntity userEntity) {
        Bundle args = new Bundle();
        ChatProfileDialogFrg fragment = new ChatProfileDialogFrg();
        args.putSerializable(USER_ARG , userEntity);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    boolean isBlur() {
        return true;
    }

    @Override
    boolean isCancelableDialog() {
        return true;
    }

    @Override
    DialogInterface.OnDismissListener onDismiss() {
        return new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                ((ChatProfileDialogPresenter)presenter).onDismissed();
            }
        };
    }

    @Override
    BaseDialogFrgPresenter getPresenter() {
        return new ChatProfileDialogPresenter();
    }

    @Override
    BaseDialogFrgView getViewArc() {
        return new ChatProfileDialogView();
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
