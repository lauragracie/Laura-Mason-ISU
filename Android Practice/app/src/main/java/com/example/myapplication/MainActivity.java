package com.example.myapplication;

import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    Button bfloor1north;
    Button bfloor2north;
    Button bfloor3north;
    Button bfloor4north;
    ImageView floor1north;
    ImageView floor2north;
    ImageView floor3north;
    ImageView floor4north;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bfloor1north = findViewById(R.id.bfloor1north);
        bfloor2north = findViewById(R.id.bfloor2north);
        bfloor3north = findViewById(R.id.bfloor3north);
        bfloor4north = findViewById(R.id.bfloor4north);
        floor1north = findViewById(R.id.floor1north);
        floor2north = findViewById(R.id.floor2north);
        floor3north = findViewById(R.id.floor3north);
        floor4north = findViewById(R.id.floor4north);

        bfloor1north.setOnClickListener(new View.OnClickListener() {
            public void onClick (View view){
                floor1north.setAlpha(1.0f);
                floor2north.setAlpha(0f);
                floor3north.setAlpha(0f);
                floor4north.setAlpha(0f);
            }
        });

        bfloor2north.setOnClickListener(new View.OnClickListener() {
            public void onClick (View view){
                floor1north.setAlpha(0f);
                floor2north.setAlpha(1.0f);
                floor3north.setAlpha(0f);
                floor4north.setAlpha(0f);
            }
        });

        bfloor3north.setOnClickListener(new View.OnClickListener() {
            public void onClick (View view){
                floor1north.setAlpha(0f);
                floor2north.setAlpha(0f);
                floor3north.setAlpha(1.0f);
                floor4north.setAlpha(0f);
            }
        });

        bfloor4north.setOnClickListener(new View.OnClickListener() {
            public void onClick (View view){
                floor1north.setAlpha(0f);
                floor2north.setAlpha(0f);
                floor3north.setAlpha(0f);
                floor4north.setAlpha(1.0f);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
