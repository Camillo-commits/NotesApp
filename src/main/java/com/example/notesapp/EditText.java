package com.example.notesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

public class EditText extends AppCompatActivity {

    TextView textView;
    ArrayList<String> arrayList = new ArrayList<>();
    int position;
    String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_text);
        Intent intent = getIntent();
        text = intent.getStringExtra("text");
        position = intent.getIntExtra("position",-1);

        textView = (TextView) findViewById(R.id.textView);
        try {
            arrayList = (ArrayList<String>) ObjectSerializer.deserialize(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
        text = arrayList.get(position);
        textView.setText(text);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        text = textView.getText().toString();
        Log.i("back",textView.getText().toString());
        SharedPreferences preferences = this.getSharedPreferences("com.example.notesapp", Context.MODE_PRIVATE);
        arrayList.remove(position);
        arrayList.add(text);
        try {
            preferences.edit().putString("text",ObjectSerializer.serialize(arrayList)).apply();
        } catch (IOException e) {
            e.printStackTrace();
        }
        onPause();
        //finish();
    }
}
