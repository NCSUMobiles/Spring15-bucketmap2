package csc591.bucketlistraleigh.view;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;


import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;


import java.io.File;
import java.util.Arrays;
import csc591.bucketlistraleigh.R;



public class AuthActivity extends Activity {

    //Initializing Variables
    private EditText txtusername = null;
    private EditText txtpassword = null;
    private Button btnLogin = null;
    private Button btnSignUp = null;
    private ImageButton btnFb = null;
    private boolean isResumed = false;
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_auth);
        callbackManager = CallbackManager.Factory.create();

        btnFb = (ImageButton) findViewById(R.id.facebook_btn);

        btnFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                facebookLogin();
            }
        });


        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(getApplicationContext(),"Login Successfull", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AuthActivity.this,HomeActivity.class);
                AuthActivity.this.startActivity(intent);
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(),"Login Cancelled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException e) {
                Toast.makeText(getApplicationContext(),"Login Error", Toast.LENGTH_SHORT).show();
            }
        });


        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                if(isResumed){
                    if(currentAccessToken!=null){

                        Log.i("Main Activity","In If condition");
                        AccessToken token = AccessToken.getCurrentAccessToken();
                        //Intent intent = new Intent(MainActivity.this, FBSuccessActivity.class);
                        // startActivity(intent);
                    }
                    else{
                        Log.i("Main Activity","In Else condition");
                    }
                }

            }
        };
        //Get the username and password from the View
        txtusername = (EditText) findViewById(R.id.username);
        txtpassword = (EditText) findViewById(R.id.userpassword);

        btnLogin = (Button) findViewById(R.id.btn_sign_in);
        btnSignUp = (Button) findViewById(R.id.btn_sign_up);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        //SignUp Button's Click Listener
        btnSignUp.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Get username and password
                        String username = txtusername.getText().toString();
                        String password = txtpassword.getText().toString();
                        int flag = 0;
                        File path = getApplication().getDatabasePath("bucketlistraleigh.db");
                        SQLiteDatabase db = SQLiteDatabase.openDatabase(path.getPath(), null,0);
                        try {
                            Cursor curLogin = db.rawQuery("SELECT username from login", null);
                            if (curLogin.moveToFirst()) {
                                do {
                                    String data = curLogin.getString(curLogin.getColumnIndex("username"));
                                    Log.i("data",data);
                                    if(data.equals(username))
                                    {
                                        txtusername.setText("Username Already Exists. Please try again");
                                        flag = 1;
                                    }
                                } while (curLogin.moveToNext());
                                if(flag == 0)
                                {
                                    db.execSQL("INSERT INTO login VALUES('blr5',"+"'"+username+"'"+","+"'"+password+"'"+");");
                                }
                            }
                            curLogin.close();
                        }catch (Exception e){
                            Log.i("Exception-logdatabase()",  e.toString());
                        }
                        //This part is only for checking if the new username and password are getting inserted or not
                        try {
                            Cursor curLogin = db.rawQuery("SELECT username from login", null);
                            if (curLogin.moveToFirst()) {
                                do {
                                    String data = curLogin.getString(curLogin.getColumnIndex("username"));
                                    Log.i("login table-username", data+" ");
                                } while (curLogin.moveToNext());
                            }
                            curLogin.close();
                        }catch (Exception e){
                            Log.i("Exception-logdatabase()",  e.toString());
                        }
                    }
                });
        //Login button's onClickListener
        btnLogin.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Get username and password
                        String username = txtusername.getText().toString();
                        String password = txtpassword.getText().toString();
                        int flag = 0;
                        File path = getApplication().getDatabasePath("bucketlistraleigh.db");
                        SQLiteDatabase db = SQLiteDatabase.openDatabase(path.getPath(), null,0);
                        try {
                            Cursor curLogin = db.rawQuery("SELECT username,password from login", null);
                            if (curLogin.moveToFirst()) {
                                do {
                                    String data = curLogin.getString(curLogin.getColumnIndex("username"));
                                    String data1 = curLogin.getString(curLogin.getColumnIndex("password"));
                                    Log.i("username",data);
                                    Log.i("password",data1);
                                    if(data.equals(username))
                                    {
                                        flag = 1;
                                        if(data1.equals(password))
                                        {
                                            Intent mainActivityIntent = new Intent(AuthActivity.this,HomeActivity.class);
                                            AuthActivity.this.startActivity(mainActivityIntent);
                                        }
                                        else
                                        {
                                            txtusername.setText("Invalid Password. Please try again");
                                        }
                                    }
                                } while (curLogin.moveToNext());
                                if(flag == 0)
                                {
                                    txtusername.setText("Invalid Username. Please try again");
                                }
                            }
                            curLogin.close();
                        }catch (Exception e){
                            Log.i("Exception-logdatabase()",  e.toString());
                        }
                    }
                });
    }
    public void facebookLogin(){
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
    }
    //Lifecycle methods

    @Override
    protected void onPause(){
        super.onPause();
        isResumed = false;

        // Call the 'deactivateApp' method to log an app event for use in analytics and advertising
        // reporting.  Do so in the onPause methods of the primary Activities that an app may be
        // launched into.
        AppEventsLogger.deactivateApp(this);

    }

    @Override
    protected void onResume(){
        super.onResume();
        isResumed = true;
        // Call the 'activateApp' method to log an app event for use in analytics and advertising
        // reporting.  Do so in the onResume methods of the primary Activities that an app may be
        // launched into.
        AppEventsLogger.activateApp(this);
    }
/*
    @Override
    protected void onRestart(){

    }
    */

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        accessTokenTracker.stopTracking();
    }


    @Override
    public void onActivityResult(int requestCode,int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,resultCode,data);
    }

}