package FrgView;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import life.sabujak.roundedbutton.RoundedButton;
import servers.monitor.fastest.hoohoonew.R;

public class RecieverModeDialogView extends BaseDialogFrgView {


    private TextView txt_state_recieve_mode_dialog  , txt_waiting_recieve_mode_dialog;
    private ImageView img_barcode_generator_recieve_mode_dialog , img_barcode_recieve_mode_dialog ;
    private RoundedButton btn_cancel_recieve_mode_dialog ;

    @Override
    public int getLayout() {
        return R.layout.reciever_mode_dialog;
    }

    @Override
    public void initViews() {
        txt_state_recieve_mode_dialog = view.findViewById(R.id.txt_state_recieve_mode_dialog);
        txt_waiting_recieve_mode_dialog = view.findViewById(R.id.txt_waiting_recieve_mode_dialog);
        btn_cancel_recieve_mode_dialog = view.findViewById(R.id.btn_cancel_recieve_mode_dialog);
        img_barcode_generator_recieve_mode_dialog = view.findViewById(R.id.img_barcode_generator_recieve_mode_dialog);
        img_barcode_recieve_mode_dialog = view.findViewById(R.id.img_barcode_recieve_mode_dialog);
    }

    @Override
    public void doSomeThing() {

    }


    public ImageView getImg_barcode_generator_recieve_mode_dialog() {
        return img_barcode_generator_recieve_mode_dialog;
    }

    public ImageView getImg_barcode_recieve_mode_dialog() {
        return img_barcode_recieve_mode_dialog;
    }

    public TextView getTxt_state_recieve_mode_dialog() {
        return txt_state_recieve_mode_dialog;
    }

    public TextView getTxt_waiting_recieve_mode_dialog() {
        return txt_waiting_recieve_mode_dialog;
    }

    public RoundedButton getBtn_cancel_recieve_mode_dialog() {
        return btn_cancel_recieve_mode_dialog;
    }

    @Override
    public void onClick(View view) {
        //TODO
    }
}
