package csc591.bucketlistraleigh.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import csc591.bucketlistraleigh.fragments.FoodFragment;
import csc591.bucketlistraleigh.R;
import csc591.bucketlistraleigh.helper.Building;

public class FoodActivity extends Activity implements FoodFragment.OnFragmentInteractionListener {
    ImageButton draw_btn = null;
    Building building = Building.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);


        //Load an image with all the food places highlighted
        //Bitmap region decoder that shows all the places with food.

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new FoodFragment()).commit();


        }

        draw_btn = (ImageButton) findViewById(R.id.draw_btn1);
        draw_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FingerPaintActivity.class);
               // intent.putExtra("buildingID",building.getBuildingId());
               // intent.putExtra("buildingName",building.getBuildingName());
                startActivity(intent);
            }
        });





    }
}