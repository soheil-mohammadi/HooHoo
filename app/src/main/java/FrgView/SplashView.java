package FrgView;

import android.view.View;
import android.widget.TextView;

import Custom.ProgressBarIndicator;
import servers.monitor.fastest.hoohoonew.R;

public class SplashView extends BaseFrgView {

    private ProgressBarIndicator progress_bar_splash_screen;
    private TextView txt_hoohoo_version_name_splash ;


    @Override
    public int getLayout() {
        return R.layout.frg_splash;
    }

    @Override
    public void initViews() {
        progress_bar_splash_screen = view.findViewById(R.id.progress_bar_splash_screen);
        txt_hoohoo_version_name_splash = view.findViewById(R.id.txt_hoohoo_version_name_splash);
    }

    @Override
    public void doSomeThing() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

        }
    }


    public ProgressBarIndicator getProgress_bar_splash_screen() {
        return progress_bar_splash_screen;
    }


    public TextView getTxt_hoohoo_version_name_splash() {
        return txt_hoohoo_version_name_splash;
    }
}
