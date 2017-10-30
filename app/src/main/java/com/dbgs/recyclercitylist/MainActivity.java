package com.dbgs.recyclercitylist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.dbgs.recyclercitylist.listener.GroupListener;
import com.dbgs.recyclercitylist.listener.PowerGroupListener;
import com.dbgs.recyclercitylist.model.City;
import com.dbgs.recyclercitylist.util.CityUtil;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerview;
    List<City> list =null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerview= (RecyclerView) findViewById(R.id.recyclerview);
//        SimpleDecoration simpleDecoration = new SimpleDecoration();
//        recyclerview.addItemDecoration(simpleDecoration);

          list = CityUtil.getCityList();


        recyclerview.addItemDecoration(powerStickDecoration);


        MyAdapter adapter = new MyAdapter(list);
        recyclerview.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        recyclerview.setAdapter(adapter);
    }

    StickDecoration stickDecoration = new StickDecoration(new GroupListener() {
        @Override
        public String getGroupName(int position) {
            return list.get(position).getProvince();
        }
    });

    PowerStickDecoration powerStickDecoration = new PowerStickDecoration(new PowerGroupListener() {
        @Override
        public String getGroupName(int position) {
             return list.get(position).getProvince();
        }

        @Override
        public View getGroupView(int position) {
            if (list.size() > position) {
                View view = getLayoutInflater().inflate(R.layout.father, null, false);
                ((TextView) view.findViewById(R.id.tv_father)).setText(list.get(position).getProvince());
                return view;
            } else {
                return null;
            }
        }
    });

}
