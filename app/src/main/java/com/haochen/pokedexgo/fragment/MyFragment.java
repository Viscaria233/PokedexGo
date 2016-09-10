package com.haochen.pokedexgo.fragment;

import android.os.Handler;
import android.view.View;
import android.widget.ListView;

import com.haochen.pokedexgo.R;
import com.haochen.pokedexgo.adapter.MyAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by Haochen on 2016/8/12.
 */
public abstract class MyFragment extends BaseFragment implements ISearch {

    protected List data;
    protected ListView listView;
    protected MyAdapter adapter;
    protected HashMap<View, Boolean> checked;
    protected Handler handler;
    protected static final int CHANGE_ADAPTER_DATA = 1;

    protected void setChecked(View v, boolean checked) {
        if (!this.checked.containsKey(v)) {
            return;
        }
        if (checked != this.checked.get(v)) {
            this.checked.remove(v);
            this.checked.put(v, checked);
            if (checked) {
                v.setBackgroundResource(R.color.header_checked);
            } else {
                v.setBackgroundResource(R.color.header);
            }
        }
    }

    protected void resetChecked() {
        Set<View> set = checked.keySet();
        List<View> list = new ArrayList<>();
        for (View item : set) {
            if (checked.get(item)) {
                list.add(item);
            }
        }
        for (View item : list) {
            setChecked(item, false);
        }
    }

    protected List sort(List list, Comparator comparator) {
        Object[] array = list.toArray();
        Arrays.sort(array, comparator);
        ArrayList<Object> newList = new ArrayList<>();
        for (Object item : array) {
            newList.add(item);
        }
        return newList;
    }

    protected List reverse(List list) {
        List newList = new ArrayList<>();
        for (int i = list.size() - 1; i >= 0; --i) {
            newList.add(list.get(i));
        }
        return newList;
    }

    @Override
    public void search(String key) {
        resetChecked();
    }
}
