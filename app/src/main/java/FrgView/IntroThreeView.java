package FrgView;

import android.view.View;
import android.widget.EditText;

import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;
import servers.monitor.fastest.hoohoonew.R;

public class IntroThreeView extends BaseFrgView {



    private CircleImageView img_avatar_app_intro  ;
    private EditText edt_user_display_name_app_intro , edt_user_bio_app_intro ;
    private SwitchCompat swc_sex_user ;

    @Override
    public int getLayout() {
        return R.layout.intro_three_frg;
    }

    @Override
    public void initViews() {
        img_avatar_app_intro = view.findViewById(R.id.img_avatar_app_intro);
        edt_user_display_name_app_intro = view.findViewById(R.id.edt_user_display_name_app_intro);
        edt_user_bio_app_intro = view.findViewById(R.id.edt_user_bio_app_intro);
        swc_sex_user = view.findViewById(R.id.swc_sex_user);
    }

    @Override
    public void doSomeThing() {

    }

    @Override
    public void onClick(View view) {

    }


    public CircleImageView getImg_avatar_app_intro() {
        return img_avatar_app_intro;
    }

    public EditText getEdt_user_display_name_app_intro() {
        return edt_user_display_name_app_intro;
    }

    public EditText getEdt_user_bio_app_intro() {
        return edt_user_bio_app_intro;
    }

    public SwitchCompat getSwc_sex_user() {
        return swc_sex_user;
    }
}
