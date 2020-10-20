package FrgPresenter;

import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;

import Dao.UserDao;
import Entities.UserEntity;
import Fragments.BaseFragment;
import FrgView.IntroThreeView;
import core.Patterns;
import lv.chi.photopicker.PhotoPickerFragment;
import servers.monitor.fastest.hoohoonew.App;
import servers.monitor.fastest.hoohoonew.R;

public class IntroThreePresenter extends BaseFrgPresenter {

    private static final String TAG = "IntroThreePresenter";
    private String avatar =  "";

    @Override
    public void onCreate(BaseFragment fragment) {
        super.onCreate(fragment);
    }


    @Override
    public void onResume() {
        super.onResume();
    }


    public void selectAvatarClicked() {
        PhotoPickerFragment.newInstance(false ,true,1 ,
                R.style.ChiliPhotoPicker_Dark).show(fragment.getFragmentManager(), "HooHooImagePicker");
    }


    public void imagePicked(Uri photo) {

        if(Patterns.builder().getFileLength(photo) > (3145728)) {
            App.getInstance().showCustomToast(App.getInstance()
                    .getResString(R.string.avatar_cant_big_volume) , Toast.LENGTH_SHORT
                    , R.color.colorPrimaryRed);
            return;
        }

        avatar = photo.toString();
        Glide.with(App.context).load(photo).into(((IntroThreeView)viewArc).getImg_avatar_app_intro());

    }

    public boolean saveUserDetails() {
        UserDao userDao = App.getInstance().getAppDatabase().userDao();
        String userName = ((IntroThreeView)viewArc).getEdt_user_display_name_app_intro().getText().toString().trim();
        String userBio = ((IntroThreeView)viewArc).getEdt_user_bio_app_intro().getText().toString().trim();
        boolean isUserMale = ((IntroThreeView)viewArc).getSwc_sex_user().isChecked();

        if(userName.equals("")) {
            App.getInstance().showSnackbar(view , App.getInstance().getResString(R.string.choose_user_name)
                    , Toast.LENGTH_SHORT , R.color.colorPrimaryRed);
            return false;

        } else if(avatar.equals("")) {
            App.getInstance().showSnackbar(view , App.getInstance().getResString(R.string.choose_avatar_please)
                    , Toast.LENGTH_SHORT , R.color.colorPrimaryRed);
            return false;

        }else {

            if(userBio.equals(""))
                userBio = Patterns.USER_BIO_DEFAULT;

            userDao.insertUser(new UserEntity(userName , userBio , avatar ,
                    App.getInstance().getUniqeId() ,isUserMale ? 1 : 0 ));
            return true;
        }

    }

}
