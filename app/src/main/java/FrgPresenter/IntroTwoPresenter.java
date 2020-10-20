package FrgPresenter;

import java.util.ArrayList;

import Adapter.LanguageAppAdapter;
import Fragments.BaseFragment;
import FrgView.IntroTwoView;
import Models.LanguageAppModel;
import androidx.recyclerview.widget.LinearLayoutManager;
import servers.monitor.fastest.hoohoonew.App;
import servers.monitor.fastest.hoohoonew.R;

public class IntroTwoPresenter extends BaseFrgPresenter {

    @Override
    public void onCreate(BaseFragment fragment) {
        super.onCreate(fragment);
    }


    @Override
    public void onResume() {
        super.onResume();
        setLangList();
    }

    private void setLangList() {
        ArrayList<LanguageAppModel> langs = new ArrayList<>();
        langs.add(new LanguageAppModel("en" , "English" , R.drawable.ic_united_states));
        langs.add(new LanguageAppModel("fa" , "Persian" , R.drawable.ic_iran));

        ((IntroTwoView)viewArc).getRecycler_language_available_list_intro().
                setLayoutManager(new LinearLayoutManager(App.context));
        ((IntroTwoView)viewArc).getRecycler_language_available_list_intro().
                setAdapter(new LanguageAppAdapter(langs));
    }
}
