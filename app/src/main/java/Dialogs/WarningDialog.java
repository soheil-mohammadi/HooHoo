package Dialogs;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

import Listeners.OnCustomClick;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatCheckBox;
import life.sabujak.roundedbutton.RoundedButton;
import servers.monitor.fastest.hoohoonew.App;
import servers.monitor.fastest.hoohoonew.R;

public class WarningDialog {

    private static WarningDialog instance;
    private Dialog dialog;


    private LinearLayout container_reminder_dialog_warning ;
    private AppCompatCheckBox ch_reminder_dialog_warning ;
    private LottieAnimationView lte_dialog_warning ;
    private TextView txt_dialog_warning  , txt_desc_dialog_warning;
    private RoundedButton btn_dialog_warning_accept , btn_dialog_warning_deny;

    public static WarningDialog builder() {
        if(instance == null)
            instance = new WarningDialog();

        return instance;
    }


    public WarningDialog() {

        dialog = new Dialog(App.getInstance().getCurrentActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_warning);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT ,  LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        initViews();
    }


    private void initViews() {
        container_reminder_dialog_warning = dialog.findViewById(R.id.container_reminder_dialog_warning);
        ch_reminder_dialog_warning = dialog.findViewById(R.id.ch_reminder_dialog_warning);
        lte_dialog_warning = dialog.findViewById(R.id.lte_dialog_warning);
        txt_dialog_warning = dialog.findViewById(R.id.txt_dialog_warning);
        txt_desc_dialog_warning = dialog.findViewById(R.id.txt_desc_dialog_warning);
        btn_dialog_warning_accept = dialog.findViewById(R.id.btn_dialog_warning_accept);
        btn_dialog_warning_deny = dialog.findViewById(R.id.btn_dialog_warning_deny);
    }

    private static final String TAG = "WarningDialog";

    public void show(@NonNull final String text , @NonNull final String desc , @NonNull int rawAnimRes , @Nullable final OnCustomClick onClickAccept ,
                     @Nullable final OnCustomClick onClickDeny , boolean isShowReminder) {


        lte_dialog_warning.setAnimation(rawAnimRes);

        txt_dialog_warning.setText(text);
        txt_desc_dialog_warning.setText(desc);

        if(isShowReminder)
            container_reminder_dialog_warning.setVisibility(View.VISIBLE);
        else
            container_reminder_dialog_warning.setVisibility(View.GONE);

        if(onClickAccept != null)
            btn_dialog_warning_accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickAccept.onClicked(isShowReminder  ? ch_reminder_dialog_warning.isChecked() : null);
                    dialog.dismiss();
                }
            });
        else
            btn_dialog_warning_accept.setVisibility(View.GONE);


        if(onClickDeny != null)
            btn_dialog_warning_deny.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickDeny.onClicked(null);
                    dialog.dismiss();
                }
            });
        else
            btn_dialog_warning_deny.setVisibility(View.GONE);


        dialog.show();
        instance = null;

    }



}
