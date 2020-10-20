package Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import Custom.AnimationLib;
import Entities.ChatHeaderEntity;
import Fragments.ChatFrg;
import Models.ChatsFakeModel;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;
import servers.monitor.fastest.hoohoonew.App;
import Utils.GlideManager;
import servers.monitor.fastest.hoohoonew.R;

/**
 * Created by soheilmohammadi on 10/6/17.
 */

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.view_holder> {

    private static final String TAG = "ChatListAdapter";
    private ArrayList<ChatHeaderEntity> data ;

    public ChatListAdapter(ArrayList<ChatHeaderEntity> data ) {
        this.data = data ;
    }



    @Override
    public view_holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new view_holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_chat_list,
                parent , false));
    }

    @Override
    public void onBindViewHolder(final view_holder holder, final int position) {
        ChatHeaderEntity chatInfo = this.data.get(position);

        holder.container_row_chat_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                App.getInstance().showFrg(R.id.frame_activity_main , true ,ChatFrg.newInstance(chatInfo.uniqueID));
            }
        });

        setUserName(holder , chatInfo.getChatWithName() );
        setLastMag(holder , chatInfo.getLastMsg());
        checkDrawLineBetween(holder , position);
        setDateChatRow(holder  , chatInfo.getDate());
        setChatRowAvatar(holder , chatInfo.avatar , chatInfo.isMale == 1);
        setAnimOnView(holder.container_row_chat_list);
    }

    private void setUserName(view_holder holder , String  userName) {
        holder.txt_user_name_row_chat_list.setText(userName);
    }


    private void setLastMag(view_holder holder , String lastMsg) {
        if(lastMsg.length() > 15)
            lastMsg = lastMsg.substring(0 , 15) + " ..." ;

        holder.txt_last_msg_row_chat_list.setText(lastMsg);
    }


    private void setAnimOnView(final View view) {
        AnimationLib.builder().translateAnim(view ,  1200 , AnimationLib.State.SHOW);
    }

    public void setChatRowAvatar(view_holder holder , String avatar , boolean isMale) {
        if(avatar.equals(""))
            GlideManager.builder().loadRes(ChatsFakeModel.builder().getRandomAvatarName(isMale),
                    holder.img_avatar_row_chat_list);
        else
            GlideManager.builder().loadPathWithoutCache(avatar
                    , holder.img_avatar_row_chat_list);
    }

    private void setDateChatRow(view_holder holder , long date) {
        holder.txt_date_row_chat_list.setText(App.getInstance().calculateBetweenDate(date));
    }

    private void checkDrawLineBetween(view_holder holder ,int position) {
        if(data.size() - 1 == position )
            holder.view_line_between_row_chat_list.setVisibility(View.GONE);
        else
            holder.view_line_between_row_chat_list.setVisibility(View.VISIBLE);
    }


    @Override
    public int getItemCount() {
        return data.size() ;
    }


    public class view_holder extends  RecyclerView.ViewHolder {

        View view_line_between_row_chat_list ;
        LinearLayout container_row_chat_list ;
        CircleImageView img_avatar_row_chat_list ;
        TextView txt_user_name_row_chat_list ;
        TextView txt_date_row_chat_list ;
        TextView txt_last_msg_row_chat_list ;

        view_holder(View itemView) {
            super(itemView);
            view_line_between_row_chat_list =  itemView.findViewById(R.id.view_line_between_row_chat_list);
            container_row_chat_list =  itemView.findViewById(R.id.container_row_chat_list);
            img_avatar_row_chat_list = itemView.findViewById(R.id.img_avatar_row_chat_list);
            txt_user_name_row_chat_list = itemView.findViewById(R.id.txt_user_name_row_chat_list);
            txt_last_msg_row_chat_list = itemView.findViewById(R.id.txt_last_msg_row_chat_list);
            txt_date_row_chat_list = itemView.findViewById(R.id.txt_date_row_chat_list);
        }
    }

}
