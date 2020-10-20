package FrgPresenter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import Adapter.MainPageTabAdapter;
import Custom.AnimationLib;
import Custom.Toolbar;
import DateController.ChatHeaderManager;
import Dialogs.WarningDialog;
import Entities.UserEntity;
import Fragments.BaseDialogFragment;
import Fragments.BaseFragment;
import Fragments.BlackListFrg;
import Fragments.ChatListFrg;
import Fragments.DiscoverDialogFrg;
import Fragments.RecieverModeDialogFrg;
import FrgView.BaseFrgView;
import FrgView.MainView;
import Listeners.OnCustomClick;
import Models.ChatsFakeModel;
import Utils.GlideManager;
import Utils.PermissionManager;
import Utils.SharedManager;
import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import core.LocationManager;
import servers.monitor.fastest.hoohoonew.App;
import core.ConnectionManager;
import servers.monitor.fastest.hoohoonew.R;

public class MainPresenter extends BaseFrgPresenter implements Thread.UncaughtExceptionHandler {

    private static final String TAG = "MainPresenter";
    private Context mContext ;

    private UserEntity userEntity ;


    @Override
    public void onCreate(BaseFragment fragment) {
        super.onCreate(fragment);
    }

    @Override
    public void onCreateView(BaseFragment fragment, View view, BaseFrgView viewArc) {
        super.onCreateView(fragment, view, viewArc);
        if (android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.M)
            Thread.setDefaultUncaughtExceptionHandler(this);

        userEntity = App.getInstance().getAppDatabase().userDao().fetchUser();
        handleDrawer();
        setupNavigation();
        setupTabs();
    }

    private void handleDrawer() {
        ((MainView)viewArc).getDrawer_layout_main().addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                AnimationLib.builder().circular(((MainView)viewArc).getFlat_btn_new_chat() , 400 ,
                        AnimationLib.State.HIDE);
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                AnimationLib.builder().circular(((MainView)viewArc).getFlat_btn_new_chat() , 400 ,
                        AnimationLib.State.SHOW);
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }

    private void setupNavigation() {

        ((MainView)viewArc).getTxt_number_chats().setText(ChatHeaderManager.builder().getAllChats().size() + "");

        String userAvatar = userEntity.getUserAvatar();

        if(userAvatar.equals("")) {

            String res = ChatsFakeModel.builder().getRandomAvatarName(
                    userEntity.getUserSex() == 1);
            GlideManager.builder().loadRes(res,
                    ((MainView)viewArc)
                            .getNavigation_view_user_avatar());
            GlideManager.builder().loadResBlur(res ,((MainView)viewArc)
                    .getImg_back_nav_main() );
        } else {

            GlideManager.builder().loadPath(userAvatar
                    , ((MainView)viewArc).getNavigation_view_user_avatar());
            GlideManager.builder().loadPathBlur(userAvatar ,((MainView)viewArc)
                    .getImg_back_nav_main() );
        }


        ((MainView)viewArc).getNavigation_view_user_display_name().setText(userEntity.getUserName());

    }


    private void setupTabs() {
        ArrayList<Fragment> frgs = new ArrayList<>();
        frgs.add(ChatListFrg.newInstance());
        frgs.add(BlackListFrg.newInstance());
        ((MainView)viewArc).getView_pager_frg_main().setAdapter(new MainPageTabAdapter(
                fragment.getChildFragmentManager() , frgs));
        ((MainView)viewArc).getView_pager_frg_main().addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                switch (i) {
                    case 0 :
                        Toolbar.builder().title(App.getInstance().getCurrentActivity().getString(R.string.chats));
                        break;

                    case 1 :
                        Toolbar.builder().title(App.getInstance().getCurrentActivity().getString(R.string.black_list));
                        break;

                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        ((MainView) viewArc).getTab_layout_frg_main().setupWithViewPager(((MainView) viewArc).getView_pager_frg_main());
        setupTabIcons();
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    private void setupTabIcons() {
        ((MainView) viewArc).getTab_layout_frg_main().getTabAt(0).setIcon(R.drawable.ic_chat);
        ((MainView) viewArc).getTab_layout_frg_main().getTabAt(1).setIcon(R.drawable.ic_black);
    }

    public void onAttach(Context context) {
        this.mContext = context;
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    public void onStop() {
    }

    public void onDestroy() {

        //  Log.e(TAG, "onDestroy: !!" );
    }


    public void startDiscovering() {

//        if(!SharedManager.builder().get("dontRemindForDisablingRecieverMode" , false)) {
//            WarningDialog.builder().show(App.getInstance().getResString(R.string.reciever_mode),
//                    App.getInstance().getResString(R.string.not_in_reciever_mode), R.raw.warning, new OnCustomClick() {
//                        @Override
//                        public void onClicked(Object object) {
//                            boolean isDontShowAgain = (boolean) object;
//                            SharedManager.builder().set("dontRemindForDisablingRecieverMode" , isDontShowAgain);
//                            forwardToDiscoverDialog();
//                        }
//                    }, new OnCustomClick() {
//                        @Override
//                        public void onClicked(Object object) {
//                            //Dont do anythings :)
//                        }
//                    }, true);
//
//        } else {
//
//            forwardToDiscoverDialog();
//        }


        forwardToDiscoverDialog();


    }



    private void forwardToFrgByGrantLocation(BaseDialogFragment dialogFragment) {
        PermissionManager.builder().grant(PermissionManager.LOCATION, new PermissionManager.OnPermissionResult() {
            @Override
            public void onAccepted() {
                boolean isOn = LocationManager.builder().isEnabled();

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(isOn)
                            App.getInstance().showDialogFrg(dialogFragment);
                    }
                } , 200);

            }

            @Override
            public void onDeny() {

            }
        });
    }


    public void startDiscoverable() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M  && Build.VERSION.SDK_INT <= Build.VERSION_CODES.N) {

            if (!Settings.System.canWrite(App.context)) {

                WarningDialog.builder().show(App.getInstance().getResString(R.string.modify_system_settings),
                        App.getInstance().getResString(R.string.modify_system_settings_desc), R.raw.settings, new OnCustomClick() {
                            @Override
                            public void onClicked(Object object) {
                                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS,
                                        Uri.parse("package:" + App.context.getPackageName()));
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                App.context.startActivity(intent);
                            }
                        }, null, false);


            } else {
                App.getInstance().showDialogFrg(RecieverModeDialogFrg.newInstance());
            }


        }else {
            forwardToFrgByGrantLocation(RecieverModeDialogFrg.newInstance());
        }


    }


    private void forwardToDiscoverDialog() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            forwardToFrgByGrantLocation(DiscoverDialogFrg.newInstance());
        }else {
            App.getInstance().showDialogFrg(DiscoverDialogFrg.newInstance());
        }
    }


    @Override
    public void uncaughtException(@NonNull Thread thread, @NonNull Throwable throwable) {
        Log.e(TAG, "uncaughtException: " + throwable.getMessage() );
    }
}
