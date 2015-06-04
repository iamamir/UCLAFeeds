package com.netsole;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.netsol.Models.UserModel;
import com.netsol.adapters.RealEstateBL;
import com.netsol.utiliy.Consts;
import com.netsol.utiliy.Utility;
import com.netsole.eaapplication.R;

import org.w3c.dom.Text;

import java.util.Random;

/**
 * Created by kamranw on 5/14/2015.
 */
public class Activity_Detail_Email extends Activity{
    TextView tvEmailBody;
    TextView tvSubject;
    TextView tvdate;
    TextView mUserNameImage;
    ImageButton btnFav;
    RealEstateBL zsBL;
    UserModel mSelectedUserModel;
    String position;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_email_screen);

        zsBL = new RealEstateBL(this);
        tvEmailBody = (TextView)findViewById(R.id.activity_detail_email_screen_detail_text_tv);
        tvSubject = (TextView)findViewById(R.id.activity_detail_email_screen_tv_subject);
        tvdate = (TextView)findViewById(R.id.activity_detail_email_screen_tv_date_time);
        mUserNameImage = (TextView)findViewById(R.id.design_layout_user_image);
        btnFav = (ImageButton)findViewById(R.id.imgBtnFav);


        Intent i = getIntent();
        mSelectedUserModel = (UserModel) i.getParcelableExtra("UserInfoList");
        position = i.getStringExtra("position");
        int colorInt = Utility.resourceNameToId(Activity_Detail_Email.this, mSelectedUserModel.colorCode, "color");
        GradientDrawable gradientDrawable = (GradientDrawable)mUserNameImage.getBackground();
        gradientDrawable.setColor(getResources().getColor(colorInt));
        btnFav.setOnClickListener(onfavButtonClicked);
        tvEmailBody.setText(mSelectedUserModel.message);
        String date[]  = mSelectedUserModel.messagedataTime.split("-");
        tvdate.setText(date[0]);
        tvSubject.setText(mSelectedUserModel.subject);
        tvEmailBody.setMovementMethod(new ScrollingMovementMethod());
        char a1 = Utility.getFirstCharacter(mSelectedUserModel.subject, 0);

        mUserNameImage.setText(a1 +"");
        btnFav.setBackgroundResource(R.drawable.fav_icon1);
        if(mSelectedUserModel.isFavourite.equalsIgnoreCase("true"))
        {
           btnFav.setImageResource(R.drawable.fav_icon);
        }
        else{
            btnFav.setImageResource(R.drawable.fav_icon1);

        }

    }
    private View.OnClickListener onfavButtonClicked = new View.OnClickListener() {

        @Override
        public void onClick(View v) {



            if(mSelectedUserModel.isFavourite.equalsIgnoreCase("true"))
            {
                boolean flag = Consts.favMessageIDList.remove(mSelectedUserModel.messageID+":"+mSelectedUserModel.isFavourite+":"+mSelectedUserModel.isRead);
                boolean flag1 = Consts.favUserInfoList.remove(mSelectedUserModel);
                mSelectedUserModel.isFavourite = "false";
                btnFav.setImageResource(R.drawable.fav_icon1);

            }
            else{
                mSelectedUserModel.isFavourite = "true";
                btnFav.setImageResource(R.drawable.fav_icon);
                Consts.favMessageIDList.add(mSelectedUserModel.messageID+":"+mSelectedUserModel.isFavourite+":"+mSelectedUserModel.isRead);
                Consts.favUserInfoList.add(mSelectedUserModel);
            }

            new PostMessageTask().execute(mSelectedUserModel);


        }};
    @Override
    public void onBackPressed() {
        Intent i = new Intent(Activity_Detail_Email.this,
                MainActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_right,
                R.anim.slide_out_left);
        finish();
    }
    class PostMessageTask extends AsyncTask<UserModel, Void, Void>
    {

        String response = "";
        ProgressDialog pd;
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            pd = new ProgressDialog(Activity_Detail_Email.this);
            pd.setMessage("Posting Favourite....");
            pd.setIndeterminate(false);
            pd.setCancelable(true);
        }

        @Override
        protected Void doInBackground(UserModel... arg0)
        {

            response = zsBL.postingFavouriteAPI(arg0[0].messageID,arg0[0].isFavourite,arg0[0].isRead);

            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            super.onPostExecute(result);
            if (pd != null)
            {
                pd.cancel();
            }
            if (response != null)
            {
                if (response.equalsIgnoreCase("success"))
                {




                }
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Activity_Detail_Email.this);
                    builder.setMessage("Favourite failed to save.Please try again.")
                            .setCancelable(false)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id)
                                {

                                   finish();

                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();

                }


            }

        }
    }
}
