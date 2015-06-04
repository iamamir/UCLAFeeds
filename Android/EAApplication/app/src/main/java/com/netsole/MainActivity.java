package com.netsole;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Base64;
import com.google.api.client.util.StringUtils;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.ListThreadsResponse;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePart;
import com.google.api.services.gmail.model.MessagePartHeader;
import com.google.api.services.gmail.model.Thread;
import com.netsol.Models.UserModel;
import com.netsol.adapters.Adapter_Property_Detail_Item;
import com.netsol.adapters.RealEstateBL;
import com.netsol.utiliy.Consts;
import com.netsol.utiliy.NDSpinner;
import com.netsol.utiliy.Utility;
import com.netsole.eaapplication.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class MainActivity extends ActionBarActivity implements AdapterView.OnItemSelectedListener, DataTransferInterface {

    HttpTransport httpTransport = new NetHttpTransport();
    JsonFactory jsonFactory = new JacksonFactory();
    GoogleCredential credential;
    String token = "";
    EditText editTextSearchStr;
    ListView list;
    TextView tvNotification;
    Button searchButton, favouriteButton, btnLogout;
    SharedPreferences pref;
    List<UserModel> searchedEmailList = new ArrayList<UserModel>();
    ProgressDialog pDialog;
    RealEstateBL zsBL;
    Context mDataTransferContext = this.mDataTransferContext;
    private static final String APP_NAME = "GmailTestApplication";
    Adapter_Property_Detail_Item zipAdapter;
    boolean favFlag = false;

    String[] listContent = {"All", "Read", "Unread"};

    NDSpinner spinnerOsversions;
    TextView selVersion;
    protected boolean inhibit_spinner = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        zsBL = new RealEstateBL(this);
        pref = getSharedPreferences("AppPref", MODE_PRIVATE);
        list = (ListView) findViewById(R.id.listView);
        editTextSearchStr = (EditText) findViewById(R.id.activity_main_editText_search);
        searchButton = (Button) findViewById(R.id.btn_search);
        btnLogout = (Button) findViewById(R.id.btn_logout);
        tvNotification = (TextView) findViewById(R.id.tv_notification);
        tvNotification.setVisibility(View.GONE);
        favouriteButton = (Button) findViewById(R.id.btnFavourites);
        searchButton.setOnClickListener(onSearchButtonClicked);
        favouriteButton.setOnClickListener(onFavouriteButtonClicked);
        btnLogout.setOnClickListener(onLogOutButtonClicked);

        selVersion = (TextView) findViewById(R.id.selVersion);
        spinnerOsversions = (NDSpinner) findViewById(R.id.osversions);

        ArrayAdapter<String> adapter_state = new ArrayAdapter<String>(this,
                R.layout.item_textview, listContent);
        adapter_state
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOsversions.setAdapter(adapter_state);
        spinnerOsversions.setOnItemSelectedListener(this);

    }

    public void onItemSelected(AdapterView<?> parent, View view, int position,
                               long id) {
        favouriteButton.setBackgroundResource(R.drawable.fav_icon1);
        if (inhibit_spinner) {

            inhibit_spinner = false;
        } else {
            String selState = (String) spinnerOsversions.getSelectedItem();
            selVersion.setText("Mail Box");

            if (position == 0) {
                if (Consts.messageIDList.size() > 0) {
                    new MessageDecodeForFavReadTask("all_emails").execute(Consts.messageIDList);
                } else {
                    new GetAllEmailsTask().execute();
                }
            } else if (position == 1) {
                new MessageDecodeForFavReadTask("read").execute(Consts.readMessageIDList);
            } else {
                new MessageDecodeForFavReadTask("unread").execute(Consts.readMessageIDList);
            }
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
        new GetAllEmailsTask().execute();
    }

    @Override
    protected void onResume() {
        super.onResume();


        initComponents();
    }

    private void initComponents() {

        if (Consts.userInfoList.size() > 0) {
            new MessageDecodeForFavReadTask("all_emails").execute(Consts.messageIDList);
        } else {
            new GetAllEmailsTask().execute();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
//        myReceiver = new MyReceiver();
//        intentFilter = new IntentFilter("com.hmkcode.android.USER_ACTION");
//        unregisterReceiver(myReceiver);
    }

    private View.OnClickListener onSearchButtonClicked = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            String searchedText = editTextSearchStr.getText().toString();
            if (searchedText.length() > 0)
                new SearchTask().execute(searchedText);
            else {
                Toast.makeText(MainActivity.this, "Please enter a string to search.", Toast.LENGTH_SHORT).show();

            }
        }
    };
    private View.OnClickListener onLogOutButtonClicked = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            SharedPreferences.Editor edit = pref.edit();
            edit.putString("Email", "");
            edit.putString("token", "");
            edit.commit();

            Intent i = new Intent(MainActivity.this,
                    Activity_Login_Screen.class);
            startActivity(i);
            finish();
        }
    };


    private View.OnClickListener onFavouriteButtonClicked = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (favFlag) {
                favFlag = false;
                if (Consts.favMessageIDList.size() > 0) {
                    favouriteButton.setBackgroundResource(R.drawable.fav_icon);
                } else {

                    favouriteButton.setBackgroundResource(R.drawable.fav_icon);
                }

                new MessageDecodeForFavReadTask("favourite").execute(Consts.favMessageIDList);

            } else {
                favFlag = true;
                new MessageDecodeForFavReadTask("all_emails").execute(Consts.messageIDList);
                favouriteButton.setBackgroundResource(R.drawable.fav_icon1);
            }

        }
    };

    private final TextWatcher searchStr = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

//            String searchedText = "check";//editTextSearchStr.getText().toString();
//            new SearchTask().execute(searchedText);
        }

        @Override
        public void afterTextChanged(Editable s) {


        }
    };


    class SearchTask extends AsyncTask<String, Void, Void> {
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Searching Emails....");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(String... arg0) {
            for (UserModel user : Consts.userInfoList) {
                if (user.subject.toLowerCase().contains(arg0[0])) {
                    searchedEmailList.add(user);
                }
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            pDialog.dismiss();
            if (!searchedEmailList.isEmpty()) {
                zipAdapter = new Adapter_Property_Detail_Item(MainActivity.this, searchedEmailList, MainActivity.this);
                list.setAdapter(zipAdapter);
                list.setVisibility(View.VISIBLE);
                tvNotification.setVisibility(View.GONE);
            } else {
                zipAdapter = new Adapter_Property_Detail_Item(MainActivity.this, Consts.userInfoList, MainActivity.this);
                list.setAdapter(zipAdapter);
                list.setVisibility(View.VISIBLE);
                Toast.makeText(MainActivity.this, "No such email found.Please try again.", Toast.LENGTH_SHORT).show();
                tvNotification.setVisibility(View.GONE);
            }

        }
    }


    class GetAllEmailsTask extends AsyncTask<Void, Void, Void> {
        List<Thread> threads = null;

        List<Message> frommessages = new ArrayList<Message>();
        List<Message> tomessages = new ArrayList<Message>();


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Retrive email from gmail server....");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            Consts.messageIDList.clear();

            // Create a new authorized Gmail API client
            Consts.service = new Gmail.Builder(httpTransport, jsonFactory, Consts.credential).setApplicationName(APP_NAME).build();

            // Retrieve a page of Threads; max of 100 by default.

            ListThreadsResponse threadsResponse;

            try {
                threadsResponse = Consts.service.users().threads().list(Consts.USER).setQ("From: wajeeha.kamran@netsoltech.com").execute();
                ListMessagesResponse fromResponse = Consts.service.users().messages().list(Consts.USER).setQ("From: wajeeha.kamran@netsoltech.com").execute();
                ListMessagesResponse toResponse = Consts.service.users().messages().list(Consts.USER).setQ("To: wajeeha.kamran@netsoltech.com").execute();

                threads = threadsResponse.getThreads();
                frommessages = fromResponse.getMessages();
                tomessages = toResponse.getMessages();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();

            }
            for (Message message : frommessages) {
                Consts.messageIDList.add(message.getId());
            }
            //To Messages
            for (Message message : tomessages) {
                Consts.messageIDList.add(message.getId());

            }

            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            new getFavouriteTaskWithNoProgress().execute();

        }
    }

    class getFavouriteTaskWithNoProgress extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (pDialog == null) {
                pDialog = new ProgressDialog(MainActivity.this);
                pDialog.setMessage("Reading Emails....");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(false);
                pDialog.show();
            }

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            Consts.favMessageIDList = zsBL.gettingFavouriteListAPI();
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            new getUNReadTaskWithOutProgress().execute();


        }
    }

    class getUNReadTaskWithOutProgress extends AsyncTask<Void, Void, Void> {

        List<String> unreadMessagesList = new ArrayList<String>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            Consts.readMessageIDList = zsBL.gettingMessqesListAPI();
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pDialog != null) {
                pDialog.dismiss();
            }
            new MessageDecodeTask().execute(Consts.messageIDList);


        }
    }

    class MessageDecodeForFavReadTask extends AsyncTask<List<String>, Void, Void> {

        String mflag;

        public MessageDecodeForFavReadTask(String flag) {
            mflag = flag;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Assigning data to adapter....");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(List<String>... messageIDList) {
            if (Consts.readUserInfoList != null)
                Consts.readUserInfoList.clear();

            if (Consts.favUserInfoList != null)
                Consts.favUserInfoList.clear();

            if (Consts.unReadUserInfoList != null)
                Consts.unReadUserInfoList.clear();

            if (mflag.equalsIgnoreCase("favourite")) {
                for (UserModel userObj : Consts.userInfoList) {

                    Iterator<String> dbIterator = messageIDList[0].iterator();

                    while (dbIterator.hasNext()) {
                        String dbId = dbIterator.next();
                        String messageID[] = dbId.split(":");
                        if (!messageID[0].equalsIgnoreCase(userObj.messageID)) {

                        } else {
                            Consts.favUserInfoList.add(userObj);
                            break;

                        }
                    }
                }
            } else if (mflag.equalsIgnoreCase("read")) {

                for (UserModel userObj : Consts.userInfoList) {

                    Iterator<String> dbIterator = messageIDList[0].iterator();

                    while (dbIterator.hasNext()) {
                        String dbId = dbIterator.next();
                        String messageID[] = dbId.split(":");
                        if (!messageID[0].equalsIgnoreCase(userObj.messageID)) {
                            if (!dbIterator.hasNext()) {

                            }
                        } else {
                            Consts.readUserInfoList.add(userObj);
                            break;
                        }
                    }
                }

            } else if (mflag.equalsIgnoreCase("unread")) {
                for (UserModel userObj : Consts.userInfoList) {

                    Iterator<String> dbIterator = messageIDList[0].iterator();

                    while (dbIterator.hasNext()) {
                        String dbId = dbIterator.next();
                        String messageID[] = dbId.split(":");
                        if (!messageID[0].equalsIgnoreCase(userObj.messageID)) {
                            if (!dbIterator.hasNext()) {
                                Consts.unReadUserInfoList.add(userObj);
                            }
                        } else {

                            break;
                        }
                    }
                }
            } else {
                testFavouritesAndResdMessages();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            pDialog.dismiss();
            if (mflag.equalsIgnoreCase("favourite")) {
                if (!Consts.favUserInfoList.isEmpty()) {

                    zipAdapter = new Adapter_Property_Detail_Item(MainActivity.this, Consts.favUserInfoList, MainActivity.this);
                    list.setAdapter(zipAdapter);
                    list.setVisibility(View.VISIBLE);

                } else {
                    Toast.makeText(MainActivity.this,"No favourites found",Toast.LENGTH_LONG).show();
                    list.setVisibility(View.INVISIBLE);
                }
            } else if (mflag.equalsIgnoreCase("read")) {
                if (!Consts.readUserInfoList.isEmpty()) {

                    zipAdapter = new Adapter_Property_Detail_Item(MainActivity.this, Consts.readUserInfoList, MainActivity.this);
                    list.setAdapter(zipAdapter);
                    list.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(MainActivity.this, "No read emails found .", Toast.LENGTH_SHORT).show();
                }

            } else if (mflag.equalsIgnoreCase("unread")) {
                if (!Consts.unReadUserInfoList.isEmpty()) {
                    zipAdapter = new Adapter_Property_Detail_Item(MainActivity.this, Consts.unReadUserInfoList, MainActivity.this);
                    list.setAdapter(zipAdapter);
                    list.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(MainActivity.this, "No unread emails found.", Toast.LENGTH_SHORT).show();
                }


            } else {
                if (!Consts.userInfoList.isEmpty()) {
                    zipAdapter = new Adapter_Property_Detail_Item(MainActivity.this, Consts.userInfoList, MainActivity.this);
                    list.setAdapter(zipAdapter);
                    list.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(MainActivity.this, "No emails found.", Toast.LENGTH_SHORT).show();
                }
            }


        }
    }

    class MessageDecodeTask extends AsyncTask<List<String>, Void, Void> {


        UserModel fromUser = new UserModel();
        List<String> usermessgesIdList = new ArrayList<String>();


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Assigning data to adapter....");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(List<String>... messageIDList) {
            Consts.userInfoList.clear();
            usermessgesIdList = messageIDList[0];
            for (String messageId : usermessgesIdList) {
                Message downloadedMessage = null;

                List<MessagePartHeader> arrHeaders = null;
                try {
                    if (Consts.service != null) {
                        downloadedMessage = Consts.service.users().messages().get(Consts.USER, messageId).execute();
                    } else {
                        Consts.service = new Gmail.Builder(httpTransport, jsonFactory, Consts.credential).setApplicationName(APP_NAME).build();
                        downloadedMessage = Consts.service.users().messages().get(Consts.USER, messageId).execute();

                    }
                    arrHeaders = downloadedMessage.getPayload().getHeaders();

                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (arrHeaders.size() > 11) {


                    fromUser = new UserModel();

                    fromUser.messageID = messageId;
                    List<MessagePart> messageParts = downloadedMessage.getPayload().getParts();
                    String mimeType = downloadedMessage.getPayload().getMimeType();
                    if (mimeType.contains("alternative")) {

                        String a = StringUtils.newStringUtf8(Base64.decodeBase64(messageParts.get(0).getBody().getData().getBytes()));
                        fromUser.message = a;
                    }

                    fromUser.messagedataTime = arrHeaders.get(15).getValue();
                    fromUser.subject = arrHeaders.get(16).getValue();
                    fromUser.fromUserName = arrHeaders.get(17).getValue();
                    fromUser.toUserName = arrHeaders.get(18).getValue();
                    fromUser.colorCode = Utility.getColorCodeForSender(fromUser.subject);
                    fromUser.isFavourite = "false";
                    fromUser.isRead = "false";

                    Consts.userInfoList.add(fromUser);


                } else {
                    fromUser = new UserModel();
                    fromUser.messageID = messageId;
                    /////////////////////////////////////////////////////////////
                    downloadedMessage = null;
                    try {

                        downloadedMessage = Consts.service.users().messages().get(Consts.USER, messageId).execute();

                        arrHeaders = downloadedMessage.getPayload().getHeaders();
                        List<MessagePart> messageParts = downloadedMessage.getPayload().getParts();
                        String mimeType = downloadedMessage.getPayload().getMimeType();
                        if (mimeType.contains("alternative")) {
                            String a = StringUtils.newStringUtf8(Base64.decodeBase64(messageParts.get(0).getBody().getData().getBytes()));
                            fromUser.message = a;
                        }
                        fromUser.messagedataTime = arrHeaders.get(4).getValue();
                        fromUser.subject = arrHeaders.get(7).getValue();
                        fromUser.fromUserName = arrHeaders.get(9).getValue();

                        fromUser.isFavourite = "false";
                        fromUser.isRead = "false";

                        fromUser.toUserName = arrHeaders.get(8).getValue();
                        fromUser.colorCode = Utility.getColorCodeForSender(fromUser.subject);
                        Consts.userInfoList.add(fromUser);

                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }

            testFavouritesAndResdMessages();

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            pDialog.dismiss();
            zipAdapter = new Adapter_Property_Detail_Item(MainActivity.this, Consts.userInfoList, MainActivity.this);
            list.setAdapter(zipAdapter);
            list.setVisibility(View.VISIBLE);


        }
    }

    void testFavouritesAndResdMessages() {
        for (UserModel userobj : Consts.userInfoList) {
            if (Consts.favMessageIDList.size() > 0) {
                Iterator<String> dbIterator = Consts.favMessageIDList.iterator();
                while (dbIterator.hasNext()) {

                    String dbId = dbIterator.next();
                    String messageID[] = dbId.split(":");
                    if (!messageID[0].equalsIgnoreCase(userobj.messageID)) {
                        userobj.isFavourite = "false";
                    } else {
                        userobj.isFavourite = "true";
                        break;
                    }
                }

            } else {
                Iterator<UserModel> dbIterator = Consts.userInfoList.iterator();
                while (dbIterator.hasNext()) {
                    UserModel dbId = dbIterator.next();
                    dbId.isFavourite = "false";
                }


            }
        }
        for (UserModel userobj : Consts.userInfoList) {
            Iterator<String> dbIterator = Consts.readMessageIDList.iterator();
            while (dbIterator.hasNext()) {
                String dbId = dbIterator.next();
                String messageID[] = dbId.split(":");
                if (!messageID[0].equalsIgnoreCase(userobj.messageID)) {
                    userobj.isRead = "false";
                } else {
                    userobj.isRead = "true";
                    break;
                }
            }
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(MainActivity.this,
                Activity_Login_Screen.class);
        startActivity(i);
        finish();
    }

    public class MyReceiver extends BroadcastReceiver {
        public MyReceiver() {
            super();
        }

        @Override
        public void onReceive(Context context, Intent intent) {

            initComponents();
        }

    }

    @Override
    public void setValues(String a) {
        favouriteButton.setBackgroundResource(R.drawable.fav_icon);
        list.setVisibility(View.INVISIBLE);
    }
}
