package com.netsole;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.netsol.adapters.RealEstateBL;
import com.netsol.utiliy.Consts;
import com.netsole.eaapplication.R;


public class Activity_Splash_Screen extends Activity {

    RealEstateBL zsBL;
    ProgressDialog pd;
    ConnectionDetector cd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        zsBL = new RealEstateBL(this);
        cd = new ConnectionDetector(getApplicationContext());
        Intent i = new Intent(Activity_Splash_Screen.this, Activity_Login_Screen.class);//Activity_Login_Screen
        startActivity(i);
        finish();



    }




}
