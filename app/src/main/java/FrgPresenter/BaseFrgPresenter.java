package FrgPresenter;
import android.view.View;

import Fragments.BaseFragment;
import FrgView.BaseFrgView;

/**
 * Created by soheilmohammadi on 9/22/18.
 */

public class BaseFrgPresenter {


    public View view;
    public BaseFrgView viewArc;
    public BaseFragment fragment ;


    public void onCreate(BaseFragment fragment){
        this.fragment = fragment;
    }

    public void onCreateView(BaseFragment fragment , View view  , BaseFrgView viewArc ){
        this.fragment = fragment;
        this.view = view;
        this.viewArc = viewArc;
    }

    public void onResume() {
        //TODO
    }

}
