package Adapter;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;

import Listeners.OnCustomClick;
import Listeners.OnSelectionApps;
import Listeners.OnThread;
import Models.AppModel;
import Utils.GlideManager;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;
import core.ApkMsg;
import servers.monitor.fastest.hoohoonew.App;
import servers.monitor.fastest.hoohoonew.R;

public class InstalledAppsAdapter extends RecyclerView.Adapter<InstalledAppsAdapter.ViewHolder> {

    private static final String TAG = "InstalledAppsAdapter";

    private Handler handler;
    private ArrayList<AppModel> data ;
    private OnSelectionApps onSelectionApps;

    public InstalledAppsAdapter(ArrayList<AppModel> data  , OnSelectionApps onSelectionApps) {
        handler = new Handler(Looper.getMainLooper());
        this.data = data;
        this.onSelectionApps = onSelectionApps;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).
                inflate(R.layout.item_installed_apps_list , parent , false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        AppModel app = data.get(position);

        setAppName(holder.txt_app_name_installed_app_item , app.getAppName());
        setImgApp(app.getApkPath() , holder);
        setCheckState(holder.ch_app_installed_app_item , app.isChecked() );
        setSizeApp(app.getApkPath() , holder);
        onClickContainer(holder , position);
        onClickCheckBox(holder , position);
    }

    private void setCheckState(AppCompatCheckBox ch_app_installed_app_item, boolean checked) {
        ch_app_installed_app_item.setChecked(checked);
    }


    private void onClickContainer(ViewHolder viewHolder , int position) {

        viewHolder.container_installed_app_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppModel app = data.get(position);
                boolean isChecked = app.isChecked();
                app.setChecked(!isChecked);
                viewHolder.ch_app_installed_app_item.setChecked(!isChecked);
                data.set(position , app);
                notifyItemChanged(position);

                if(app.isChecked())
                    onSelectionApps.onSelected(app);
                else
                    onSelectionApps.onDeselected(app);
            }
        });

    }

    private void onClickCheckBox(ViewHolder viewHolder , int position) {

        viewHolder.ch_app_installed_app_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppModel app = data.get(position);
                boolean isChecked = app.isChecked();
                app.setChecked(!isChecked);
                viewHolder.ch_app_installed_app_item.setChecked(!isChecked);
                data.set(position , app);
                notifyItemChanged(position);

                if(app.isChecked())
                    onSelectionApps.onSelected(app);
                else
                    onSelectionApps.onDeselected(app);
            }
        });

    }

    private void setImgApp(String apkPath , ViewHolder holder) {
        App.getInstance().initThread(new OnThread() {
            @Override
            public void onReady() {
                Drawable logo = ApkMsg.builder().getDrawable(apkPath);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        GlideManager.builder().loadDrawable(logo ,
                                holder.img_logo_installed_app_item);
                    }
                });
            }
        });
    }

    private void setSizeApp(String path , ViewHolder holder) {

        App.getInstance().initThread(new OnThread() {
            @Override
            public void onReady() {

                long mb = new File(path).length() / (1024 * 1024);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        holder.txt_app_volume_installed_app_item.
                                setText(App.getInstance().getResString(R.string.app_volume_mb , mb));
                    }
                });
            }
        });
    }

    private void setAppName(TextView txt , CharSequence appName) {
        txt.setText(appName);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{


        private LinearLayout container_installed_app_item;
        private ImageView img_logo_installed_app_item;
        private AppCompatCheckBox ch_app_installed_app_item;
        private TextView txt_app_name_installed_app_item , txt_app_volume_installed_app_item;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            container_installed_app_item = itemView.findViewById(R.id.container_installed_app_item);
            img_logo_installed_app_item = itemView.findViewById(R.id.img_logo_installed_app_item);
            ch_app_installed_app_item = itemView.findViewById(R.id.ch_app_installed_app_item);
            txt_app_name_installed_app_item = itemView.findViewById(R.id.txt_app_name_installed_app_item);
            txt_app_volume_installed_app_item = itemView.findViewById(R.id.txt_app_volume_installed_app_item);

        }
    }
}
