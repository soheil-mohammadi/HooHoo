package FrgView;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import life.sabujak.roundedbutton.RoundedButton;
import servers.monitor.fastest.hoohoonew.R;

public class ChatProfileDialogView extends BaseDialogFrgView {

    private ImageView img_avatar_chat_profile_dialog , img_avatar_back_chat_profile_dialog ;
    private TextView txt_name_chat_profile_dialog  , txt_bio_chat_profile_dialog;

    @Override
    public int getLayout() {
        return R.layout.chat_profile_dialog;
    }

    @Override
    public void initViews() {
        img_avatar_back_chat_profile_dialog = view.findViewById(R.id.img_avatar_back_chat_profile_dialog);
        img_avatar_chat_profile_dialog = view.findViewById(R.id.img_avatar_chat_profile_dialog);
        txt_name_chat_profile_dialog = view.findViewById(R.id.txt_name_chat_profile_dialog);
        txt_bio_chat_profile_dialog = view.findViewById(R.id.txt_bio_chat_profile_dialog);
    }

    @Override
    public void doSomeThing() {

    }


    public ImageView getImg_avatar_chat_profile_dialog() {
        return img_avatar_chat_profile_dialog;
    }

    public TextView getTxt_name_chat_profile_dialog() {
        return txt_name_chat_profile_dialog;
    }

    public TextView getTxt_bio_chat_profile_dialog() {
        return txt_bio_chat_profile_dialog;
    }

    public ImageView getImg_avatar_back_chat_profile_dialog() {
        return img_avatar_back_chat_profile_dialog;
    }

    @Override
    public void onClick(View view) {
        //TODO
    }
}
