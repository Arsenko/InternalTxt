package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private AdapterList adapterList;
    private SimpleAdapter adapter;
    private ArrayList<String> temp;
    private File source;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        source = new File(this.getExternalFilesDir(null), "source.txt");
        if (!source.exists()) {
            writeToFile(source);
        }
        readFromFile(source);
        setContent().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapterList.adapterList.remove(position);
                removeFromFile(source, position);
                adapter.notifyDataSetChanged();
            }
        });


        final SwipeRefreshLayout refresh = findViewById(R.id.refresh);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh.setRefreshing(false);
                setContent();
                adapter.notifyDataSetChanged();
            }
        });
    }

    public ListView setContent() {
        adapterList = new AdapterList(temp);
        adapter = adapterList.createAdapter(this);
        ListView listView = findViewById(R.id.myListView);
        listView.setAdapter(adapter);
        return listView;
    }

    private void readFromFile(File source) {
        try(FileReader fromSource = new FileReader(source)) {
            ArrayList<String> lines = new ArrayList<>();
            String oneLine;
            BufferedReader br = new BufferedReader(fromSource);
            while ((oneLine = br.readLine()) != null) {
                lines.add(oneLine);
            }
            temp = lines;
        } catch (IOException e) {
            Toast.makeText(this,getString(R.string.errorOpenFile),Toast.LENGTH_LONG).show();
        }
    }

    private void writeToFile(File source) {
        try (FileWriter toSource = new FileWriter(source, false)) {
            toSource.append(getString(R.string.large_text));
        } catch (IOException e) {
            Toast.makeText(this,getString(R.string.errorOpenFile),Toast.LENGTH_LONG).show();
        }
    }

    private void writeStringToFile(File source) {
        try (FileWriter toSource = new FileWriter(source, false)){
            toSource.append(temp.toString());
        } catch (IOException e) {
            Toast.makeText(this,getString(R.string.errorOpenFile),Toast.LENGTH_LONG).show();
        }
    }

    private void removeFromFile(File source, int position) {
        try ( FileReader fromSource = new FileReader(source)){
            ArrayList<String> lines = new ArrayList<>();
            String oneLine;
            BufferedReader br = new BufferedReader(fromSource);
            int i = 0;
            while ((oneLine = br.readLine()) != null) {
                if (i == position) {
                    i++;
                    continue;
                }
                lines.add(oneLine);
                i++;
            }
            temp = lines;
            writeStringToFile(source);
        } catch (IOException e) {
            Toast.makeText(this,getString(R.string.errorOpenFile),Toast.LENGTH_LONG).show();
        }
    }
}