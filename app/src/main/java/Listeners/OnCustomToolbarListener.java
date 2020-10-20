package Listeners;

import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by soheilmohammadi on 9/17/17.
 */

public interface OnCustomToolbarListener {
    void  on_navBar_clicked();
    void handle_toolbar_component_(TextView title, ImageView icon, ImageView img_extra_tool_one_toolbar,
                                   ImageView img_extra_tool_two_toolbar);
}
