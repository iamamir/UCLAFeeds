package com.netsol.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.netsol.Models.UserModel;
import com.netsol.utiliy.Consts;
import com.netsol.utiliy.Utility;
import com.netsole.Activity_Detail_Email;
import com.netsole.DataTransferInterface;
import com.netsole.MainActivity;
import com.netsole.eaapplication.R;
import java.util.ArrayList;
import java.util.List;


public class Adapter_Property_Detail_Item extends BaseAdapter {
    protected Activity parentActivity;
    List<UserModel> userModelList = new ArrayList<UserModel>();
    Activity context;
    RealEstateBL zsBL;

    DataTransferInterface dtInterface;
    public Adapter_Property_Detail_Item(Activity activity, List<UserModel> userModelList1,DataTransferInterface dtInterfaces) {
        this.parentActivity = activity;
        context = activity;
        this.userModelList = userModelList1;
        dtInterface = dtInterfaces;
    }

    @Override
    public View getView(final int position, View convertView,
                        final ViewGroup parent) {
       final ProgramDetailHolder holder = new ProgramDetailHolder();
        convertView = null;
        if (convertView == null) {
            LayoutInflater inflater = parentActivity.getLayoutInflater();
            convertView = inflater.inflate(R.layout.item_property,
                    null);
            convertView.setTag(holder);
        }
//        else {
//            holder = (ProgramDetailHolder) convertView.getTag();
//        }
        zsBL = new RealEstateBL(context);
        holder.programcontainerpanel = (RelativeLayout) convertView.findViewById(R.id.programcontainerpanel);
        holder.body = (TextView) convertView.findViewById(R.id.item_email_body);
        holder.subject = (TextView) convertView.findViewById(R.id.item_email_subject);
       // holder.datetime = (TextView) convertView.findViewById(R.id.tv_email_date);
        holder.mUserNameImage = (TextView) convertView.findViewById(R.id.design_layout_user_image);
        holder.favBtn = (ImageButton) convertView.findViewById(R.id.imgBtnFavourite);

        holder.programcontainerpanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String position = (String) v.getTag();
                UserModel userObj =  userModelList.get(Integer.parseInt(position));
                if(userObj.isRead.equalsIgnoreCase("true")) {
                    Intent i = new Intent(context,
                            Activity_Detail_Email.class);
                    i.putExtra("UserInfoList", userObj);
                    i.putExtra("position", position);
                    context.startActivity(i);
                    context.finish();
                    //Toast.makeText(context,"Already Seen",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    userObj.isRead = "true";
                    new PostReadMessageTask(position).execute(userObj);
                    //Toast.makeText(context,"Seen",Toast.LENGTH_LONG).show();
                    Consts.readMessageIDList.add(userObj.messageID+":"+userObj.isFavourite+":"+userObj.isRead);
                }

            }
        });
        holder.favBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String position = (String) v.getTag();
                UserModel userObj =  userModelList.get(Integer.parseInt(position));
                if(userObj.isFavourite.equalsIgnoreCase("true"))
                {
                    boolean flag = Consts.favMessageIDList.remove(userObj.messageID+":"+userObj.isFavourite+":"+userObj.isRead);
                    boolean flag1 = Consts.favUserInfoList.remove(userObj);
                    Toast.makeText(context,"Removed from favourites",Toast.LENGTH_LONG).show();
                    userObj.isFavourite = "false";
                    holder.favBtn.setImageResource(R.drawable.fav_icon1);
                    if (flag1)
                    {
                        if (userModelList.size()>0) {
                            updateAdapter(userModelList);
                        }
                        else
                        {
                            updateAdapter(userModelList);
                            Toast.makeText(context,"No favourites found",Toast.LENGTH_SHORT).show();
                            dtInterface.setValues("hello world");
                        }
                    }


                }
                else{
                    Toast.makeText(context,"Added to favourites",Toast.LENGTH_SHORT).show();
                    userObj.isFavourite = "true";
                    holder.favBtn.setImageResource(R.drawable.fav_icon);
                    Consts.favMessageIDList.add(userObj.messageID+":"+userObj.isFavourite+":"+userObj.isRead);
                    Consts.favUserInfoList.add(userObj);

                }

                new PostFavReadMessageTask().execute(userObj);


            }
        });
        UserModel userObj = userModelList.get(position);
        holder.subject.setText(userObj.subject);
       // holder.datetime.setText(userObj.messagedataTime);
        holder.body.setText(userObj.message.substring(0, 50));
        int colorInt = Utility.resourceNameToId(context, userObj.colorCode, "color");

        GradientDrawable gradientDrawable = (GradientDrawable) holder.mUserNameImage.getBackground();
        gradientDrawable.setColor(context.getResources().getColor(colorInt));
        holder.programcontainerpanel.setTag(position + "");
        holder.favBtn.setTag(position + "");
        if (userObj.subject.length()>0) {
            char a1 = Utility.getFirstCharacter(userObj.subject, 0);
            holder.mUserNameImage.setText((a1+""));
        }
        holder.favBtn.setImageResource(R.drawable.fav_icon1);

            if(userObj.isFavourite.equalsIgnoreCase("true"))
            {
                holder.favBtn.setImageResource(R.drawable.fav_icon);
            }
            else{
                holder.favBtn.setImageResource(R.drawable.fav_icon1);

        }

        if(userObj.isRead.equalsIgnoreCase("true"))
        {
            holder.subject.setText(userObj.subject);
            holder.body.setText(userObj.message.substring(0, 50));
        }
        else{

            holder.subject.setText(userObj.subject.toUpperCase());
            holder.body.setText(userObj.message.substring(0, 50).toUpperCase());
        }

        return convertView;

    }

    public void updateAdapter(List<UserModel> arrylst) {
        this.userModelList= arrylst;

        //and call notifyDataSetChanged
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return userModelList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    class PostReadMessageTask extends AsyncTask<UserModel, Void, Void>
    {
        UserModel userObj;
        String response = "";
        ProgressDialog pd;
        String position;

        PostReadMessageTask(String i ) {
            position = i;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            pd = new ProgressDialog(context);
            pd.setIndeterminate(false);
            pd.setCancelable(true);
        }

        @Override
        protected Void doInBackground(UserModel... arg0)
        {
            userObj = arg0[0];
            response = zsBL.postingFavouriteAPI(arg0[0].messageID,userObj.isFavourite,userObj.isRead);

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

                    Intent i = new Intent(context,
                            Activity_Detail_Email.class);
                    i.putExtra("UserInfoList", userObj);
                    i.putExtra("position", position);
                    context.startActivity(i);
                    context.finish();

                }
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("seen.Please try again.")
                            .setCancelable(false)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id)
                                {

                                    context.finish();

                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();

                }


            }

        }
    }
    class PostFavReadMessageTask extends AsyncTask<UserModel, Void, Void>
    {
        UserModel userObj;
        String response = "";
        ProgressDialog pd;
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            pd = new ProgressDialog(context);
            pd.setIndeterminate(false);
            pd.setCancelable(true);
        }

        @Override
        protected Void doInBackground(UserModel... arg0)
        {
            userObj = arg0[0];
            response = zsBL.postingFavouriteAPI(arg0[0].messageID,userObj.isFavourite,userObj.isRead);

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

//                    Intent intent = new Intent();
//                    intent.setAction("com.hmkcode.android.USER_ACTION");
//                    context.sendBroadcast(intent);

                }
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("seen.Please try again.")
                            .setCancelable(false)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id)
                                {

                                    context.finish();

                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();

                }


            }

        }
    }


}

  class ProgramDetailHolder {
    RelativeLayout programcontainerpanel;
    TextView body;
    TextView subject;
    //TextView datetime;
    TextView mUserNameImage;
   ImageButton favBtn;


}
