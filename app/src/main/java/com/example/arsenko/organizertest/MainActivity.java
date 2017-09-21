package com.example.arsenko.organizertest;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends Activity {

    ArrayList<String> notes;

    Button saveBtn;

    ListView lvMain;

    EditText et;

    ArrayAdapter<String> adapter;

    SharedPreferences.Editor editor;

    SharedPreferences prefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        saveBtn = (Button) findViewById(R.id.saveBtn);

        lvMain = (ListView) findViewById(R.id.lvMain) ;

        et = (EditText) findViewById(R.id.et);

        notes = new ArrayList<>();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_checked, notes);

        prefs = getSharedPreferences("sPref", MODE_PRIVATE);

        if(prefs.getInt("counter", 0) >0) {
            for(int i=0; i<prefs.getInt("counter",0); i++){
             String myValue = prefs.getString("value_" + i, "");
                notes.add(myValue);
            }

        }else{
            System.out.println("Список пустий");
        }
        adapter.notifyDataSetChanged();

        et.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                    if (keyCode == KeyEvent.KEYCODE_ENTER) {
                        if (et.getText().toString().matches(".*[a-zA-Zа-яА-Я0-9].*")) {
                            notes.add(et.getText().toString());
                            adapter.notifyDataSetChanged();
                            //adapter.add(et.getText().toString());
                            et.setText("");
                            return true;
                        }

                    }
                return false;

            }
        });

        lvMain.setAdapter(adapter);


        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNotes();
            }

        });


        lvMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View itemClicked, int position, long id) {
                Toast.makeText(getApplicationContext(), "кароч, давай бистра:  " + ((TextView) itemClicked).getText(), Toast.LENGTH_SHORT).show();
            }
        });

        lvMain.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterLongView, View itemLongClicked, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(itemLongClicked.getContext());
                builder.setMessage("Delete this note?")
                        .setPositiveButton("Yes", new AlertDialog.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // adapter.remove(notes.get(id));
                                notes.remove(position);
                                adapter.notifyDataSetChanged();
                            }
                        }).setNegativeButton("No", null);

                AlertDialog alertDialog = builder.create();
                alertDialog.show();


                Toast.makeText(getApplicationContext(), "Видалено: " + ((TextView) itemLongClicked).getText(), Toast.LENGTH_SHORT).show();

                return false;
            }

        });
    }
    void saveNotes() {
        SharedPreferences prefs = getSharedPreferences("sPref", MODE_PRIVATE);
        editor = prefs.edit();
        editor.putInt("counter", notes.size());
        if (notes.size() > 0) {
            for (int i = 0; i < notes.size(); i++) {
                editor.putString("value_" + i, notes.get(i));
            }
        }
        editor.apply();

    }
    @Override
    public void onPause() {
        super.onPause();
        saveNotes();

    }
}