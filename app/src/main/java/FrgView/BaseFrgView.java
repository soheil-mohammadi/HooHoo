package FrgView;

import android.view.View;

import FrgPresenter.BaseFrgPresenter;

/**
 * Created by soheilmohammadi on 9/22/18.
 */

public abstract class BaseFrgView {

    BaseFrgPresenter presenter;
    View view;

    public void onCreate(View view , BaseFrgPresenter presenter) {
        this.view = view;
        this.presenter = presenter;
        initViews();
        doSomeThing();
    }


    public abstract int getLayout();
    public  abstract void initViews();
    public abstract void doSomeThing();
    public abstract void onClick(View view);

}
