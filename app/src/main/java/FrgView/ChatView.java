package FrgView;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vanniktech.emoji.EmojiEditText;

import Custom.Toolbar;
import FrgPresenter.ChatPresenter;
import Listeners.OnCustomToolbarListener;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import servers.monitor.fastest.hoohoonew.App;
import core.ConnectionManager;
import servers.monitor.fastest.hoohoonew.R;

public class ChatView extends BaseFrgView {


    private LinearLayout container_frg_chat , container_no_chats_available ;
    private RecyclerView recycler_chat_frg;
    private EmojiEditText edt_txt_frg_chat ;
    private ImageView img_background_chat_frg , emoji_frg_chat  ,
            img_send_txt_chat , attachment_chat_files;

    @Override
    public int getLayout() {
        return R.layout.chat_frg;
    }

    @Override
    public void initViews() {
        container_frg_chat = view.findViewById(R.id.container_frg_chat);
        container_no_chats_available = view.findViewById(R.id.container_no_chats_available);
        edt_txt_frg_chat = view.findViewById(R.id.edt_txt_frg_chat);
        recycler_chat_frg = view.findViewById(R.id.recycler_chat_frg);
        img_background_chat_frg = view.findViewById(R.id.img_background_chat_frg);
        emoji_frg_chat = view.findViewById(R.id.emoji_frg_chat);
        img_send_txt_chat = view.findViewById(R.id.img_send_txt_chat);
        attachment_chat_files = view.findViewById(R.id.attachment_chat_files);



        Custom.Toolbar.builder().view(view,true , Toolbar.NO_IMG_TOOL , Toolbar.NO_IMG_TOOL).
                backColor(ContextCompat.getColor(App.context , R.color.colorAccent))
                .title(App.getInstance().getContext().getString(R.string.initializing))
                .image(R.drawable.logo)
                .build(new OnCustomToolbarListener() {
                    @Override
                    public void on_navBar_clicked() {
                        ((ChatPresenter) presenter).onNavBackPressed();
                    }

                    @Override
                    public void handle_toolbar_component_(TextView title, ImageView icon, final ImageView img_extra_tool_one_toolbar
                            , ImageView img_extra_tool_two_toolbar) {


                        icon.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ((ChatPresenter) presenter).showProfile();
                            }
                        });


                    }
                });
    }

    @Override
    public void doSomeThing() {

    }


    public LinearLayout getContainer_frg_chat() {
        return container_frg_chat;
    }

    public LinearLayout getContainer_no_chats_available() {
        return container_no_chats_available;
    }

    public RecyclerView getRecycler_chat_frg() {
        return recycler_chat_frg;
    }

    public ImageView getImg_background_chat_frg() {
        return img_background_chat_frg;
    }


    public EmojiEditText getEdt_txt_frg_chat() {
        return edt_txt_frg_chat;
    }


    public ImageView getEmoji_frg_chat() {
        return emoji_frg_chat;
    }


    public ImageView getImg_send_txt_chat() {
        return img_send_txt_chat;
    }

    public ImageView getAttachment_chat_files() {
        return attachment_chat_files;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.emoji_frg_chat :
                ((ChatPresenter) presenter).onEmojiClicked();
                break;

            case R.id.attachment_chat_files :
                ((ChatPresenter) presenter).onAttachFilesClicked();
                break;

            case R.id.img_send_txt_chat :
                ((ChatPresenter) presenter).onSendMsgClicked();
                break;
        }
    }

    public String getMessageText() {
        return getEdt_txt_frg_chat().getText().toString().trim();
    }
}
