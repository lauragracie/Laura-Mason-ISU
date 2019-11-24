package com.example.myapplication;

//Import Android libraries and widgets

//Import libraries for reading from files
import android.content.res.AssetManager;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

//Import libraries for layout elements (buttons, images, etc)
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

//Import libraries for pop-up dialog
import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;

//Import libraries for basic app functionality
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.content.Context;

//Import library to get events for keyboard and searchBar
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;

//Import library for Arrays
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{
    //Declare buttons
    Button bfloor1;
    Button bfloor2;
    Button bfloor3;
    Button bfloor4;
    Button bdirections;
    ImageButton searchButton;
    ImageButton clearButton;

    //Declare images for map and location marker
    ImageView floorimage;
    ImageView location;

    //Declare text input fields
    EditText searchBar;
    EditText aTo;
    EditText aFrom;

    TextView textDisplay;

    AssetManager assetManager;

    //Define format of database (146 rooms with 6 data fields to read in for each)
    final int numRooms = 146;
    final int numDataFields = 6;

    private ViewGroup rootlayout;

    boolean isRoomDisplay = false;

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

        bfloor1 = findViewById(R.id.bfloor1);
        bfloor2 = findViewById(R.id.bfloor2);
        bfloor3 = findViewById(R.id.bfloor3);
        bfloor4 = findViewById(R.id.bfloor4);
        bdirections = findViewById(R.id.bdirections);

        floorimage = findViewById(R.id.floorimage);

        //floorimage.setOnTouchListener(this);
        location = findViewById(R.id.location);

        //Initialize text fields
        searchBar = findViewById(R.id.searchBar);
        textDisplay = findViewById(R.id.roomNumber);


        rootlayout = (ViewGroup) findViewById(R.id.root);

        aTo = new EditText(this);
        aFrom = new EditText(this);

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        /*final EditText aFrom = new EditText(this);
        aFrom.setHint("From");
        layout.addView(aFrom);

        final EditText aTo = new EditText(this);
        aTo.setHint("To");
        layout.addView(aTo);*/

        aFrom.setHint("From");
        layout.addView(aFrom);

        aTo.setHint("To");
        layout.addView(aTo);

        builder.setView(layout);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //String from = aFrom.getText().toString();
                //String to = aTo.getText().toString();

                String from = aFrom.getText().toString();
                String to = aTo.getText().toString();

                int toRoomIndex = getRoomIndex(to);
                int fromRoomIndex = getRoomIndex(from);
                if(toRoomIndex != -1){
                    displayFloor(roomDatabase[toRoomIndex][2]);
                    displayRoom(to);
                    location.setAlpha(1.0f);
                }
                else{

                }
                instructions.clear();
                instructions = generateInstructions(from, to);

                textDisplay.setText("");
                for (int i = 0; i < instructions.size(); i++) {
                    textDisplay.append((i + 1) + ". ");
                    textDisplay.append(instructions.get(i));
                    textDisplay.append("\n");
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
                displayRoom(searchBar.getText().toString());
                closeKeyboard();
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                searchBar.getText().clear();
                location.setAlpha(0.0f);
                isRoomDisplay = false;
                closeKeyboard();
            }
        });

        rootlayout = (ViewGroup) findViewById(R.id.root);

        // Set up actions for all the buttons
        bfloor1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                switchFloor(1);
            }
        });

        bfloor2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                switchFloor(2);
            }
        });

        bfloor3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                switchFloor(3);
            }
        });

        bfloor4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                switchFloor(4);
            }
        });

        bdirections.setOnClickListener(new View.OnClickListener(){
            public void onClick (View view){
                alertDialog.show();
            }
        });
    }

    public void displayRoom(String room){
        int roomIndex = getRoomIndex(room);
        if (roomIndex != -1) {
            displayFloor(roomDatabase[roomIndex][2]);

            textDisplay.setText("");
            location.setAlpha(1.0f);
            isRoomDisplay = true;

            View location = findViewById(R.id.location);
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) location.getLayoutParams();
            int floorX = getFloorX();
            int floorY = getFloorY();
            lp.leftMargin = floorX + (int)(1.57*Integer.parseInt(roomDatabase[roomIndex][4]));
            lp.topMargin = floorY + (int)(1.50*Integer.parseInt(roomDatabase[roomIndex][5]));
            location.setLayoutParams(lp);

        } else {
            location.setAlpha(0.0f);
            isRoomDisplay = false;
            textDisplay.setText("Couldn't find a room with that name \n\n");
        }
    }


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

    private int getFloorX(){
        return (int)floorimage.getX();
    }

    private int getFloorY(){
        return (int)floorimage.getY();
    }

    public void switchFloor(int floor){
        floorimage.setAlpha(1.0f);
        switch(floor){
            case 1:
                floorimage.setImageResource(R.drawable.floor1);
                break;
            case 2:
                floorimage.setImageResource(R.drawable.floor2);
                break;
            case 3:
                floorimage.setImageResource(R.drawable.floor3);
                break;
            case 4:
                floorimage.setImageResource(R.drawable.floor4);
                break;
            default:
                floorimage.setImageResource(R.drawable.floor2);
                break;
        }
        if(isRoomDisplay){
            int roomIndex = getRoomIndex(searchBar.getText().toString());
            if(getRoomIndex(searchBar.getText().toString()) == -1 ){
                   roomIndex = getRoomIndex(aTo.getText().toString());
            }
            if(roomDatabase[roomIndex][2].compareTo(Integer.toString(floor)) == 0){
                location.setAlpha(1.0f);
                isRoomDisplay = true;
            }
            else{
                location.setAlpha(0.0f);
            }
        }
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
