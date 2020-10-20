package FrgView;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;
import servers.monitor.fastest.hoohoonew.R;

public class IntroTwoView extends BaseFrgView {


    private RecyclerView recycler_language_available_list_intro ;

    @Override
    public int getLayout() {
        return R.layout.intro_two_frg;
    }

    @Override
    public void initViews() {
        recycler_language_available_list_intro = view.findViewById(R.id.recycler_language_available_list_intro);
    }

    @Override
    public void doSomeThing() {

    }

    @Override
    public void onClick(View view) {

    }


    public RecyclerView getRecycler_language_available_list_intro() {
        return recycler_language_available_list_intro;
    }
}
