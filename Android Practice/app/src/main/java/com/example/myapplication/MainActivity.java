package com.example.myapplication;

import android.os.Build;
import android.content.Context;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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

public class MainActivity extends AppCompatActivity implements View.OnTouchListener{
    Button bfloor1north;
    Button bfloor2north;
    Button bfloor3north;
    Button bfloor4north;
    Button bfloor1south;
    Button bfloor2south;
    Button bfloor3south;
    Button bclear;
    ImageView floor1north;
    ImageView floor2north;
    ImageView floor3north;
    ImageView floor4north;
    ImageView floor1south;
    ImageView floor2south;
    ImageView floor3south;
    private ViewGroup rootlayout;

    int windowwidth;
    int windowheight;
    private int _xDelta;
    private int _yDelta;

    ImageButton searchButton;
    EditText searchBar;
    TextView mText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Initialize buttons
        searchButton = (ImageButton)findViewById(R.id.searchButton);
        FloatingActionButton fab = findViewById(R.id.fab);
        bfloor1north = findViewById(R.id.bfloor1north);
        bfloor2north = findViewById(R.id.bfloor2north);
        bfloor3north = findViewById(R.id.bfloor3north);
        bfloor4north = findViewById(R.id.bfloor4north);
        bfloor1south = findViewById(R.id.bfloor1south);
        bfloor2south = findViewById(R.id.bfloor2south);
        bfloor3south = findViewById(R.id.bfloor3south);
        bclear = findViewById(R.id.bclear);

        floor1north = findViewById(R.id.floor1north);
        floor2north = findViewById(R.id.floor2north);
        floor3north = findViewById(R.id.floor3north);
        floor4north = findViewById(R.id.floor4north);
        floor1south = findViewById(R.id.floor1south);
        floor2south = findViewById(R.id.floor2south);
        floor3south = findViewById(R.id.floor3south);

        floor1north.setOnTouchListener(this);
        floor2north.setOnTouchListener(this);
        floor3north.setOnTouchListener(this);
        floor4north.setOnTouchListener(this);
        floor1south.setOnTouchListener(this);
        floor2south.setOnTouchListener(this);
        floor3south.setOnTouchListener(this);

        rootlayout = (ViewGroup) findViewById(R.id.root);

        rootlayout.post(new Runnable() {
            @Override
            public void run() {
                windowwidth = rootlayout.getWidth();
                windowheight = rootlayout.getHeight();
            }
        });

        bfloor1north.setOnClickListener(new View.OnClickListener() {
            public void onClick (View view){
                floor1north.setAlpha(1.0f);
                floor2north.setAlpha(0f);
                floor3north.setAlpha(0f);
                floor4north.setAlpha(0f);
                floor1south.setAlpha(0f);
                floor2south.setAlpha(0f);
                floor3south.setAlpha(0f);
            }
        });

        bfloor2north.setOnClickListener(new View.OnClickListener() {
            public void onClick (View view){
                floor1north.setAlpha(0f);
                floor2north.setAlpha(1.0f);
                floor3north.setAlpha(0f);
                floor4north.setAlpha(0f);
                floor1south.setAlpha(0f);
                floor2south.setAlpha(0f);
                floor3south.setAlpha(0f);
            }
        });

        bfloor3north.setOnClickListener(new View.OnClickListener() {
            public void onClick (View view){
                floor1north.setAlpha(0f);
                floor2north.setAlpha(0f);
                floor3north.setAlpha(1.0f);
                floor4north.setAlpha(0f);
                floor1south.setAlpha(0f);
                floor2south.setAlpha(0f);
                floor3south.setAlpha(0f);
            }
        });

        bfloor4north.setOnClickListener(new View.OnClickListener() {
            public void onClick (View view){
                floor1north.setAlpha(0f);
                floor2north.setAlpha(0f);
                floor3north.setAlpha(0f);
                floor4north.setAlpha(1.0f);
                floor1south.setAlpha(0f);
                floor2south.setAlpha(0f);
                floor3south.setAlpha(0f);
            }
        });

        bfloor1south.setOnClickListener(new View.OnClickListener() {
            public void onClick (View view){
                floor1north.setAlpha(0f);
                floor2north.setAlpha(0f);
                floor3north.setAlpha(0f);
                floor4north.setAlpha(0f);
                floor1south.setAlpha(1.0f);
                floor2south.setAlpha(0f);
                floor3south.setAlpha(0f);
            }
        });

        bfloor2south.setOnClickListener(new View.OnClickListener() {
            public void onClick (View view){
                floor1north.setAlpha(0f);
                floor2north.setAlpha(0f);
                floor3north.setAlpha(0f);
                floor4north.setAlpha(0f);
                floor1south.setAlpha(0f);
                floor2south.setAlpha(1.0f);
                floor3south.setAlpha(0f);
            }
        });

        bfloor3south.setOnClickListener(new View.OnClickListener() {
            public void onClick (View view){
                floor1north.setAlpha(0f);
                floor2north.setAlpha(0f);
                floor3north.setAlpha(0f);
                floor4north.setAlpha(0f);
                floor1south.setAlpha(0f);
                floor2south.setAlpha(0f);
                floor3south.setAlpha(1.0f);
            }
        });

        bclear.setOnClickListener(new View.OnClickListener() {
            public void onClick (View view){
                floor1north.setAlpha(0f);
                floor2north.setAlpha(0f);
                floor3north.setAlpha(0f);
                floor4north.setAlpha(0f);
                floor1south.setAlpha(0f);
                floor2south.setAlpha(0f);
                floor3south.setAlpha(0f);
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){
                searchBar = (EditText)findViewById(R.id.searchBar);
                mText = (TextView)findViewById(R.id.mText);
                mText.setText("Room #: " +searchBar.getText().toString()+"!");
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
        });
        
        

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
