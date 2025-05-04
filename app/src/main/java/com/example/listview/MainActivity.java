package com.example.listview;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText editTextItem;
    Button buttonAdd, buttonSave;
    ListView listViewItems;

    ArrayList<String> itemList;
    ArrayAdapter<String> adapter;

    int editIndex = -1;

    final String PREFS_NAME = "MyPrefs";
    final String LIST_KEY = "ItemList";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextItem = findViewById(R.id.editTextItem);
        buttonAdd = findViewById(R.id.buttonAdd);
        buttonSave = findViewById(R.id.buttonSave);
        listViewItems = findViewById(R.id.listViewItems);

        itemList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, itemList);
        listViewItems.setAdapter(adapter);

        loadListFromPrefs();

        buttonAdd.setOnClickListener(v -> {
            String item = editTextItem.getText().toString().trim();
            if (!item.isEmpty()) {
                if (editIndex == -1) {
                    itemList.add(item);
                } else {
                    itemList.set(editIndex, item);
                    editIndex = -1;
                }
                adapter.notifyDataSetChanged();
                editTextItem.setText("");
            } else {
                Toast.makeText(this, "Please enter text", Toast.LENGTH_SHORT).show();
            }
        });

        listViewItems.setOnItemClickListener((parent, view, position, id) -> {
            editTextItem.setText(itemList.get(position));
            editIndex = position;
        });

        buttonSave.setOnClickListener(v -> {
            saveListToPrefs();
            Toast.makeText(this, "List Saved!", Toast.LENGTH_SHORT).show();
        });
    }

    private void saveListToPrefs() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        JSONArray jsonArray = new JSONArray(itemList);
        editor.putString(LIST_KEY, jsonArray.toString());
        editor.apply();
    }

    private void loadListFromPrefs() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String savedList = prefs.getString(LIST_KEY, null);
        if (savedList != null) {
            try {
                JSONArray jsonArray = new JSONArray(savedList);
                for (int i = 0; i < jsonArray.length(); i++) {
                    itemList.add(jsonArray.getString(i));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
