package activities;

import Fragments.IntroOneFrg;
import Fragments.IntroThreeFrg;
import Fragments.IntroTwoFrg;
import FrgPresenter.IntroThreePresenter;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import lv.chi.photopicker.PhotoPickerFragment;
import servers.monitor.fastest.hoohoonew.App;
import servers.monitor.fastest.hoohoonew.R;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


import com.github.paolorotolo.appintro.AppIntro;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class IntroActivity extends AppIntro implements View.OnClickListener, PhotoPickerFragment.Callback{


    private Fragment currentFrg ;
    private static final String TAG = "IntroActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getInstance().setCurrentActivity(this);
        App.getInstance().setLocale(App.getInstance().getCurrentLang());

        if(App.getInstance().isIntroPassed())
            MainActivity.start();
        else  {

            addSlide(IntroOneFrg.newInstance());
           // addSlide(IntroTwoFrg.newInstance());
            addSlide(IntroThreeFrg.newInstance());

            setBarColor(ContextCompat.getColor(App.context ,R.color.colorPrimary ));
            setSeparatorColor(ContextCompat.getColor(App.context ,R.color.colorPrimaryDark ));

            showSkipButton(false);
            setBackButtonVisibilityWithDone(true);

            setVibrate(true);
            setVibrateIntensity(30);

        }


    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        // Do something when users tap on Skip button.
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {

        if(currentFragment instanceof IntroThreeFrg) {

           if(((IntroThreePresenter)  ((IntroThreeFrg) currentFragment).presenter).saveUserDetails()) {
               super.onDonePressed(currentFragment);
               App.getInstance().setIntroPassed();
               MainActivity.start();
               finish();
           }

        }

    }



    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {

        currentFrg = newFragment;
        super.onSlideChanged(oldFragment, newFragment);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.select_avatar_app_intro :
                if(currentFrg instanceof IntroThreeFrg)
                    ((IntroThreePresenter)  ((IntroThreeFrg) currentFrg).presenter)
                            .selectAvatarClicked();

                break;
        }

    }

    @Override
    public void onImagesPicked(@NotNull ArrayList<Uri> photos) {
        if(currentFrg instanceof IntroThreeFrg)
            ((IntroThreePresenter)  ((IntroThreeFrg) currentFrg).presenter).imagePicked(photos.get(0));
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
