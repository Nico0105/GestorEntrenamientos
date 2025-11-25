package com.example.gestorentrenamientos;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.gestorentrenamientos.database.User;

import java.util.List;

public class UsersAdapter extends ArrayAdapter<User> {

    public UsersAdapter(Context context, List<User> users) {
        super(context, android.R.layout.simple_list_item_2, android.R.id.text1, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = super.getView(position, convertView, parent);

        TextView t1 = v.findViewById(android.R.id.text1);
        TextView t2 = v.findViewById(android.R.id.text2);

        User u = getItem(position);

        t1.setText(u.getUsername());
        t2.setText(u.getEmail());

        return v;
    }
}
