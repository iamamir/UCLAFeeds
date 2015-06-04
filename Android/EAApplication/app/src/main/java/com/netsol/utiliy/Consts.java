package com.netsol.utiliy;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.services.gmail.Gmail;
import com.netsol.Models.UserModel;

import java.util.ArrayList;
import java.util.List;




public class Consts {

	public static String currentLocale = "";
	public static String P_TABS_ID;
	public static String P_TABS_NAME;


	// Server API Format
	public static final String API_FORMAT = "api_name={API_NAME}&format=json&json={JSON}";
	public static final String MULTIPART_API_FORMAT = "?api_name={API_NAME}&format=json";
	public static final String API_NAME_PARM = "{API_NAME}";
	public static final String API_JSON_PARM = "{JSON}";

	public static String ANDROID_ID = "";
	public static String VERSION_NUMBER = "VERSION_NUMBER";
	public static String LOGIN_STATE_LOGOUT = "0";
	public static GoogleCredential credential = null;
	public static  int progress = 0;
	public static UserModel CURRENT_USER ;
	public static String lastLoginState = LOGIN_STATE_LOGOUT; //0=logout
    public static  List<String> messageIDList = new ArrayList<String>();
    public static  List<String> favMessageIDList = new ArrayList<String>();
    public static  List<String> readMessageIDList = new ArrayList<String>();
    public static final String USER = "me";
    public static Gmail service;
	//API
	public static String BASE_URL = "http://10.14.9.4:9000/";
	public static final String CONTACT_URL = BASE_URL + "contactUs";
	public static final String FORGOT_PASSWORD_EMAIL = "";
    public static List<UserModel> userInfoList = new ArrayList<UserModel>();
    public static List<UserModel> favUserInfoList = new ArrayList<UserModel>();
    public static List<UserModel> readUserInfoList = new ArrayList<UserModel>();
    public static List<UserModel> unReadUserInfoList = new ArrayList<UserModel>();

    List<String> messageList = new ArrayList<String>();
}
