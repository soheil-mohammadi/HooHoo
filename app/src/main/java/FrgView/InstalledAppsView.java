package FrgView;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import Custom.Toolbar;
import Fragments.BaseFragment;
import FrgPresenter.ChatPresenter;
import FrgPresenter.InstalledAppsPresenter;
import Listeners.OnCustomToolbarListener;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import servers.monitor.fastest.hoohoonew.App;
import servers.monitor.fastest.hoohoonew.R;

public class InstalledAppsView extends BaseFrgView {

    private LinearLayout container_getting_installed_apps_frg , container_selected_installed_apps_frg ;
    private RecyclerView recycler_installed_apps_frg ;
    private TextView txt_selected_count_installed_apps_frg ;


    @Override
    public int getLayout() {
        return R.layout.installed_apps_frg;
    }

    @Override
    public void initViews() {
        container_getting_installed_apps_frg = view.findViewById(R.id.container_getting_installed_apps_frg);
        container_selected_installed_apps_frg = view.findViewById(R.id.container_selected_installed_apps_frg);
        recycler_installed_apps_frg = view.findViewById(R.id.recycler_installed_apps_frg);
        txt_selected_count_installed_apps_frg = view.findViewById(R.id.txt_selected_count_installed_apps_frg);

        Custom.Toolbar.builder().view(view,true , Toolbar.NO_IMG_TOOL , Toolbar.NO_IMG_TOOL).
                backColor(ContextCompat.getColor(App.context , R.color.colorAccent))
                .title(App.getInstance().getContext().getString(R.string.apps))
                .image(Toolbar.NO_IMG_TOOL)
                .build(new OnCustomToolbarListener() {
                    @Override
                    public void on_navBar_clicked() {
                        ((BaseFragment) App.getInstance()
                                .getCurrentFrg(R.id.frame_activity_main)).onNavBackPressed();
                    }

                    @Override
                    public void handle_toolbar_component_(TextView title, ImageView icon, final ImageView img_extra_tool_one_toolbar
                            , ImageView img_extra_tool_two_toolbar) {
                        //TODO
                    }
                });
    }

    @Override
    public void doSomeThing() {

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.btn_select_installed_apps_frg :
                ((InstalledAppsPresenter)presenter).selectAppsBtn();
                break;
        }
    }


    public LinearLayout getContainer_getting_installed_apps_frg() {
        return container_getting_installed_apps_frg;
    }


    public LinearLayout getContainer_selected_installed_apps_frg() {
        return container_selected_installed_apps_frg;
    }

    public TextView getTxt_selected_count_installed_apps_frg() {
        return txt_selected_count_installed_apps_frg;
    }

    public RecyclerView getRecycler_installed_apps_frg() {
        return recycler_installed_apps_frg;
    }
}
