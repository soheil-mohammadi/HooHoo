package FrgView;

import android.view.View;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;
import servers.monitor.fastest.hoohoonew.R;

public class ChatListView extends BaseFrgView {

    private RecyclerView recycler_chat_list_frg ;
    private LinearLayout container_no_chat_list_frg ;

    @Override
    public int getLayout() {
        return R.layout.chat_list_frg;
    }

    @Override
    public void initViews() {
        recycler_chat_list_frg = view.findViewById(R.id.recycler_chat_list_frg);
        container_no_chat_list_frg = view.findViewById(R.id.container_no_chat_list_frg);
    }

    @Override
    public void doSomeThing() {

    }

    @Override
    public void onClick(View view) {

    }


    public LinearLayout getContainer_no_chat_list_frg() {
        return container_no_chat_list_frg;
    }

    public RecyclerView getRecycler_chat_list_frg() {
        return recycler_chat_list_frg;
    }
}
