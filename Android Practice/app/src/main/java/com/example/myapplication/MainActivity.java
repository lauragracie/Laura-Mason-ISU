package com.example.myapplication;


import android.content.res.AssetManager;
import android.os.Build;
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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import android.widget.LinearLayout.LayoutParams;

import android.text.InputType;
import android.view.MotionEvent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.widget.LinearLayout;
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

//public class MainActivity extends AppCompatActivity implements View.OnTouchListener{
public class MainActivity extends AppCompatActivity{
    Button bfloor1;
    Button bfloor2;
    Button bfloor3;
    Button bfloor4;

    Button bdirections;

    ImageView floorimage;
  
    final int numRooms = 146;
    final int numDataFields = 6;

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

    boolean displayRoom = false;


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

        bfloor1 = findViewById(R.id.bfloor1);
        bfloor2 = findViewById(R.id.bfloor2);
        bfloor3 = findViewById(R.id.bfloor3);
        bfloor4 = findViewById(R.id.bfloor4);
        bdirections = findViewById(R.id.bdirections);

        floorimage = findViewById(R.id.floorimage);

        floorimage.setOnTouchListener(this);
        location = findViewById(R.id.location);

        //Initialize text fields
        searchBar = findViewById(R.id.searchBar);
        roomNumber = findViewById(R.id.roomNumber);
        building = findViewById(R.id.building);
        floor = findViewById(R.id.floor);
        roomName = findViewById(R.id.roomName);


        rootlayout = (ViewGroup) findViewById(R.id.root);

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText aFrom = new EditText(this);
        aFrom.setHint("From");
        layout.addView(aFrom);

        final EditText aTo = new EditText(this);
        aTo.setHint("To");
        layout.addView(aTo);

        builder.setView(layout);

        builder.setTitle("Title");
        builder.setMessage("Message");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String from = aFrom.getText().toString();
                String to = aTo.getText().toString();
                int toRoomIndex = getRoomIndex(to);
                int fromRoomIndex = getRoomIndex(from);
                if(toRoomIndex != -1){
                    displayFloor(roomDatabase[toRoomIndex][2]);
                    location.setAlpha(1.0f);
                }
                else{

                }
                instructions.clear();
                instructions = generateInstructions(from, to);

                roomNumber.setText("");
                for (int i = 0; i < instructions.size(); i++) {
                    roomNumber.append((i + 1) + ". ");
                    roomNumber.append(instructions.get(i));
                    roomNumber.append("\n");
                }

                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        final AlertDialog alertDialog = builder.create();

        rootlayout.post(new Runnable() {
            @Override
            public void run() {
                windowwidth = rootlayout.getWidth();
                windowheight = rootlayout.getHeight();
            }
        });
      
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) floorimage.getLayoutParams();
        lp.topMargin = 100;
        lp.bottomMargin = 200;
        lp.rightMargin = 200;
        lp.width = 1696;
        lp.height = 1180;
        floorimage.setLayoutParams(lp);


        searchBar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    searchButton.performClick();
                }
                return false;
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                roomNumber.setText("Searching");

                int roomIndex = getRoomIndex(searchBar.getText().toString());
                if (roomIndex != -1) {

                    /*RelativeLayout.LayoutParams lp1 = (RelativeLayout.LayoutParams) floorimage.getLayoutParams();
                    lp1.width = 1660;
                    lp1.height = 2360;
                    floorimage.setLayoutParams(lp1);*/



                    roomNumber.setText(roomDatabase[roomIndex][0]);
                    building.setText(roomDatabase[roomIndex][1]);
                    floor.setText(roomDatabase[roomIndex][2]);
                    roomName.setText(roomDatabase[roomIndex][3]);
                    displayFloor(roomDatabase[roomIndex][2]);

                    location.setAlpha(1.0f);
                    displayRoom = true;

                    View location = findViewById(R.id.location);
                    RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) location.getLayoutParams();
                    int floorX = getFloorX(roomDatabase[roomIndex][2]);
                    int floorY = getFloorY(roomDatabase[roomIndex][2]);
                    lp.leftMargin = floorX + (int)(1.57*Integer.parseInt(roomDatabase[roomIndex][4]));
                    lp.topMargin = floorY + (int)(1.50*Integer.parseInt(roomDatabase[roomIndex][5]));
                    location.setLayoutParams(lp);

                } else {
                    location.setAlpha(0.0f);
                    displayRoom = false;
                    roomNumber.setText("Couldn't find a room with that name");
                    building.setText("");
                    floor.setText("");
                    roomName.setText("");
                }
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                searchBar.getText().clear();
                location.setAlpha(0.0f);
                displayRoom = false;
            }
        });


        rootlayout = (ViewGroup) findViewById(R.id.root);
        rootlayout.post(new Runnable() {
            @Override
            public void run() {
                windowwidth = rootlayout.getWidth();
                windowheight = rootlayout.getHeight();
            }
        });

        // Set up actions for all the buttons
        bfloor1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                floorimage.setAlpha(1.0f);
                floorimage.setImageResource(R.drawable.floor1);
                if(displayRoom){
                    int roomIndex = getRoomIndex(searchBar.getText().toString());
                    if(roomDatabase[roomIndex][2].compareTo("1") == 0){
                        location.setAlpha(1.0f);
                        displayRoom = true;
                    }
                    else{
                        location.setAlpha(0.0f);
                        //displayRoom = false;
                    }
                }
            }
        });

        bfloor2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                floorimage.setAlpha(1.0f);
                floorimage.setImageResource(R.drawable.floor2);
                if(displayRoom){
                    int roomIndex = getRoomIndex(searchBar.getText().toString());
                    if(roomDatabase[roomIndex][2].compareTo("2") == 0){
                        location.setAlpha(1.0f);
                        displayRoom = true;
                    }
                    else{
                        location.setAlpha(0.0f);
                        //displayRoom = false;
                    }
                }
            }
        });

        bfloor3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                floorimage.setAlpha(1.0f);
                floorimage.setImageResource(R.drawable.floor3);
                if(displayRoom){
                    int roomIndex = getRoomIndex(searchBar.getText().toString());
                    if(roomDatabase[roomIndex][2].compareTo("3") == 0){
                        location.setAlpha(1.0f);
                        displayRoom = true;
                    }
                    else{
                        location.setAlpha(0.0f);
                        //displayRoom = false;
                    }
                }
            }
        });

        bfloor4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                floorimage.setAlpha(1.0f);
                floorimage.setImageResource(R.drawable.floor4);
                if(displayRoom){
                    int roomIndex = getRoomIndex(searchBar.getText().toString());
                    if(roomDatabase[roomIndex][2].compareTo("4") == 0){
                        location.setAlpha(1.0f);
                        displayRoom = true;
                    }
                    else{
                        location.setAlpha(0.0f);
                        //displayRoom = false;
                    }
                }
            }
        });


        bdirections.setOnClickListener(new View.OnClickListener(){
            public void onClick (View view){
                alertDialog.show();
            }
        });
      
    }


    /*public boolean onTouch(View view, MotionEvent event) {
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
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    lp.removeRule(RelativeLayout.CENTER_HORIZONTAL);
                    lp.removeRule(RelativeLayout.CENTER_VERTICAL);
                } else {
                    lp.addRule(RelativeLayout.CENTER_HORIZONTAL, 0);
                    lp.addRule(RelativeLayout.CENTER_VERTICAL, 0);
                }
                lp.leftMargin = X - _xDelta;
                lp.topMargin = Y - _yDelta;
                lp.rightMargin = view.getWidth() - lp.leftMargin - windowwidth;
                lp.bottomMargin = view.getHeight() - lp.topMargin - windowheight;
                view.setLayoutParams(lp);

                if(displayRoom){
                    View location = findViewById(R.id.location);
                    RelativeLayout.LayoutParams lp2 = (RelativeLayout.LayoutParams) location.getLayoutParams();
                    int roomIndex = getRoomIndex(searchBar.getText().toString());
                    int floorX = getFloorX(roomDatabase[roomIndex][2]);
                    int floorY = getFloorY(roomDatabase[roomIndex][2]);
                    lp2.leftMargin = floorX + Integer.parseInt(roomDatabase[roomIndex][4]);
                    lp2.topMargin = floorY + Integer.parseInt(roomDatabase[roomIndex][5]);
                    location.setLayoutParams(lp2);
                }
                break;
        }
        return true;
    }*/

    //Function that generates a simple list of instructions to arrive at your destination
    public ArrayList<String> generateInstructions (String start, String end) {
        ArrayList<String> instructions = new ArrayList<String>();

        int startIndex = getRoomIndex(start);
        int endIndex = getRoomIndex(end);

        if (startIndex == -1 || endIndex == -1) {
            instructions.add("target room does not exist");
            return instructions;
        }

        if (!roomDatabase[startIndex][1].equals(roomDatabase[endIndex][1]) & !roomDatabase[endIndex][1].equals("N/A")) {
            String size;

            switch (roomDatabase[endIndex][1]) {
                case "North":
                    size = "large";
                    break;
                case "South":
                    size = "small";
                    break;
                default:
                    size = "N/A";
            }

            instructions.add("Go to the " + roomDatabase[endIndex][1] + " (" + size + ") building.");
        }
        else if (roomDatabase[endIndex][1].equals("N/A")) {
            instructions.add("Exit the building.");
            instructions.add("Go to the parking lot");
        }

        if (!roomDatabase[endIndex][2].equals("N/A")) {
            instructions.add("Go to floor " + roomDatabase[endIndex][2]);
        }

        instructions.add("Go to Room " + roomDatabase[endIndex][0]);

        return instructions;
    }


    private boolean getDatabase(String[][] roomDatabase) {
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
                            roomDatabase[i][j] = receiveString;
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

    private int getFloorX(String floor){
        return (int)floorimage.getX();
        /*switch(floor){
            case "1":
                return (int)floorimage.getX();
            case "2":
                return (int)floor2north.getX();
            case "3":
                return (int)floor3north.getX();
            case "4":
                return (int)floor4north.getX();
            default:
                return (int)floor2north.getX();
        }*/
    }

    private int getFloorY(String floor){
        return (int)floorimage.getY();
        /*switch(floor){
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
        }*/
    }
}
