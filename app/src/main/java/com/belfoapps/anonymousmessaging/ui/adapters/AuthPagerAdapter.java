package com.belfoapps.anonymousmessaging.ui.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.belfoapps.anonymousmessaging.R;
import com.belfoapps.anonymousmessaging.utils.Config;

import java.util.ArrayList;

public class AuthPagerAdapter extends FragmentPagerAdapter {
    /*************************************** Declarations *****************************************/
    private ArrayList<Fragment> fragments;
    private Context context;

    /*************************************** Constructor ******************************************/
    public AuthPagerAdapter(@NonNull FragmentManager fm, ArrayList<Fragment> fragments, Context context) {
        super(fm);
        this.fragments = fragments;
        this.context = context;
    }

    /*************************************** Methods **********************************************/
    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    public View getTabView(int position) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(context).inflate(R.layout.custom_tab, null);
        TextView tv = view.findViewById(R.id.tab_title);
        tv.setText(Config.tabTitles[position]);
        return view;
    }
}
