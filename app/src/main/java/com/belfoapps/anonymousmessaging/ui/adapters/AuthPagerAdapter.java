package com.belfoapps.anonymousmessaging.ui.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.belfoapps.anonymousmessaging.R;

import java.util.ArrayList;

public class AuthPagerAdapter extends FragmentStateAdapter {
    /*************************************** Declarations *****************************************/
    private ArrayList<Fragment> fragments;
    private Context context;

    /*************************************** Constructor ******************************************/
    public AuthPagerAdapter(@NonNull FragmentActivity fragmentActivity, ArrayList<Fragment> fragments, Context context) {
        super(fragmentActivity);
        this.fragments = fragments;
        this.context = context;
    }

    /*************************************** Methods **********************************************/
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragments.get(position);
    }

    @Override
    public int getItemCount() {
        return fragments.size();
    }

    public View getTabView(int position) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(context).inflate(R.layout.custom_tab, null);
        TextView tv = view.findViewById(R.id.tab_title);
        tv.setText(context.getResources().getStringArray(R.array.tab_titles)[position]);
        return view;
    }
}
