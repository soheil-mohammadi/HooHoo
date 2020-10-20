package FrgPresenter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import Fragments.BaseFragment;
import Fragments.MainFrg;
import FrgView.BaseFrgView;
import FrgView.SplashView;
import Listeners.OnThread;
import core.ConnectionManager;
import core.types.HotspotManager;
import servers.monitor.fastest.hoohoonew.App;
import servers.monitor.fastest.hoohoonew.R;

import static android.app.Activity.RESULT_OK;

public class SplashPresenter extends BaseFrgPresenter {

    private static final String TAG = "SplashPresenter";
    private Context mContext ;



    @Override
    public void onCreateView(BaseFragment fragment, View view, BaseFrgView viewArc) {
        super.onCreateView(fragment, view, viewArc);
        ConnectionManager.builder().destroyWifi();
        HotspotManager.builder().startBlt();
        initPrg();
        setVersionName();
    }

    private void setVersionName() {
        ((SplashView)viewArc).
                getTxt_hoohoo_version_name_splash().setText(App.getInstance()
                .getResString(R.string.hoohoo_version_name , App.getInstance().getVersionName()));
    }

    private void initPrg() {

        App.getInstance().initThread(new OnThread() {
            @Override
            public void onReady() {
                for (int i = 0; i <=100 ; i++) {
                    try {

                        if(i == 100) {
                            fragment.isActionDone = true;
                            RedirectToMain();
                            continue;
                        }

                        final int finalI = i;
                        if(fragment.getActivity() != null) {
                            fragment.getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ((SplashView) viewArc).getProgress_bar_splash_screen().setProgress(finalI);
                                }
                            });
                        }


                        Thread.sleep(30);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void RedirectToMain() {
        if(!fragment.isStopped()) {
            App.getInstance().showFrg(R.id.frame_activity_main , true , MainFrg.newInstance());
        }
    }


    public void onAttach(Context context) {
        this.mContext = context;
    }


    @Override
    public void onResume() {
        super.onResume();

    }


    public void onReback() {
        RedirectToMain();
    }

    public  void onStop() {
        //TODO
    }


    public void onBackPressed() {
        //TODO
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            //TODO
        }
    }
}
