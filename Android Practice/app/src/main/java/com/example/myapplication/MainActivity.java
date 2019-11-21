package com.example.myapplication;


import android.content.res.AssetManager;
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
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.widget.RelativeLayout;
import android.view.MenuItem;

import android.view.inputmethod.EditorInfo;

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
  
    final int numRooms = 148;
    final int numDataFields = 4;

    ImageButton searchButton;
    ImageButton clearButton;

    ImageView location;

    private ViewGroup rootlayout;

    int windowwidth;
    int windowheight;
    private int _xDelta;
    private int _yDelta;

    EditText searchBar;
    TextView roomNumber;
    TextView building;
    TextView floor;
    TextView roomName;

    AssetManager assetManager;

    String[][] roomDatabase = new String[numRooms][numDataFields];

    ArrayList<String> instructions = new ArrayList<String>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getDatabase(roomDatabase);

        //Initialize buttons

        searchButton = findViewById(R.id.searchButton);
        clearButton = findViewById(R.id.clearButton);
        searchButton = findViewById(R.id.searchButton);

        bfloor1 = findViewById(R.id.bfloor1);
        bfloor2 = findViewById(R.id.bfloor2);
        bfloor3 = findViewById(R.id.bfloor3);
        bfloor4 = findViewById(R.id.bfloor4);
        bclear = findViewById(R.id.bclear);
        bdirections = findViewById(R.id.bdirections);

        floorimage = findViewById(R.id.floorimage);

        floorimage.setOnTouchListener(this);
        location = findViewById(R.id.location);

        //Initialize text fields
        searchBar = findViewById(R.id.searchBar);
        roomNumber = findViewById(R.id.roomNumber);
        building= findViewById(R.id.building);
        floor = findViewById(R.id.floor);
        roomName = findViewById(R.id.roomName);

        rootlayout = (ViewGroup) findViewById(R.id.root);

        rootlayout.post(new Runnable() {
            @Override
            public void run() {
                windowwidth = rootlayout.getWidth();
                windowheight = rootlayout.getHeight();
            }
        });
        searchBar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    searchButton.performClick();
                }
                return false;
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){

                int roomIndex = getRoomIndex(searchBar.getText().toString());
                if(roomIndex != -1){
                    roomNumber.setText(roomDatabase[roomIndex][0]);
                    building.setText(roomDatabase[roomIndex][1]);
                    floor.setText(roomDatabase[roomIndex][2]);
                    roomName.setText(roomDatabase[roomIndex][3]);
                    displayFloor(roomDatabase[roomIndex][2]);
                    location.setAlpha(1.0f);
                }
                else{
                    roomNumber.setText("Couldn't find a room with that name");
                    building.setText("");
                    floor.setText("");
                    roomName.setText("");
                }
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){
                searchBar.getText().clear();
            }
        });

        /*fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

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
        ArrayList<String> instructions = new ArrayList<String>();

        int startIndex = getRoomIndex(start);
        int endIndex = getRoomIndex(end);

        if (startIndex == -1 || endIndex == -1) {
            instructions.add("target room does not exist");
            return instructions;
        }

        if ((roomDatabase[startIndex][1] != roomDatabase[endIndex][1]) & roomDatabase[endIndex][1] != "N/A") {
            instructions.add("Go to the " + roomDatabase[endIndex][1] + " building.");
        }
        else if (roomDatabase[endIndex][1] == "N/A") {
            instructions.add("Exit the building.");
        }

        if (roomDatabase[endIndex][2] == "N/A") {
            instructions.add("Go to floor " + roomDatabase[endIndex][2]);
        }

        instructions.add("Go to Room " + roomDatabase[endIndex][0]);

        return instructions;
    }


    private boolean getDatabase(String[][] database) {
        assetManager = getAssets();
        //String input = "";
        try {
            InputStream inputStream = assetManager.open("RoomDatabase.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";

                for(int i = 0; i<numRooms; i++){
                    //Read in list number of room, but don't process the data.
                    bufferedReader.readLine();
                    for(int j = 0; j<numDataFields; j++){
                        if( (receiveString = bufferedReader.readLine()) != null ) {
                            database[i][j] = receiveString;
                        }
                    }
                    //Read in empty space between entries.
                    bufferedReader.readLine();
                }

                inputStream.close();
                //input = stringBuilder.toString();
            }
            return true;
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
            return false;
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
            return false;
        }
    }

    private int getRoomIndex(String n){
        for(int j = 0; j < numRooms; j++){
            if(n.compareToIgnoreCase(roomDatabase[j][0]) == 0|| n.compareToIgnoreCase(roomDatabase[j][3]) == 0){
                if(!n.equals("N/A")){
                    return j;
                }
            }
        }
        return -1;
    }

    private void displayFloor(String floor){
        switch(floor){
            case "1":
                bfloor1.performClick();
                break;
            case "2":
                bfloor2.performClick();
                break;
            case "3":
                bfloor3.performClick();
                break;
            case "4":
                bfloor4.performClick();
                break;
            default:
                bfloor2.performClick();
                break;
        }
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }*/

    /*@Override
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
    }*/
}