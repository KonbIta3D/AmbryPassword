package com.ambry.passw.activity;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.passww.R;

/**
 * Created by pcuser on 25.07.13.
 */
public class MyAdapter extends BaseAdapter {

    LayoutInflater inflater;
    ArrayList<Item> items;
    private Context context;


    public MyAdapter(Context context, ArrayList<Item> items) {
        this.context = context;
        this.items = items;
        inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public Item getMyItem(int position) {
        return (Item) getItem(position);
    }

    public int getCount() {
        return items.size();
    }

    public Object getItem(int position) {
        return items.get(position);
    }

    public long getItemId(int position) {
        return items.get(position).getId();
    }

    public View getView(int position, View view, ViewGroup viewGroup) {
        View myListView = view;
        if (myListView == null) {
            myListView = inflater.inflate(R.layout.item, viewGroup, false);
        }
        Item item = (Item) getItem(position);
        ((TextView) myListView.findViewById(R.id.tvTextLogin)).setText(item.getLogin());
        ((TextView) myListView.findViewById(R.id.tvTextPassword)).setText(item.getPassword());
        ((TextView) myListView.findViewById(R.id.tvTextComment)).setText(item.getComment());

        return myListView;

    }


}
