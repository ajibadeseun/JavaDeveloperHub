package com.example.android.developerhub;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by DAMILOLA on 8/29/2017.
 */

public class GithubAdapter extends ArrayAdapter<Github> {

    public GithubAdapter(Context context, ArrayList<Github> accounts) {
        super(context,0, accounts);
    }


    @Override
    public View getView(int position,  View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.profile_list, parent, false);
        }
             Github github =  getItem(position);
        TextView textView = (TextView) listItemView.findViewById(R.id.textView);
        textView.setText(github.username);
        ImageView imageView = (ImageView) listItemView.findViewById(R.id.image);
        imageView.setImageResource(R.mipmap.ic_launcher);
        Glide.with(getContext()).load(github.imageUrl).into(imageView);

        return listItemView;
    }
}
