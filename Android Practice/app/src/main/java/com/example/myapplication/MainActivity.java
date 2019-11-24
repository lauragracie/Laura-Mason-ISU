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

    boolean isRoomDisplay = false;

    //Initialize array to store room database and vector to store instructions
    String[][] roomDatabase = new String[numRooms][numDataFields];
    ArrayList<String> instructions = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Read in database from text file
        getDatabase(roomDatabase);

        //Initialize buttons
        searchButton = findViewById(R.id.searchButton);
        clearButton = findViewById(R.id.clearButton);
        bfloor1 = findViewById(R.id.bfloor1);
        bfloor2 = findViewById(R.id.bfloor2);
        bfloor3 = findViewById(R.id.bfloor3);
        bfloor4 = findViewById(R.id.bfloor4);
        bdirections = findViewById(R.id.bdirections);

        //Initialize images
        floorimage = findViewById(R.id.floorimage);
        location = findViewById(R.id.location);

        //Initialize text display
        textDisplay = findViewById(R.id.textDisplay);

        //Initialize text fields
        searchBar = findViewById(R.id.searchBar);
        aTo = new EditText(this);
        aFrom = new EditText(this);

        //Create new dialog for getting directions between rooms
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        //Setup search bars
        aFrom.setHint("From");
        layout.addView(aFrom);
        aTo.setHint("To");
        layout.addView(aTo);

        builder.setView(layout);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //When the "Ok" button is pressed, read in the text from the search bars
                String from = aFrom.getText().toString();
                String to = aTo.getText().toString();

                //Display the final destination on the map
                int toRoomIndex = getRoomIndex(to);
                if(toRoomIndex != -1){
                    displayFloor(roomDatabase[toRoomIndex][2]);
                    displayRoom(to);
                    location.setAlpha(1.0f);
                }

                //Generate instructions between the two locations
                instructions.clear();
                instructions = generateInstructions(from, to);

                //Display numbered list of instructions to the screen
                textDisplay.setText("");
                for (int i = 0; i < instructions.size(); i++) {
                    textDisplay.append((i + 1) + ". ");
                    textDisplay.append(instructions.get(i));
                    textDisplay.append("\n");
                }

                dialog.dismiss();
            }
        });
        //When the cancel button is pressed, close the dialog
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        final AlertDialog alertDialog = builder.create();

        //Set margins and dimensions for the floor map
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) floorimage.getLayoutParams();
        lp.topMargin = 100;
        lp.bottomMargin = 200;
        lp.rightMargin = 200;
        lp.width = 1696;
        lp.height = 1180;
        floorimage.setLayoutParams(lp);

        //When the user presses enter in the search bar, toggle the search button
        searchBar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    searchButton.performClick();
                }
                return false;
            }
        });

        //When the search button is pressed, display the room and close the keyboard
        searchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                displayRoom(searchBar.getText().toString());
                closeKeyboard();
            }
        });

        //When the clear button is pressed, clear the text input and the location marker and close the keyboard
        clearButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                searchBar.getText().clear();
                location.setAlpha(0.0f);
                isRoomDisplay = false;
                closeKeyboard();
            }
        });


        //When a floor button is pressed, switch to the respective floor
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


        //When the directions button is pressed, bring up the dialog to get directions
        bdirections.setOnClickListener(new View.OnClickListener(){
            public void onClick (View view){
                alertDialog.show();
            }
        });
    }


    //Display a room on a map using the location marker based on a room name or number
    public void displayRoom(String room){
        int roomIndex = getRoomIndex(room);

        if (roomIndex != -1) {
            //If the room is in the database, switch to the floor that that room is on and display location marker
            displayFloor(roomDatabase[roomIndex][2]);
            textDisplay.setText("");
            location.setAlpha(1.0f);
            isRoomDisplay = true;

            //Move location marker to the position of the room on the map
            View location = findViewById(R.id.location);
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) location.getLayoutParams();
            int floorX = getFloorX();
            int floorY = getFloorY();
            //Position on the map is defined by the x and y values for that room in the database
            //relative to the position of the floor map.
            lp.leftMargin = floorX + (int)(1.57*Integer.parseInt(roomDatabase[roomIndex][4]));
            lp.topMargin = floorY + (int)(1.50*Integer.parseInt(roomDatabase[roomIndex][5]));
            location.setLayoutParams(lp);

        } else {
            //If the room isn't in the database, display error message and clear location marker
            location.setAlpha(0.0f);
            isRoomDisplay = false;
            textDisplay.setText("Couldn't find a room with that name \n\n");
        }
    }


    //Function that generates a simple list of instructions to arrive at your destination
    //Takes in room name or number for start and end locations
    public ArrayList<String> generateInstructions (String start, String end) {
        ArrayList<String> instructions = new ArrayList<String>();

        int startIndex = getRoomIndex(start);
        int endIndex = getRoomIndex(end);

        //Display error message if one of the rooms isn't in the database
        if (startIndex == -1 || endIndex == -1) {
            instructions.add("target room does not exist");
            return instructions;
        }

        //If the rooms are in different building and the end location isn't a portable (portable floor = "N/A")
        //display instructions to change buildings
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
        //If destination is a portable, add instructions to go to the parking cage
        else if (roomDatabase[endIndex][1].equals("N/A")) {
            instructions.add("Exit the building.");
            instructions.add("Go to the parking lot.");
        }

        //If the destination isn't a portable, add instruction on which floor to go to
        if (!roomDatabase[endIndex][1].equals("N/A")) {
            instructions.add("Go to floor " + roomDatabase[endIndex][2]);
        }

        //Add instruction to proceed to the room
        //If the room has a name (ex: Cafeteria, Main Office, etc.), include it in the instruction
        if (!roomDatabase[endIndex][3].equals("N/A")){
            instructions.add("Go to Room " + roomDatabase[endIndex][0] + " (" + roomDatabase[endIndex][3] + ").");
        }
        else {
            instructions.add("Go to Room " + roomDatabase[endIndex][0] + ".");
        }

        return instructions;
    }

    //Read data into the roomDatabase array from the text file
    private boolean getDatabase(String[][] roomDatabase) {
        assetManager = getAssets();
        try {
            InputStream inputStream = assetManager.open("RoomDatabase.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";

                for(int i = 0; i<numRooms; i++){
                    //Read in first line (list number of room) but don't process the data.
                    bufferedReader.readLine();
                    //Read in the data fields from the file for each room.
                    //0 = room number, 1 = building, 2 = floor, 3 = name, 4 = xPos, 5 = yPos
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
        //Catch exceptions and log errors
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
            return false;
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
            return false;
        }
    }

    //Given a name or number for a room, return index of that room in the database
    private int getRoomIndex(String n){
        for(int j = 0; j < numRooms; j++){
            if(n.compareToIgnoreCase(roomDatabase[j][0]) == 0|| n.compareToIgnoreCase(roomDatabase[j][3]) == 0){
                //Ignore cases when n is "N/A", which is the default room name value
                if(!n.equals("N/A")){
                    return j;
                }
            }
        }
        return -1;
    }

    //Given a floor, toggle the corresponding floor button
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

    //Return X value for the floor map
    private int getFloorX(){
        return (int)floorimage.getX();
    }

    //Return Y value for the floor map
    private int getFloorY(){
        return (int)floorimage.getY();
    }

    //Given a floor number, display the map for that floor
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
            //If the search bar is empty, get the room name/number from the "To:" search bar
            if(getRoomIndex(searchBar.getText().toString()) == -1 ){
                roomIndex = getRoomIndex(aTo.getText().toString());
            }
            //If the floor that the room is on is being displayed, show the location marker
            if(roomDatabase[roomIndex][2].compareTo(Integer.toString(floor)) == 0){
                location.setAlpha(1.0f);
                isRoomDisplay = true;
            }
            //If the floor isn't being displayed, don't show the location marker
            else{
                location.setAlpha(0.0f);
            }
        }
    }

    //Close the keyboard
    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}