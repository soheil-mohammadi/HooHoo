package Listeners;

import android.content.DialogInterface;

public interface OnAlertActionsClicked {

    void  onNegativeClicked(DialogInterface dialog);
    void  onPositiveClicked(DialogInterface dialog);
}
