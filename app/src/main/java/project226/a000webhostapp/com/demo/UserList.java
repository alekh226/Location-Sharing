package project226.a000webhostapp.com.demo;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.ArrayList;
import java.util.List;


public class UserList extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_user_list, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.userList);
        // use this setting to
        // improve performance if you know that changes
        // in content do not change the layout size
        // of the RecyclerView
        recyclerView.setHasFixedSize(true);
        // use a linear layout manager
        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        List<String> input = new ArrayList<>();
        List<String> keys = new ArrayList<>();

        MyDBHandler myDBHandler = new MyDBHandler(getActivity().getApplicationContext(),null,null,1);

        input = myDBHandler.getUserNameList();
        keys=myDBHandler.getKeyList();
        mAdapter = new MyAdapter(getActivity().getApplicationContext(),input,keys);
        recyclerView.setAdapter(mAdapter);
        return view;
    }





}
