package com.dbgs.recyclercitylist;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dbgs.recyclercitylist.model.City;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by acerhss on 17-10-27.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyHodler> {

    List<City>  list;

    public MyAdapter(List<City> list) {
        this.list = list;
    }

    @Override
    public MyHodler onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.child, parent, false);

        return new MyHodler(view);
    }

    @Override
    public void onBindViewHolder(MyHodler holder, int position) {
        holder.setView(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list == null ?0 : list.size();
    }

    public static class MyHodler extends RecyclerView.ViewHolder{
        AppCompatTextView textView;
        public MyHodler(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_child);
        }

        public void setView(City data){
            textView.setText(data == null ? "" : data.getName());
        }
    }
}
