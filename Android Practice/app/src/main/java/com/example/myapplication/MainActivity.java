package com.example.myapplication;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.content.Context;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.InputType;
import android.view.MotionEvent;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.widget.RelativeLayout;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener{
    Button bfloor1;
    Button bfloor2;
    Button bfloor3;
    Button bfloor4;

    Button bclear;

    Button bdirections;

    ImageView floorimage;

    private ViewGroup rootlayout;

    int windowwidth;
    int windowheight;
    private int _xDelta;
    private int _yDelta;

    ImageButton searchButton;
    EditText searchBar;
    TextView mText;

    List<String> instructions = new ArrayList<String>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Initialize buttons
        searchButton = (ImageButton)findViewById(R.id.searchButton);
        bfloor1 = findViewById(R.id.bfloor1);
        bfloor2 = findViewById(R.id.bfloor2);
        bfloor3 = findViewById(R.id.bfloor3);
        bfloor4 = findViewById(R.id.bfloor4);
        bclear = findViewById(R.id.bclear);
        bdirections = findViewById(R.id.bdirections);

        floorimage = findViewById(R.id.floorimage);

        floorimage.setOnTouchListener(this);



        rootlayout = (ViewGroup) findViewById(R.id.root);

        rootlayout.post(new Runnable() {
            @Override
            public void run() {
                windowwidth = rootlayout.getWidth();
                windowheight = rootlayout.getHeight();
            }
        });

        bfloor1.setOnClickListener(new View.OnClickListener() {
            public void onClick (View view){
                floorimage.setAlpha(1.0f);
                floorimage.setImageResource(R.drawable.floor1);
            }
        });

        bfloor2.setOnClickListener(new View.OnClickListener() {
            public void onClick (View view){
                floorimage.setAlpha(1.0f);
                floorimage.setImageResource(R.drawable.floor2);
            }
        });

        bfloor3.setOnClickListener(new View.OnClickListener() {
            public void onClick (View view){
                floorimage.setAlpha(1.0f);
                floorimage.setImageResource(R.drawable.floor3);
            }
        });

        bfloor4.setOnClickListener(new View.OnClickListener() {
            public void onClick (View view){
                floorimage.setAlpha(1.0f);
                floorimage.setImageResource(R.drawable.floor4);
            }
        });


        bclear.setOnClickListener(new View.OnClickListener() {
            public void onClick (View view){
                floorimage.setAlpha(0f);
            }
        });

        bdirections.setOnClickListener(new View.OnClickListener(){
            public void onClick (View view){

            }
        });

    }

    public boolean onTouch(View view, MotionEvent event) {

        final int X = (int) event.getRawX();
        final int Y = (int) event.getRawY();

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                _xDelta = X - view.getLeft();
                _yDelta = Y - view.getTop();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_POINTER_UP:
                // Do nothing
                break;
            case MotionEvent.ACTION_MOVE:
                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) view
                        .getLayoutParams();
                // Image is centered to start, but we need to unhitch it to move it around.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    lp.removeRule(RelativeLayout.CENTER_HORIZONTAL);
                    lp.removeRule(RelativeLayout.CENTER_VERTICAL);
                } else {
                    lp.addRule(RelativeLayout.CENTER_HORIZONTAL, 0);
                    lp.addRule(RelativeLayout.CENTER_VERTICAL, 0);
                }
                lp.leftMargin = X - _xDelta;
                lp.topMargin = Y - _yDelta;
                // Negative margins here ensure that we can move off the screen to the right
                // and on the bottom. Comment these lines out and you will see that
                // the image will be hemmed in on the right and bottom and will actually shrink.
                lp.rightMargin = view.getWidth() - lp.leftMargin - windowwidth;
                lp.bottomMargin = view.getHeight() - lp.topMargin - windowheight;
                view.setLayoutParams(lp);
                break;
        }
        // invalidate is redundant if layout params are set or not needed if they are not set.
//        mRrootLayout.invalidate();
        return true;
    }

    public ArrayList<String> generateInstructions (String start, String end) {

    }

    private String readFromFile(Context context) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput("RoomDatabase.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
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
