package FrgView;

import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import Custom.Toolbar;
import FrgPresenter.MainPresenter;
import Listeners.OnCustomClick;
import Listeners.OnCustomToolbarListener;
import Listeners.OnSeachCahngedToolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import servers.monitor.fastest.hoohoonew.App;
import servers.monitor.fastest.hoohoonew.R;

public class MainView extends BaseFrgView {

    private static final String TAG = "MainView";

    private DrawerLayout drawer_layout_main ;

    private TabLayout tab_layout_main;
    private ViewPager view_pager_main;


    private ImageView img_back_nav_main , navigation_view_user_avatar ;

    private TextView navigation_view_user_display_name , txt_number_chats ;

    private FloatingActionButton flat_btn_new_chat ;



    @Override
    public int getLayout() {
        return R.layout.frg_main;
    }

    @Override
    public void initViews() {

        drawer_layout_main = view.findViewById(R.id.drawer_layout_main);
        tab_layout_main = view.findViewById(R.id.tab_layout_main);
        view_pager_main = view.findViewById(R.id.view_pager_main);
        img_back_nav_main = view.findViewById(R.id.img_back_nav_main);
        navigation_view_user_avatar = view.findViewById(R.id.navigation_view_user_avatar);
        navigation_view_user_display_name = view.findViewById(R.id.navigation_view_user_display_name);
        flat_btn_new_chat = view.findViewById(R.id.flat_btn_new_chat);
        txt_number_chats = view.findViewById(R.id.txt_number_chats);

        Custom.Toolbar.builder().view(view,false , Toolbar.NO_IMG_TOOL , Toolbar.NO_IMG_TOOL).
                backColor(ContextCompat.getColor(App.context , R.color.colorAccent))
                .title(App.getInstance().getContext().getString(R.string.chats))
                .image(R.drawable.ic_menu)
                .build(new OnCustomToolbarListener() {
                    @Override
                    public void on_navBar_clicked() {
                        //App.getInstance().requestForShutDown();
                    }

                    @Override
                    public void handle_toolbar_component_(TextView title, ImageView icon, final ImageView img_extra_tool_one_toolbar
                            , ImageView img_extra_tool_two_toolbar) {


                        icon.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                openDrawer();
                            }
                        });


                    }
                });
    }




    @Override
    public void doSomeThing() {

    }

    public void onClick(View v) {
        switch (v.getId()) {

            case  R.id.flat_btn_new_chat :
                ((MainPresenter)presenter).startDiscovering();
                break;

            case  R.id.flat_btn_recieve_mode :
                ((MainPresenter)presenter).startDiscoverable();
                break;

            case  R.id.navigation_view_communication_developer :
                App.getInstance().sendGmail();
                break;


            case  R.id.navigation_view_share :
                App.getInstance().shareApk();
                break;

        }
    }


    public TextView getTxt_number_chats() {
        return txt_number_chats;
    }

    public DrawerLayout getDrawer_layout_main() {
        return drawer_layout_main;
    }

    public TextView getNavigation_view_user_display_name() {
        return navigation_view_user_display_name;
    }

    public FloatingActionButton getFlat_btn_new_chat() {
        return flat_btn_new_chat;
    }

    public ImageView getImg_back_nav_main() {
        return img_back_nav_main;
    }

    public ImageView getNavigation_view_user_avatar() {
        return navigation_view_user_avatar;
    }

    public TabLayout getTab_layout_frg_main() {
        return tab_layout_main;
    }

    public ViewPager getView_pager_frg_main() {
        return view_pager_main;
    }

    private void openDrawer() {
        drawer_layout_main.openDrawer(GravityCompat.START , true);
    }

    public void closeDrawer() {
        drawer_layout_main.closeDrawer(GravityCompat.END);
    }
}
