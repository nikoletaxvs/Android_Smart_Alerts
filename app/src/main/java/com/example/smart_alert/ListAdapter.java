package com.example.smart_alert;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ListAdapter extends BaseAdapter {
    Context context;
   ArrayList<NaturalEvent> naturalEvents = new ArrayList<>();
    LayoutInflater inflater;

    public ListAdapter(Context ctx,ArrayList<NaturalEvent> naturalEvents){
        this.context = ctx;
        this.naturalEvents = naturalEvents;
        inflater = LayoutInflater.from(ctx);
    }



    @Override
    public int getCount() {
        return naturalEvents.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.list_item_employee,null);
        TextView txtView = (TextView) view.findViewById(R.id.eventname);
        TextView txtscore= (TextView) view.findViewById(R.id.score);
        TextView txtTime = (TextView) view.findViewById(R.id.time);
        ImageView eventImg = (ImageView) view.findViewById(R.id.event_pic);

        txtView.setText(naturalEvents.get(i).getTitle());
        txtscore.setText((naturalEvents.get(i).getScore()));
        txtTime.setText(naturalEvents.get(i).getTimestamp());

        Picasso.with(context)
                .load(naturalEvents.get(i).getImageUrl())
                        .fit()
                        .centerCrop()
                        .into(eventImg);
       // eventImg.setImageResource(R.drawable.no_photo);
        return view;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}
