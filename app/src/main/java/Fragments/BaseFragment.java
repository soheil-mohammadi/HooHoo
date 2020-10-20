package Fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import FrgPresenter.BaseFrgPresenter;
import FrgView.BaseFrgView;
import Listeners.OnFrgDestroy;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import servers.monitor.fastest.hoohoonew.App;
import servers.monitor.fastest.hoohoonew.R;

/**
 * Created by soheilmohammadi on 9/22/18.
 */

public abstract class BaseFragment extends Fragment {

    public View view;
    public BaseFrgPresenter presenter;
    public BaseFrgView viewArc ;

    private boolean isStopped = false;

    public boolean isActionDone = false;

    private static final String TAG = "BaseFragment";


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        //super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter.onCreate(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        presenter = getPresenter();
        viewArc = getViewArc();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view  = LayoutInflater.from(container.getContext()).inflate(viewArc.getLayout() , container , false);
        viewArc.onCreate(view , presenter);
        presenter.onCreateView(this , view , viewArc);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();
        if(isStopped) {
            isStopped = false;
            if(isActionDone) {
                onReback();
                isActionDone = false;
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        isStopped = true;
    }


    public boolean isStopped() {
        return isStopped;
    }

    public void onBackPressed() {
        onBackAction();
    }

    public void onNavBackPressed() {
        onBackAction();
    }

    private void onBackAction() {
        OnFrgDestroy onFrgDestroy = destroy();
        Log.e(TAG, "onBackAction: " + onFrgDestroy );
        if(onFrgDestroy == null) {
            int stackCounts = App.getInstance().getCurrentActivity()
                    .getSupportFragmentManager().getBackStackEntryCount();
            destroyFrg();

            App.getInstance().getCurrentFrg(R.id.frame_activity_main);
            Log.e(TAG, "onBackAction: " + stackCounts );
            if(stackCounts == 1){
                App.getInstance().getCurrentActivity().onBackPressed();
            }

        }else {
            onFrgDestroy.onDestroy();
        }

    }

    abstract int idResFrame();
    abstract BaseFrgPresenter getPresenter();
    abstract BaseFrgView getViewArc();
    public abstract void onClick(View view);
    abstract void onReback();
    abstract OnFrgDestroy destroy();

    public void destroyFrg() {
        try {
            App.getInstance().getCurrentActivity().getSupportFragmentManager().popBackStack();
        }catch (IllegalStateException e) {

        }
    }

}
