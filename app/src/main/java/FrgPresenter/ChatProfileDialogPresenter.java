package FrgPresenter;

import android.view.View;

import com.bumptech.glide.Glide;

import java.io.File;

import Entities.UserEntity;
import Fragments.BaseDialogFragment;
import Fragments.ChatProfileDialogFrg;
import FrgView.BaseDialogFrgView;
import FrgView.ChatProfileDialogView;
import FrgView.MainView;
import Models.ChatsFakeModel;
import Utils.GlideManager;
import core.Patterns;
import servers.monitor.fastest.hoohoonew.App;
import servers.monitor.fastest.hoohoonew.R;

public class ChatProfileDialogPresenter extends BaseDialogFrgPresenter   {

    private  final String TAG = "DiscoverDialogPresenter";
    private UserEntity userEntity ;

    @Override
    public void onCreate(BaseDialogFragment fragment, View view, BaseDialogFrgView viewArc) {
        super.onCreate(fragment, view, viewArc);
        userEntity = (UserEntity) fragment.getArguments().getSerializable(ChatProfileDialogFrg.USER_ARG);
        setUserInfo();
    }

    private void setUserInfo() {

        setUserName();
        setUserBio();
        setUserAvatar();

    }

    private void setUserName() {
        ((ChatProfileDialogView)viewArc).getTxt_name_chat_profile_dialog()
                .setText(userEntity.getUserName());
    }


    private void setUserBio() {
        if(userEntity.getUserBio().equals(Patterns.USER_BIO_DEFAULT))
            ((ChatProfileDialogView)viewArc).getTxt_bio_chat_profile_dialog()
                    .setText(App.getInstance().getResString(R.string.empty_bio));
        else
            ((ChatProfileDialogView)viewArc).getTxt_bio_chat_profile_dialog()
                    .setText(userEntity.getUserBio());
    }


    private void setUserAvatar() {
        if(userEntity.getUserAvatar().equals("") ||
                !new File(userEntity.getUserAvatar()).exists()) {

            String res = ChatsFakeModel.builder().getRandomAvatarName(
                    userEntity.getUserSex() == 1);

            GlideManager.builder().loadRes(res,
                    ((ChatProfileDialogView)viewArc).getImg_avatar_chat_profile_dialog());

            GlideManager.builder().loadResBlur(res ,
                    ((ChatProfileDialogView)viewArc).getImg_avatar_back_chat_profile_dialog() );

        } else {

            GlideManager.builder().loadPathWithoutCache(userEntity.getUserAvatar()
                    , ((ChatProfileDialogView)viewArc).getImg_avatar_chat_profile_dialog());

            GlideManager.builder().loadPathCornerBlurWithoutCache(userEntity.getUserAvatar() , 10 ,
                    ((ChatProfileDialogView)viewArc).getImg_avatar_back_chat_profile_dialog() );
        }
    }


    @Override
    public void onResume() {
        super.onResume();
    }


    public void onDismissed() {
        //TODO
    }


}
