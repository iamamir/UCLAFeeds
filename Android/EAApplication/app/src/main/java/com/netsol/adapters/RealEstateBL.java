package com.netsol.adapters;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.netsol.Models.UserModel;
import com.netsol.utiliy.Consts;
import com.netsole.AppPreferences;
import android.app.Activity;
import android.content.Context;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class RealEstateBL {

	Activity act;
	Context ctx;
	ServerCom server;
	AppPreferences prefs;

	public RealEstateBL(final Activity act) {
		this.act = act;
		ctx = act.getApplicationContext();
		server = new ServerCom();
		prefs = new AppPreferences();
	}

	// login
	public String logInAPI(String email,String password) {
		String strResponse = "";

		try {

			String url = Consts.BASE_URL + "LoginUser";

			strResponse = server
					.postJSONObject(url, jsonLogIn(email,password));

			if (strResponse != null && strResponse.length() > 0) {

				JSONObject jsonObj = new JSONObject(strResponse);

				String status = jsonObj.optString("statusCode");

				if(status.equalsIgnoreCase("200")){
					strResponse = "success";
                    UserModel user = new UserModel();
                    user.email = email;
                    user.password = password;
					Consts.CURRENT_USER = user;
				}
				else
				{
					strResponse = "fails";
				}

			}

		} catch (Exception e) {
			e.printStackTrace();

		}

		return strResponse;

	}
    public String RegisterAPI(String email) {
        String strResponse = "";

        try {

            String url = Consts.BASE_URL + "RegisterUser";

            strResponse = server
                    .postJSONObject(url, jsonRegister(email));

            if (strResponse != null && strResponse.length() > 0) {

                JSONObject jsonObj = new JSONObject(strResponse);

                String status = jsonObj.optString("statusCode");

                if(status.equalsIgnoreCase("200")){
                    strResponse = "success";

                }
                else
                {
                    strResponse = "fails";
                }

            }

        } catch (Exception e) {
            e.printStackTrace();

        }

        return strResponse;

    }


    public String postingFavouriteAPI(String messageID,String isFav , String isRead) {
        String strResponse = "";

        try {

            String url = Consts.BASE_URL + "postMessages";

            strResponse = server
                    .postJSONObject(url, jsonMessage(messageID, isFav, isRead));

            if (strResponse != null && strResponse.length() > 0) {

                JSONObject jsonObj = new JSONObject(strResponse);

                String status = jsonObj.optString("statusCode");

                if(status.equalsIgnoreCase("200")){
                    strResponse = "success";

                }
                else
                {
                    strResponse = "fails";
                }

            }

        } catch (Exception e) {
            e.printStackTrace();

        }

        return strResponse;

    }
    public List<String> gettingFavouriteListAPI() {
        String strResponse = "";
        List<String> messageList = new ArrayList<String>();

        try {

            String url = Consts.BASE_URL + "getFavouriteMessages";

            strResponse = server
                    .postJSONObject(url, jsonRetriveMessage());

            if (strResponse != null && strResponse.length() > 0) {

                JSONObject jsonObj = new JSONObject(strResponse);

                String status = jsonObj.optString("statusCode");

                if(status.equalsIgnoreCase("200")){
                    strResponse = "success";
                    messageList = parseMessagesId(jsonObj,"favourites");
                    //Consts.messageIDList = Consts

                }
                else
                {
                    strResponse = "fails";
                }

            }

        } catch (Exception e) {
            e.printStackTrace();

        }

        return messageList;

    }
    public List<String> gettingMessqesListAPI() {
        String strResponse = "";
        List<String>readMessagesList = new ArrayList<String>();

        try {

            String url = Consts.BASE_URL + "getReadMessages";

            strResponse = server
                    .postJSONObject(url, jsonRetriveMessage());

            if (strResponse != null && strResponse.length() > 0) {

                JSONObject jsonObj = new JSONObject(strResponse);

                String status = jsonObj.optString("statusCode");

                if(status.equalsIgnoreCase("200")){
                    strResponse = "success";
                    readMessagesList = parseMessagesId(jsonObj, "isReadList");

                }
                else
                {
                    strResponse = "fails";
                }

            }

        } catch (Exception e) {
            e.printStackTrace();

        }

        return readMessagesList;

    }
    public List<String> gettingunReadMessqesListAPI() {
        String strResponse = "";

       List<String> unReadmessageIDList = new ArrayList<String>();
        List<String> finalmessageIDList = new ArrayList<String>();
        try {

            String url = Consts.BASE_URL + "getReadMessages";

            strResponse = server
                    .postJSONObject(url, jsonRetriveMessage());

            if (strResponse != null && strResponse.length() > 0) {
                JSONObject jsonObj = new JSONObject(strResponse);
                String status = jsonObj.optString("statusCode");

                if(status.equalsIgnoreCase("200")){
                    strResponse = "success";
                    unReadmessageIDList = parseMessagesId(jsonObj, "isReadList");

                    for(String unreadmessageId:Consts.messageIDList)
                    {
                        Iterator<String> dbIterator = unReadmessageIDList.iterator();
                       while(dbIterator.hasNext()) {
                           String dbId = dbIterator.next();
                            if (!dbId.equalsIgnoreCase(unreadmessageId)) {
                                if (!dbIterator.hasNext()) {
                                    finalmessageIDList.add(unreadmessageId);
                                }
                            }
                           else {
                                break;
                            }
                        }
                    }
                }
                else
                {
                    strResponse = "fails";
                }

            }

        } catch (Exception e) {
            e.printStackTrace();

        }

        return finalmessageIDList;

    }
    //isReadList

    private List<String> parseMessagesId(JSONObject jsonObject, String objectName) {
        List<String> listUsernames = new ArrayList<String>();

        try{
            JSONObject DataJson = jsonObject.getJSONObject("data");
            JSONArray arr = DataJson.getJSONArray(objectName);

            for (int i = 0; i < arr.length(); i++) {
                try{
                    JSONObject userJson = arr.getJSONObject(i);
                    String messageIdFav = userJson.optString("messageID")+":"+userJson.optString("isFavourite")+":"+userJson.optString("isRead");
                    listUsernames.add(messageIdFav);
                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                }
            }


        }
        catch(JSONException e){
            e.printStackTrace();
        }


        return listUsernames;
    }
	public String forgotPasswordAPI (String email) {
		String strResponse = "";

		try {

			String url = Consts.BASE_URL + "forgetPassword";

			strResponse = server
					.postJSONObject(url, jsonForgotPassword(email));

			if (strResponse != null && strResponse.length() > 0) {

				JSONObject jsonObj = new JSONObject(strResponse);

				String status = jsonObj.optString("statusCode");

				if(status.equalsIgnoreCase("200")){
					strResponse = "Your password has been sent to your email address successfully";

				}
				else
				{
					strResponse = "Your password has not been sent.Please try again ";
				}

			}

		} catch (Exception e) {
			e.printStackTrace();

		}

		return strResponse;

	}
	public String ContactAPI(String name,String email,String inquiry, String subject, String message) {
		String strResponse = "";

		try {

			String url = Consts.CONTACT_URL;
			strResponse = server
					.postJSONObject(url, jsonContactUs(name,email,inquiry,subject,message));

			if (strResponse != null && strResponse.length() > 0) {

				JSONObject jsonObj = new JSONObject(strResponse);

				String status = jsonObj.optString("statusCode");

				if(status.equalsIgnoreCase("200")){
					strResponse = "Email has been sent successfully";

				}
				else
				{
					strResponse = "Email has not been sent .Please try again";
				}

			}

		} catch (Exception e) {
			e.printStackTrace();

		}

		return strResponse;

	}


	private UserModel parseUser(JSONObject jsonObject) {
		UserModel user = new UserModel();
		try{
			JSONArray arr = jsonObject.getJSONArray("userData");
			JSONObject userJson = arr.getJSONObject(0);

			user.userName = userJson.optString("userName");
			user.email = userJson.optString("email");
			user.password = userJson.optString("password");

			JSONArray compArr = jsonObject.getJSONArray("userCompany");
			JSONObject compJson = compArr.getJSONObject(0);



		}
		catch(JSONException e){}


		return user;
	}


	private JSONObject jsonLogIn(String email,String passowrd) {
		JSONObject json = new JSONObject();

		try {
            json.put("password", passowrd);
			json.put("email", email);
            json.put("type", "consumer");

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return json;
	}
    private JSONObject jsonRegister(String email) {
        JSONObject json = new JSONObject();

        try {
           json.put("email", email);
           json.put("type", "consumer");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return json;
    }
    private JSONObject jsonMessage(String messageID,String isFav, String isRead) {
        JSONObject json = new JSONObject();

        try {
            json.put("userID", Consts.CURRENT_USER.email);
            json.put("messageID", messageID);
            json.put("isFav", isFav);
            json.put("isRead", isRead);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return json;
    }
    private JSONObject jsonRetriveMessage() {
        JSONObject json = new JSONObject();

        try {
            json.put("userID", Consts.CURRENT_USER.email);



        } catch (JSONException e) {
            e.printStackTrace();
        }

        return json;
    }
	private JSONObject jsonContactUs(String name,String email,String inquiry,String subject, String message) {
		JSONObject json = new JSONObject();


		try {

			json.put("fullName", name);
			json.put("email", email);
			json.put("inquiry", inquiry);
			json.put("subject", subject);
			json.put("message", message);

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return json;
	}
	private JSONObject jsonForgotPassword(String email) {
		JSONObject json = new JSONObject();

		try {
			json.put("email", email);

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return json;
	}
}
