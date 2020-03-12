package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    AdapterList adapterList;
    SimpleAdapter adapter;
    ArrayList<String> temp;
    File source;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    public void init(){
        source=new File(this.getExternalFilesDir(null),"source.txt");
        if(!source.exists()) {
            writeToFile(source);
        }
        readFromFile(source);
        setContent().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapterList.adapterList.remove(parent.getItemAtPosition(position));
                removeFromFile(source,position);
                adapter.notifyDataSetChanged();
            }
        });


        final SwipeRefreshLayout refresh=findViewById(R.id.refresh);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh.setRefreshing(false);
                setContent();
                adapter.notifyDataSetChanged();
            }
        });
    }

    public ListView setContent(){
        adapterList=new AdapterList(temp);
        adapter=adapterList.createAdapter(this);
        ListView listView=findViewById(R.id.myListView);
        listView.setAdapter(adapter);
        return listView;
    }

    private void readFromFile(File source){
        try {
            ArrayList<String> lines=new ArrayList<>();
            String oneLine;
            FileReader fromSource = new FileReader(source);
            BufferedReader br=new BufferedReader(fromSource);
            while((oneLine=br.readLine())!=null){
                lines.add(oneLine);
            }
            temp=lines;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeToFile(File source){
        try {
            FileWriter toSource = new FileWriter(source,false);
            toSource.append(getString(R.string.large_text));
            toSource.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeStringToFile(File source){
        try {
            FileWriter toSource = new FileWriter(source,false);
            toSource.append(temp.toString());
            toSource.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void removeFromFile(File source, int position){
        try {
            ArrayList<String> lines=new ArrayList<>();
            String oneLine;
            FileReader fromSource = new FileReader(source);
            BufferedReader br=new BufferedReader(fromSource);
            int i=0;
            while((oneLine=br.readLine())!=null){
                if(i==position){
                    i++;
                    continue;
                }
                lines.add(oneLine);
                i++;
            }
            temp=lines;
            writeStringToFile(source);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}