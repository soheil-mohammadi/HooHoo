package Fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import com.ms_square.etsyblur.BlurConfig;
import com.ms_square.etsyblur.BlurDialogFragment;

import FrgPresenter.BaseDialogFrgPresenter;
import FrgView.BaseDialogFrgView;
import Listeners.OnFrgDestroy;
import androidx.annotation.Nullable;
import servers.monitor.fastest.hoohoonew.App;

/**
 * Created by soheilmohammadi on 9/22/18.
 */

public abstract class BaseDialogFragment extends BlurDialogFragment implements View.OnClickListener {

    private static final String TAG = "BaseDialogFragment";

    public View view;
    public BaseDialogFrgPresenter presenter;
    public BaseDialogFrgView viewArc ;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        presenter = getPresenter();
        viewArc = getViewArc();
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(App.getInstance().getCurrentActivity());

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(viewArc.getLayout());
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(isCancelableDialog());



        view  = dialog.getWindow().getDecorView();
        viewArc.onCreate(view , presenter);
        presenter.onCreate(this , view , viewArc);

        return dialog;

    }




    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();
    }



    @Override
    public void onDismiss(DialogInterface dialog) {

        if(onDismiss() != null) {
            onDismiss().onDismiss(dialog);
            super.onDismiss(dialog);
        } else {
            super.onDismiss(dialog);
        }

    }

    public void finish() {
        dismiss();
    }

    public void onBackPressed() {
        dismiss();
    }

    abstract boolean isBlur();
    abstract boolean isCancelableDialog();
    abstract DialogInterface.OnDismissListener onDismiss();
    abstract BaseDialogFrgPresenter getPresenter();
    abstract BaseDialogFrgView getViewArc();
    abstract OnFrgDestroy destroy();

}
