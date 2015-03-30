package csc591.bucketlistraleigh.view;

import android.content.Context;
import android.util.Log;
import android.webkit.WebView;

public class GifWebView extends WebView {

    public GifWebView(Context context, String path){
        super(context);
        loadUrl(path);
    }

}
