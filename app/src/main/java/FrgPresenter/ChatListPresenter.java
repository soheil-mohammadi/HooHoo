package FrgPresenter;

import android.view.View;

import java.util.ArrayList;

import Adapter.ChatListAdapter;
import Entities.ChatHeaderEntity;
import Fragments.BaseFragment;
import FrgView.BaseFrgView;
import FrgView.ChatListView;
import androidx.recyclerview.widget.LinearLayoutManager;
import servers.monitor.fastest.hoohoonew.App;
import DateController.ChatHeaderManager;

public class ChatListPresenter extends BaseFrgPresenter {

    @Override
    public void onCreate(BaseFragment fragment) {
        super.onCreate(fragment);
    }


    @Override
    public void onCreateView(BaseFragment fragment, View view, BaseFrgView viewArc) {
        super.onCreateView(fragment, view, viewArc);
        checkAvailableChats();
    }

    private void checkAvailableChats() {
       ArrayList<ChatHeaderEntity> chats = ChatHeaderManager.builder().getAllChats();

       if(chats != null && chats.size() > 0) {
           ((ChatListView) viewArc).getContainer_no_chat_list_frg().setVisibility(View.GONE);
           ((ChatListView) viewArc).getRecycler_chat_list_frg().setVisibility(View.VISIBLE);
           ((ChatListView) viewArc).getRecycler_chat_list_frg().setLayoutManager(new LinearLayoutManager(App.context));
           ((ChatListView) viewArc).getRecycler_chat_list_frg().setAdapter(new ChatListAdapter(chats));

       } else {
           ((ChatListView) viewArc).getContainer_no_chat_list_frg().setVisibility(View.VISIBLE);
           ((ChatListView) viewArc).getRecycler_chat_list_frg().setVisibility(View.GONE);
       }
    }


}
