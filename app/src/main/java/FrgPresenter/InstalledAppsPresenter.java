package FrgPresenter;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;

import java.util.ArrayList;
import java.util.List;

import Adapter.InstalledAppsAdapter;
import Fragments.BaseFragment;
import FrgView.BaseFrgView;
import FrgView.InstalledAppsView;
import Listeners.OnSelectionApps;
import Listeners.OnThread;
import Models.AppModel;
import ViewModels.SelectedAppsViewModel;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import servers.monitor.fastest.hoohoonew.App;
import servers.monitor.fastest.hoohoonew.R;

public class InstalledAppsPresenter extends BaseFrgPresenter  implements OnSelectionApps {

    private static final String TAG = "InstalledAppsPresenter";
    private Handler handler;

    private  SelectedAppsViewModel selectedAppsViewModel ;
    private ArrayList<AppModel> selectedApps = new ArrayList<>();
    private ArrayList<AppModel> allApps = new ArrayList<>();


    @Override
    public void onCreate(BaseFragment fragment) {
        super.onCreate(fragment);
        handler = new Handler(Looper.getMainLooper());
        selectedAppsViewModel = ViewModelProviders.of(App.getInstance().getCurrentActivity())
                .get(SelectedAppsViewModel.class);
    }

    @Override
    public void onCreateView(BaseFragment fragment, View view, BaseFrgView viewArc) {
        super.onCreateView(fragment, view, viewArc);
        initAppsList();
    }


    private void initAppsList() {

        App.getInstance().initThread(new OnThread() {
            @Override
            public void onReady() {

                List<ApplicationInfo> packages = App.context.getPackageManager().
                        getInstalledApplications(PackageManager.GET_META_DATA);

                for (ApplicationInfo packageInfo : packages) {


                    if(App.context.getPackageManager().getLaunchIntentForPackage(packageInfo.packageName) == null ) {
                        continue;
                    }


                    if(packageInfo.packageName.equals(App.context.getPackageName())) {
                        continue;
                    }


                    try {
                        ApplicationInfo app = App.context.getPackageManager().getApplicationInfo(
                                packageInfo.packageName, 0);

                        Log.e(TAG, "details: " +  app.sourceDir);
                        allApps.add( new AppModel(null ,
                                String.valueOf(App.context.getPackageManager().getApplicationLabel(app)) ,
                                app.sourceDir , false));

                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }

                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        ((InstalledAppsView) viewArc).getContainer_getting_installed_apps_frg().setVisibility(View.GONE);
                        ((InstalledAppsView) viewArc).getRecycler_installed_apps_frg().setVisibility(View.VISIBLE);
                        ((InstalledAppsView) viewArc).getRecycler_installed_apps_frg()
                                .setLayoutManager(new GridLayoutManager(App.context , 4 ,
                                        RecyclerView.VERTICAL , false));
                        ((InstalledAppsView) viewArc).getRecycler_installed_apps_frg()
                                .setAdapter(new InstalledAppsAdapter(allApps ,
                                        InstalledAppsPresenter.this));
                    }

                });

            }
        });




    }


    @Override
    public void onResume() {
        super.onResume();
    }

    public void selectAppsBtn() {
        selectedAppsViewModel.setSelectedApps(selectedApps);
        fragment.destroyFrg();
    }

    @Override
    public void onSelected(AppModel appModel) {
        selectedApps.add(appModel);
        checkState();
    }

    @Override
    public void onDeselected(AppModel appModel) {
        selectedApps.remove(appModel);
        checkState();
    }


    private void checkState() {
        if(selectedApps.size() > 0) {
            ((InstalledAppsView)viewArc).getContainer_selected_installed_apps_frg()
                    .setVisibility(View.VISIBLE);
            ((InstalledAppsView)viewArc).getTxt_selected_count_installed_apps_frg()
                    .setText(App.getInstance().getResString(R.string.selected_apps_count , selectedApps.size()));
        }else {
            ((InstalledAppsView)viewArc).getContainer_selected_installed_apps_frg()
                    .setVisibility(View.GONE);
        }
    }
}
