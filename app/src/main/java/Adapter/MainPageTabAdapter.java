package Adapter;


import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class MainPageTabAdapter extends FragmentStatePagerAdapter {


    private ArrayList<Fragment> frgList ;

    public MainPageTabAdapter(FragmentManager fm , ArrayList<Fragment> frgList) {
        super(fm);
        this.frgList =  frgList;
    }

    @Override
    public Fragment getItem(int i) {
        return frgList.get(i);
    }



    @Override
    public int getCount() {
        return frgList.size();
    }
}
