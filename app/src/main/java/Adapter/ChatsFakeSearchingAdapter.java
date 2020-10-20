package Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import Models.ChatsFakeModel;
import Utils.GlideManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import servers.monitor.fastest.hoohoonew.App;
import servers.monitor.fastest.hoohoonew.R;

/**
 * Created by soheilmohammadi on 5/10/18.
 */

public class ChatsFakeSearchingAdapter extends RecyclerView.Adapter<ChatsFakeSearchingAdapter.ViewHolder> {

    private ArrayList<Integer> resBitmaps ;

    public ChatsFakeSearchingAdapter( ArrayList<Integer> resBitmaps) {
        this.resBitmaps = resBitmaps ;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_chat_fake ,
                parent , false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        int resourceId = resBitmaps.get(position);
        GlideManager.builder().loadRes(resourceId
                ,holder.img_row_chat_fake );
    }

    @Override
    public int getItemCount() {
        return resBitmaps.size();
    }

    class  ViewHolder extends  RecyclerView.ViewHolder {

        ImageView img_row_chat_fake ;

        public ViewHolder(View itemView) {
            super(itemView);
            img_row_chat_fake = itemView.findViewById(R.id.img_row_chat_fake);
        }
    }
}
