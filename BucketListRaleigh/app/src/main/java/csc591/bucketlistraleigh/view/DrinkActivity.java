package csc591.bucketlistraleigh.view;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import csc591.bucketlistraleigh.fragments.DrinkFragment;
import csc591.bucketlistraleigh.R;

public class DrinkActivity extends Activity implements DrinkFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink);


        //Load an image with all the food places highlighted
        //Bitmap region decoder that shows all the places with food.

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new DrinkFragment()).commit();


        }


    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Runtime.getRuntime().gc();
        Log.i("Destroy", "Food activity destroy");

    }
}