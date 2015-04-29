package csc591.bucketlistraleigh.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import csc591.bucketlistraleigh.R;

public class BuildingImageActivity extends Activity {

    private int[] IMAGES = { R.drawable.rtb1, R.drawable.rtb2 };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.building_image_gallery);

        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        ImageAdapter adapter = new ImageAdapter();
        viewPager.setAdapter(adapter);

        //Extracting building ID and building Name
        final String buildingName = getIntent().getExtras().getString("buildingName");
        final String buildingID = getIntent().getExtras().getString("buildingID");

        //Setting Building Name dynamically
        TextView textOut = (TextView) findViewById(R.id.buildingName);
        textOut.setText(buildingName);

        //Creating an Intent for 'Back to Map' button
        final Button backToMapButton = (Button) findViewById(R.id.backToMapButton);
        backToMapButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent backToMapIntent = new Intent(v.getContext(), HomeActivity.class);
                startActivity(backToMapIntent);
            }
        });

        //Creating an Intent for 'Review' button
        final Button reviewButton = (Button) findViewById(R.id.reviewButton);
        reviewButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent reviewIntent = new Intent(v.getContext(), BuildingReviewActivity.class);
                reviewIntent.putExtra("buildingID",buildingID);
                reviewIntent.putExtra("buildingName",buildingName);
                startActivity(reviewIntent);
            }
        });

    }


    class ImageAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return IMAGES.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((ImageView) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(getApplicationContext());
            imageView.setImageResource(IMAGES[position]);
            ((ViewPager) container).addView(imageView, 0);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((ImageView) object);
        }
    }
}
