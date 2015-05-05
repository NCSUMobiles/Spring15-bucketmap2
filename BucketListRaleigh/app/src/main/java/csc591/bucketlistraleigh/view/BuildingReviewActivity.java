package csc591.bucketlistraleigh.view;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import csc591.bucketlistraleigh.R;
import csc591.bucketlistraleigh.database.CreateDB;

public class BuildingReviewActivity extends Activity {

    private ListView lv;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.building_review);

        //Fetching the list view to be populated
        lv = (ListView) findViewById(R.id.listView_building_review);

        //Getting the building name from the intent
        final String buildingName = getIntent().getExtras().getString("buildingName");
        final String buildingID = getIntent().getExtras().getString("buildingID");

        //Setting Building Name dynamically

        TextView textOut = (TextView) findViewById(R.id.buildingDescription);
        textOut.setText(buildingName);
        // Retrieving the cursor to the review contents from the database
        Cursor reviewCursor = new CreateDB(this).getBuildingReviewData(buildingID);
        ReviewCursorAdapter reviewCursorAdapter = new ReviewCursorAdapter(this, reviewCursor);

        // setting the cursor adapter
        lv.setAdapter(reviewCursorAdapter);

        //Creating an Intent for 'Back to Map' button
        Button backToMapButton = (Button) findViewById(R.id.backToMapButton);
        backToMapButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent backToMapIntent = new Intent(v.getContext(), HomeActivity.class);
                startActivity(backToMapIntent);
            }
        });

        //Creating an Intent for 'Images' button
        Button ImagesButton = (Button) findViewById(R.id.imageButton);
        ImagesButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent reviewIntent = new Intent(v.getContext(), BuildingImageActivity.class);
                reviewIntent.putExtra("buildingName",buildingName);
                reviewIntent.putExtra("buildingID",buildingID);
                startActivity(reviewIntent);
            }
        });




    }
}