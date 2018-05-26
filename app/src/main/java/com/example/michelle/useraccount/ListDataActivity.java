package com.example.michelle.useraccount;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ListDataActivity extends AppCompatActivity {

    DatabaseHelper mDatabaseHelper;
    private ListView mListView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nutrition_list_layout);
        mListView = (ListView) findViewById(R.id.listView);
        mDatabaseHelper = new DatabaseHelper(this);

        populateListView();
    }

    private void populateListView() {
        Cursor data = mDatabaseHelper.getData();
        ArrayList<String> listData = new ArrayList<>();
        final ArrayList<Integer> listCarbohydrates = new ArrayList<>();
        final ArrayList<Integer> listFat = new ArrayList<>();
        final ArrayList<Integer> listProtein = new ArrayList<>();
        final ArrayList<Integer> listSugar = new ArrayList<>();
        final ArrayList<Integer> listEnergy = new ArrayList<>();

        while (data.moveToNext()) {
            listData.add(data.getString(1));
            listCarbohydrates.add(data.getInt(2));
            listFat.add(data.getInt(3));
            listProtein.add(data.getInt(4));
            listSugar.add(data.getInt(5));
            listEnergy.add(data.getInt(6));
        }

        final ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listData);
        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String foodActivity = parent.getItemAtPosition(position).toString();
                int carbohydrates = listCarbohydrates.get(position);
                int fat = listFat.get(position);
                int protein = listProtein.get(position);
                int sugar = listSugar.get(position);
                int energy = listEnergy.get(position);

                Cursor data = mDatabaseHelper.getItemID(foodActivity);

                int itemID = -1;
                while (data.moveToNext()) {
                    itemID = data.getInt(0);
                }

                if (itemID > -1) {
                    Intent editScreenIntent = new Intent(ListDataActivity.this, EditDataActivity.class);
                    editScreenIntent.putExtra("id", itemID);
                    editScreenIntent.putExtra("foodActivity", foodActivity);
                    editScreenIntent.putExtra("carbohydrates", carbohydrates);
                    editScreenIntent.putExtra("fat", fat);
                    editScreenIntent.putExtra("protein", protein);
                    editScreenIntent.putExtra("sugar", sugar);
                    editScreenIntent.putExtra("energy", energy);
                    startActivity(editScreenIntent);
                    finish();
                } else {
                    toastMessage("No associated ID for food or activity");
                }
            }
        });
    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}