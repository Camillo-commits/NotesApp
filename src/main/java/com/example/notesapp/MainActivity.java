package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView list;
    SharedPreferences preferences;
    ArrayList<String> stringList;
    ArrayAdapter<String> adapter;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        if(item.getItemId() == R.id.noteAddButton){
            stringList.add("");
            try {
                preferences.edit().putString("text",ObjectSerializer.serialize(stringList)).apply();
            } catch (IOException e) {
                e.printStackTrace();
            }
            adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, stringList);
            list.setAdapter(adapter);
        }

        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list = (ListView) findViewById(R.id.List);
        preferences = this.getSharedPreferences("com.example.notesapp", Context.MODE_PRIVATE);
        try {
            stringList = (ArrayList<String>) ObjectSerializer.deserialize(preferences.getString("text",ObjectSerializer.serialize(new ArrayList<String>())));
        } catch (IOException e) {
            e.printStackTrace();
        }
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, stringList);
        list.setAdapter(adapter);
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(MainActivity.this).setTitle("Do you want to delete this note?")
                        .setNegativeButton("NO",null)
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                stringList.remove(position);
                                try {
                                    preferences.edit().putString("text",ObjectSerializer.serialize(stringList)).apply();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                adapter = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1, stringList);
                                list.setAdapter(adapter);
                            }

                        })
                        .show();
                return true;
            }
        });
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                Intent intent = new Intent(getApplicationContext(),editText.class);

                try {
                    intent.putExtra("text",ObjectSerializer.serialize(stringList));
                    intent.putExtra("position",position);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, stringList);
        list.setAdapter(adapter);

    }

    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);
        Log.i("onReEnter","true");
        preferences = this.getSharedPreferences("com.example.notesapp", Context.MODE_PRIVATE);
        try {
            stringList = (ArrayList<String>) ObjectSerializer.deserialize(preferences.getString("text",ObjectSerializer.serialize(new ArrayList<String>())));
        } catch (IOException e) {
            e.printStackTrace();
        }
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, stringList);
        list.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        System.exit(0);
    }
}
