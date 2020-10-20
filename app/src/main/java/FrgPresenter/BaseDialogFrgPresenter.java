package FrgPresenter;
import android.view.View;

import Fragments.BaseDialogFragment;
import FrgView.BaseDialogFrgView;

/**
 * Created by soheilmohammadi on 9/22/18.
 */

public class BaseDialogFrgPresenter {


    public View view;
    public BaseDialogFrgView viewArc;
    public BaseDialogFragment fragment ;


    public void onCreate(BaseDialogFragment fragment , View view  ,
                         BaseDialogFrgView viewArc ){
        this.fragment = fragment;
        this.view = view;
        this.viewArc = viewArc;
    }

    public void onResume() {
        //TODO
    }

}
