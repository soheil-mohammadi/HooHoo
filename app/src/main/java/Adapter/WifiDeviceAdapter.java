package Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Random;

import Listeners.OnCustomClick;
import Models.ChatsFakeModel;
import Models.WifiDevice;
import Utils.GlideManager;
import androidx.recyclerview.widget.RecyclerView;
import servers.monitor.fastest.hoohoonew.App;
import servers.monitor.fastest.hoohoonew.R;

/**
 * Created by soheilmohammadi on 12/6/17.
 */

public class WifiDeviceAdapter extends RecyclerView.Adapter<WifiDeviceAdapter.viewHolder> {


    private static final String TAG = "WifiDeviceAdapter";
    private ArrayList<WifiDevice> devices ;
    private OnCustomClick onCustomClickItemList ;



    public  WifiDeviceAdapter( ArrayList<WifiDevice>  devices  , OnCustomClick onCustomClickItemList) {
        this.devices = devices ;
        this.onCustomClickItemList = onCustomClickItemList ;
    }


    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new viewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.devices_row, parent , false));
    }

    @Override
    public void onBindViewHolder(viewHolder holder, int position) {

        final WifiDevice device =  devices.get(position);
        setDeviceName(holder , device.Name);
        setDeviceAvatar(holder , device.Sex);
        onClickRow(holder.container_device_row ,device);

    }


    private void onClickRow(View containerRow , WifiDevice wifiDevice) {
        containerRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onCustomClickItemList.onClicked(wifiDevice);
            }
        });

    }


    private void setDeviceName(viewHolder holder ,  String text) {
        holder.txt_device_name.setText(text);
    }



    private void setDeviceAvatar(viewHolder holder , int sex) {
        boolean isMan = sex == 1;
        GlideManager.builder().loadRes(ChatsFakeModel.builder().getRandomAvatarName(isMan)
                ,holder.img_avatar_device_row );

    }


    @Override
    public int getItemCount() {
        return devices.size();
    }


    class viewHolder extends  RecyclerView.ViewHolder {

        LinearLayout container_device_row ;
        TextView txt_device_name    ;
        ImageView img_avatar_device_row ;

        public viewHolder(View itemView) {
            super(itemView);
            container_device_row = itemView.findViewById(R.id.container_device_row);
            txt_device_name = itemView.findViewById(R.id.txt_device_name);
            img_avatar_device_row = itemView.findViewById(R.id.img_avatar_device_row);
        }
    }
}
