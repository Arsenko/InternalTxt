package com.example.myapplication;

import android.content.Context;
import android.widget.SimpleAdapter;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdapterList {
    private static final String HEADING = "heading";
    private static final String BODY = "body";
    List<Map<String, String>> adapterList;

    public AdapterList(ArrayList<String> ListText) {
        adapterList = prepareContent(ListText);
    }

    @NonNull
    public SimpleAdapter createAdapter(Context context) {
        return new SimpleAdapter(context, adapterList, R.layout.list_adapter_item, new String[]{HEADING, BODY}, new int[]{R.id.heading, R.id.body});
    }

    @NonNull
    private List<Map<String, String>> prepareContent(ArrayList<String> listText) {
        List<Map<String, String>> temp = new ArrayList<>();
        for (int i = 0; i < listText.size(); i++) {
            Map<String, String> mapItem = new HashMap<>();
            mapItem.put(HEADING, String.valueOf(listText.get(i).length()));
            mapItem.put(BODY, listText.get(i));
            temp.add(mapItem);
        }
        return temp;
    }
}
