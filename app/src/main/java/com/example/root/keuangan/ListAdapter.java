package com.example.root.keuangan;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class ListAdapter extends BaseAdapter {
    public Context context;
    private List<ModelList> list;

    public ListAdapter(Context context, List<ModelList> list) {
        this.context = context;
        this.list    = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = View.inflate(context, R.layout.item_list, null);
        TextView no      = (TextView)v.findViewById(R.id.no_item);
        TextView title   = (TextView)v.findViewById(R.id.title_item);
        TextView nominal = (TextView)v.findViewById(R.id.nominal_item);
        TextView date    = (TextView)v.findViewById(R.id.date_item);

        if(list.get(position).getIsType().equals("Kas")) nominal.setTextColor(Color.parseColor("#55AA33"));

        Integer idValue     = list.get(position).getId();
        Integer noValue     = list.get(position).getNo();
        String titleValue   = list.get(position).getTitle();
        String nominalValue = list.get(position).getNominal();
        String dateValue    = list.get(position).getDate();

        //Set Text
        no.setText(noValue + ". ");
        title.setText(titleValue);
        nominal.setText(nominalValue);
        date.setText(dateValue);

        v.setTag(list.get(position).getId());
        return v;
    }
}