package com.example.myapplication;

import android.content.res.AssetManager;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class MainActivity extends AppCompatActivity {
    final int numRooms = 148;
    final int numDataFields = 4;

    ImageButton searchButton;
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
        searchButton = (ImageButton)findViewById(R.id.searchButton);
        FloatingActionButton fab = findViewById(R.id.fab);

        //Initialize text fields
        searchBar = (EditText)findViewById(R.id.searchBar);
        roomNumber = (TextView)findViewById(R.id.roomNumber);
        building= (TextView)findViewById(R.id.building);
        floor = (TextView)findViewById(R.id.floor);
        roomName = (TextView)findViewById(R.id.roomName);

        searchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){

                int roomIndex = getRoomIndex(searchBar.getText().toString());
                if(roomIndex != -1){
                    roomNumber.setText(roomDatabase[roomIndex][0]);
                    building.setText(roomDatabase[roomIndex][1]);
                    floor.setText(roomDatabase[roomIndex][2]);
                    roomName.setText(roomDatabase[roomIndex][3]);
                }
                else{
                    roomNumber.setText("Couldn't find a room with that name");
                    building.setText("");
                    floor.setText("");
                    roomName.setText("");
                }
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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
                StringBuilder stringBuilder = new StringBuilder();

                /*if((receiveString = bufferedReader.readLine()) != null ){
                    stringBuilder.append(receiveString);
                }*/

                for(int i = 0; i<numRooms; i++){
                    //Read in list number of room, but don't process the data.
                    bufferedReader.readLine();
                    for(int j = 0; j<numDataFields; j++){
                        if( (receiveString = bufferedReader.readLine()) != null ) {
                            //stringBuilder.append(receiveString);
                            //database[i][j] = stringBuilder.toString();
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
            if(n.equals(roomDatabase[j][0])){
                return j;
            }
        }
        return -1;
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
