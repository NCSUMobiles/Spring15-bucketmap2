package csc591.bucketlistraleigh.view;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import csc591.bucketlistraleigh.FoodFragment;
import csc591.bucketlistraleigh.R;

public class FoodActivity extends Activity implements FoodFragment.OnFragmentInteractionListener {

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


    }
}