package FrgView;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;
import servers.monitor.fastest.hoohoonew.R;

public class BlackListView extends BaseFrgView {

    private RecyclerView recycler_chat_list_frg ;

    @Override
    public int getLayout() {
        return R.layout.black_list_frg;
    }

    @Override
    public void initViews() {
        recycler_chat_list_frg = view.findViewById(R.id.recycler_chat_list_frg);
    }

    @Override
    public void doSomeThing() {

    }

    @Override
    public void onClick(View view) {

    }


    public RecyclerView getRecycler_chat_list_frg() {
        return recycler_chat_list_frg;
    }
}
