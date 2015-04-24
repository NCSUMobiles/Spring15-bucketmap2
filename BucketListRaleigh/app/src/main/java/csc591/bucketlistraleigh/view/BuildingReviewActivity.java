package csc591.bucketlistraleigh.view;


import android.app.Activity;
import android.os.Bundle;

import csc591.bucketlistraleigh.R;

/**
 * Created by Nithya Pari on 4/20/2015.
 */
public class BuildingReviewActivity extends Activity {

    //CreateDB db = new CreateDB(this);

   // private ListView lv;
    //We will be using getBuildingReviews method in CreateDB.java
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.building_review);

      /*  lv = (ListView) findViewById(R.id.listView_building_review);

        // Instanciating an array list (you don't need to do this,
        // you already have yours).
        List<String> reviewsArrayList = new ArrayList<String>();
        reviewsArrayList.add("Review 1 - Wonderful Eatery!");
        reviewsArrayList.add("Review 2 - Good for family dinner");
        reviewsArrayList.add("Review 3 - I liked the dessert section the most");

        // This is the array adapter, it takes the context of the activity as a
        // first parameter, the type of list view as a second parameter and your
        // array as a third parameter.
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                reviewsArrayList );

        lv.setAdapter(arrayAdapter);*/

    }
}
