package com.haochen.pokedexgo.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.haochen.pokedexgo.R;

/**
 * Created by Haochen on 2016/7/22.
 */
public abstract class BaseFragment extends Fragment {
    protected View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = createView(inflater, container, savedInstanceState);
            initial();
            show();
        } else {
            update();
        }
        return view;
    }

    protected abstract View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);
    protected abstract void initial();
    protected abstract void show();
    public abstract void update();

}
