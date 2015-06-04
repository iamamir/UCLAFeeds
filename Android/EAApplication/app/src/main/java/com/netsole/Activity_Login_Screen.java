package com.netsole;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.netsol.utiliy.Consts;
import com.netsol.utiliy.Utility;
import com.netsole.eaapplication.R;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import com.netsol.adapters.RealEstateBL;


public class Activity_Login_Screen extends Activity implements OnKeyListener {
    RealEstateBL zsBL;
    public Button btnLogin, btnNext;
    EditText editTextEmail, editTextPassword;
    ImageView backgraoudImg;
    Button btnRegister, newUserBtn, signinBtn;
    ProgressDialog pd;
    SharedPreferences pref;
    ConnectionDetector cd;
    String token = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__login_screen);

        zsBL = new RealEstateBL(this);
        pref = getSharedPreferences("AppPref", MODE_PRIVATE);
        btnLogin = (Button) findViewById(R.id.activity_login_btn_login);
        btnNext = (Button) findViewById(R.id.btn_next);
        signinBtn = (Button) findViewById(R.id.btn_signin);
        newUserBtn = (Button) findViewById(R.id.btn_new_user);
        backgraoudImg = (ImageView)findViewById(R.id.activity_login_img_background);
        btnRegister = (Button) findViewById(R.id.btn_register);
        editTextEmail = (EditText) findViewById(R.id.activity_login_screen_editText_email);
        editTextPassword = (EditText) findViewById(R.id.activity_login_screen_editText_password);
        btnLogin.setVisibility(View.VISIBLE);


        btnLogin.setOnClickListener(onLoginButtonClicked);
        btnNext.setOnClickListener(onNextButtonClicked);

        newUserBtn.setOnClickListener(onNewUserClicked);
        signinBtn.setOnClickListener(onSigninBtnClicked);

        btnRegister.setOnClickListener(onRegisterButtonClicked);
        cd = new ConnectionDetector(getApplicationContext());
        btnNext.setVisibility(View.INVISIBLE);
        pref = getSharedPreferences("AppPref", MODE_PRIVATE);

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "testapplicationandroid.example.com.activity",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
        onSignINUserClicked();

    }

    private void onNewUserClicked() {
        newUserBtn.setBackgroundColor(getResources().getColor(R.color.Black));
        signinBtn.setBackgroundColor(getResources().getColor(R.color.Transparent));
        btnRegister.setVisibility(View.VISIBLE);
        btnLogin.setVisibility(View.INVISIBLE);
        editTextPassword.setVisibility(View.GONE);
        editTextEmail.setText("");
        editTextPassword.setText("");
    }

    private void onSignINUserClicked() {
        newUserBtn.setBackgroundColor(getResources().getColor(R.color.Transparent));
        signinBtn.setBackgroundColor(getResources().getColor(R.color.Black));
        btnRegister.setVisibility(View.GONE);
        btnLogin.setVisibility(View.VISIBLE);
        editTextPassword.setVisibility(View.VISIBLE);
        editTextEmail.setText("");
        editTextPassword.setText("");
    }


    @Override
    protected void onResume() {

        super.onResume();
        //Utility.backgroundTranslation(backgraoudImg);
        String token =  pref.getString("token", "");
       if (token.length()>0)
        {
            btnNext.setVisibility(View.VISIBLE);
        }
        else{
            btnNext.setVisibility(View.INVISIBLE);
       }

    }

    void performLogin() {
        new LoginTask().execute();

    }

    private OnClickListener onLoginButtonClicked = new OnClickListener() {

        @Override
        public void onClick(View v) {
//            btnRegister.setBackgroundColor(getResources().getColor(R.color.button_material_dark));
//            btnLogin.setBackgroundColor(getResources().getColor(R.color.));
            performLogin();

        }

    };
    private OnClickListener onNextButtonClicked = new OnClickListener() {

        @Override
        public void onClick(View v) {

            Intent i = new Intent(Activity_Login_Screen.this,
                    MainActivity.class);
            startActivity(i);
            overridePendingTransition(R.anim.slide_in_right,
                    R.anim.slide_out_left);
        }
    };
    private OnClickListener onSigninBtnClicked = new OnClickListener() {

        @Override
        public void onClick(View v) {
            onSignINUserClicked();

        }
    };
    private OnClickListener onNewUserClicked = new OnClickListener() {

        @Override
        public void onClick(View v) {
            onNewUserClicked();
        }
    };

    private OnClickListener onRegisterButtonClicked = new OnClickListener() {

        @Override
        public void onClick(View v) {

            new RegisterTask().execute();
        }
    };

    class RegisterTask extends AsyncTask<Void, Void, Void> {

        String response = "";
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(Activity_Login_Screen.this);
            pd.setMessage("Authenticating email address from our servers....");
            pd.setIndeterminate(false);
            pd.setCancelable(true);
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            if (cd.isConnectingToInternet()) {
//editTextEmail.getText().toString()
                response = zsBL.RegisterAPI(editTextEmail.getText().toString());

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (response != null) {
                if (response.equalsIgnoreCase("success")) {
                    if (pd != null) {
                        pd.cancel();
                    }
                    Toast.makeText(Activity_Login_Screen.this, "Account created.Kindly check your email to login in app", Toast.LENGTH_LONG).show();

                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Activity_Login_Screen.this);
                    builder.setMessage("Registration Failed.This email doesnot exists in our servers.")
                            .setCancelable(false)
                            .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent i = new Intent(Activity_Login_Screen.this,
                                            Activity_Login_Screen.class);
                                    startActivity(i);
                                    finish();

                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();

                    if (pd != null) {
                        pd.cancel();
                    }

                }

            }

        }
    }

    class Authenticate101 extends AsyncTask<Void, Void, Void> {
        ProgressDialog pDialog;
        String mEmail;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Activity_Login_Screen.this);
            pDialog.setMessage("Authenticating from Gmail server....");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            mEmail =  pref.getString("Email", "");
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            try {


                GoogleAuthUtil.invalidateToken(Activity_Login_Screen.this, pref.getString("Access Token", ""));
                token = GoogleAuthUtil.getToken(
                        Activity_Login_Screen.this,
                        mEmail,
                        "oauth2:https://www.googleapis.com/auth/gmail.readonly");
                SharedPreferences.Editor edit = pref.edit();
                edit.putString("token",token);
                edit.commit();
            } catch (IOException transientEx) {
                // Network or server error, try later
                Log.e("IOException", transientEx.toString());
            } catch (UserRecoverableAuthException e) {
                // Recover (with e.getIntent())
                startActivityForResult(e.getIntent(), 1001);

                Log.e("AuthException", e.toString());

            } catch (GoogleAuthException authEx) {
                // The call is not ever expected to succeed
                // assuming you have already verified that
                // Google Play services is installed.
                Log.e("GoogleAuthException", authEx.toString());
                new showMessageAsync().execute();
            }


            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            pDialog.dismiss();
            if (token != null) {
                SharedPreferences.Editor edit = pref.edit();

                edit.putString("Access Token", token);
                edit.commit();
                Log.i("Token", "Access Token retrieved:" + token);


               // Toast.makeText(getApplicationContext(), "Access Token is " + token, Toast.LENGTH_SHORT).show();


                Consts.credential = new GoogleCredential().setAccessToken(token);
                btnNext.setVisibility(View.VISIBLE);
            }

        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            new AlertDialog.Builder(Activity_Login_Screen.this)
                    .setIcon(R.drawable.ic_launcher)
                    .setTitle("Quit EA")
                    .setMessage(
                            "Do you really want to exit the app?")
                    .setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {

                                    finish();
                                }

                            }).setNegativeButton("No", null).show();
            return false;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
    }

    class LoginTask extends AsyncTask<Void, Void, Void> {

        String response = "";
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(Activity_Login_Screen.this);
            pd.setMessage("Authenticating email address from our servers....");
            pd.setIndeterminate(false);
            pd.setCancelable(true);
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            if (cd.isConnectingToInternet()) {
//editTextEmail.getText().toString(), editTextPassword.getText().toString()
                response = zsBL.logInAPI(editTextEmail.getText().toString(), editTextPassword.getText().toString());
                SharedPreferences.Editor edit = pref.edit();
                //Storing Data using SharedPreferences
                edit.putString("Email",editTextEmail.getText().toString());
                edit.commit();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (response != null) {
                if (response.equalsIgnoreCase("success")) {
                    if (pd != null) {
                        pd.cancel();
                    }
                    editTextEmail.setText("");
                    editTextPassword.setText("");
                    handleLoginResponse("success");


                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Activity_Login_Screen.this);
                    builder.setMessage("Login failed.Please try again.")
                            .setCancelable(false)
                            .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent i = new Intent(Activity_Login_Screen.this,
                                            Activity_Login_Screen.class);
                                    startActivity(i);
                                    finish();

                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();

                    if (pd != null) {
                        pd.cancel();
                    }

                }

            }

        }
    }


    private class showMessageAsync extends AsyncTask<Void, Void, String> {
        AlertDialog.Builder builder;

        @Override
        protected String doInBackground(Void... params) {
            return null;
        }

        protected void onPreExecute() {
            super.onPreExecute();
            builder = new AlertDialog.Builder(Activity_Login_Screen.this);

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            builder.setMessage("Please go to setting and sync this account")
                    .setCancelable(false)
                    .setPositiveButton("Go to Settings", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent startMain = new Intent(Settings.ACTION_ADD_ACCOUNT);
                            //startMain.addCategory(Intent);
                            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(startMain);

                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }

    }
    private void handleLoginResponse(String loginResult) {

        if (loginResult.contains("success")) {


            new Authenticate101().execute();

        } else {
            new AlertDialog.Builder(this).setTitle("Login Failed").setMessage("Username or Password is Invalid. Please try again ").setIcon(R.drawable.error_icon).setNeutralButton("Ok", null).show();

        }

    }

    private void handleRegisterResponse(String result) {
        if (result.equals("success")) {


            FileOutputStream fOut = null;

            OutputStreamWriter osw = null;

            try {

                fOut = openFileOutput("loginstate.dat", Context.MODE_PRIVATE);

                osw = new OutputStreamWriter(fOut);
                osw.close();
                fOut.close();

            } catch (Exception e) {

                e.printStackTrace(System.err);

            }

        } else {
            new AlertDialog.Builder(this).setTitle("Registration Failed")
                    .setMessage(result).setIcon(R.drawable.error_icon)
                    .setNeutralButton("Ok", null).show();

        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            performLogin();
        }
        return false;
    }

    private String getUserNameString(String name) {
        String userName = name.toLowerCase();
        return userName.substring(0, 1).toUpperCase() + userName.substring(1, userName.length()).toLowerCase();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences.Editor edit = pref.edit();
        edit.putString("token","");
        edit.commit();
    }
}
