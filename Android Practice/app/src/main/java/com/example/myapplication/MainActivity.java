package com.example.myapplication;

import android.content.res.AssetManager;
import android.os.Build;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import android.widget.LinearLayout.LayoutParams;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.widget.RelativeLayout;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
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


public class MainActivity extends AppCompatActivity {
  
    final int numRooms = 146;
    final int numDataFields = 6;

    Button bfloor1north;
    Button bfloor2north;
    Button bfloor3north;
    Button bfloor4north;
    ImageButton searchButton;
    ImageButton clearButton;

    ImageView floor1north;
    ImageView floor2north;
    ImageView floor3north;
    ImageView floor4north;
    ImageView location;

    EditText searchBar;
    TextView roomNumber;
    TextView building;
    TextView floor;
    TextView roomName;

    AssetManager assetManager;

    String[][] roomDatabase = new String[numRooms][numDataFields];

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
        bfloor1north = findViewById(R.id.bfloor1north);
        bfloor2north = findViewById(R.id.bfloor2north);
        bfloor3north = findViewById(R.id.bfloor3north);
        bfloor4north = findViewById(R.id.bfloor4north);


        floor1north = findViewById(R.id.floor1north);
        floor2north = findViewById(R.id.floor2north);
        floor3north = findViewById(R.id.floor3north);
        floor4north = findViewById(R.id.floor4north);
        location = findViewById(R.id.location);

        bfloor1north.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                floor1north.setAlpha(1.0f);
                floor2north.setAlpha(0f);
                floor3north.setAlpha(0f);
                floor4north.setAlpha(0f);
            }
        });

        bfloor2north.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                floor1north.setAlpha(0f);
                floor2north.setAlpha(1.0f);
                floor3north.setAlpha(0f);
                floor4north.setAlpha(0f);
            }
        });

        bfloor3north.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                floor1north.setAlpha(0f);
                floor2north.setAlpha(0f);
                floor3north.setAlpha(1.0f);
                floor4north.setAlpha(0f);
            }
        });

        bfloor4north.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                floor1north.setAlpha(0f);
                floor2north.setAlpha(0f);
                floor3north.setAlpha(0f);
                floor4north.setAlpha(1.0f);
            }
        });

        //Initialize text fields
        searchBar = findViewById(R.id.searchBar);
        roomNumber = findViewById(R.id.roomNumber);
        building= findViewById(R.id.building);
        floor = findViewById(R.id.floor);
        roomName = findViewById(R.id.roomName);

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

                roomNumber.setText("Searching");

                int roomIndex = getRoomIndex(searchBar.getText().toString());
                if(roomIndex != -1){
                    roomNumber.setText(roomDatabase[roomIndex][0]);
                    building.setText(roomDatabase[roomIndex][1]);
                    floor.setText(roomDatabase[roomIndex][2]);
                    roomName.setText(roomDatabase[roomIndex][3]);
                    displayFloor(roomDatabase[roomIndex][2]);

                    location.setAlpha(1.0f);

                    bfloor1north.setAlpha(0.0f);
                    bfloor2north.setAlpha(0.0f);
                    bfloor3north.setAlpha(0.0f);
                    bfloor4north.setAlpha(0.0f);

                    int[] mapXY = {0,0};
                    floor1north.getLocationOnScreen(mapXY);
                    View location = findViewById(R.id.location);
                    RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams)location.getLayoutParams();
                    //int floorX = (int)floor1north.getX();
                    //String floorN = roomDatabase[roomIndex][2];
                    int floorX = getFloorX(roomDatabase[roomIndex][2]);
                    int floorY = getFloorY(roomDatabase[roomIndex][2]);
                    lp.leftMargin = floorX + Integer.parseInt(roomDatabase[roomIndex][4]);
                    lp.topMargin = floorY + Integer.parseInt(roomDatabase[roomIndex][5]);
                    //lp.rightMargin = 8;
                    //lp.bottomMargin = -815;
                    location.setLayoutParams(lp);
                }

                else{
                    location.setAlpha(0.0f);
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
                location.setAlpha(0.0f);
            }
        });

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
                bfloor1north.performClick();
                break;
            case "2":
                bfloor2north.performClick();
                break;
            case "3":
                bfloor3north.performClick();
                break;
            case "4":
                bfloor4north.performClick();
                break;
            default:
                bfloor2north.performClick();
                break;
        }
    }

    private int getFloorX(String floor){
        switch(floor){
            case "1":
                return (int)floor1north.getX();
            case "2":
                return (int)floor2north.getX();
            case "3":
                return (int)floor3north.getX();
            case "4":
                return (int)floor4north.getX();
            default:
                return (int)floor2north.getX();
        }
    }

    private int getFloorY(String floor){
        switch(floor){
            case "1":
                return (int)floor1north.getY();
            case "2":
                return (int)floor2north.getY();
            case "3":
                return (int)floor3north.getY();
            case "4":
                return (int)floor4north.getY();
            default:
                return (int)floor2north.getY();
        }
    }
}