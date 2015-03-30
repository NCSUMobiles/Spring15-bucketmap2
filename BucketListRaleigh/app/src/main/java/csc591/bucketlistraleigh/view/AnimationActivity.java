package csc591.bucketlistraleigh.view;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.io.IOException;
import java.io.InputStream;

import csc591.bucketlistraleigh.R;

public class AnimationActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InputStream stream = null;

        try{
            stream = getAssets().open("bucket_list_intro.gif");
        }
        catch (IOException e){
            e.printStackTrace();
        }

        //GifMoviewView view = new GifMoviewView(this,stream);
        GifWebView view = new GifWebView(this,"file:///android_asset/intro.html");
        setContentView(view);
    }
}
