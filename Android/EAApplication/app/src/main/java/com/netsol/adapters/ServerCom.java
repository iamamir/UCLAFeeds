package com.netsol.adapters;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import com.netsol.utiliy.Consts;


public class ServerCom {
	private final static int timeoutConnection = 15000;
	private final static int timeoutSocket = 60000;
	private static final String USER_AGENT_HEADER = "user-agent";
	private static final String CONTENT_TYPE_HEADER = "Content-Type";
	private Uri.Builder builder;
	int serverResponseCode = 0;
	ProgressDialog dialog = null;
	 private String json = "";
	/**
	 *
	 * @param httpPost
	 */
	public static void setHTTPHeaderPost(HttpPost httpPost) {
		httpPost.setHeader("platform", "Android");
		httpPost.setHeader("make", Build.MANUFACTURER);
		httpPost.setHeader("model", Build.MODEL);
		httpPost.setHeader("os_version", Build.VERSION.SDK);
		httpPost.setHeader("app_version", Consts.VERSION_NUMBER);
		httpPost.setHeader("serial_number", Consts.ANDROID_ID);
		httpPost.setHeader("Content-Type",
				"application/x-www-form-urlencoded;charset=UTF-8");
	}

	public static void setHTTPHeaderGet(HttpGet httpGet) {
		httpGet.setHeader("Content-Type",
				"application/json");
	}

	public static boolean verifyEMPStatus(JSONObject json) {
		if (json != null) {
			if (json.optString("status").equals("0")) {
				Log.d("", "EMP success");
				return true;
			}
		}
		Log.d("", "EMP failure");
		return false;
	}

	public static JSONObject getResponseData(JSONObject json) {
		return json.optJSONObject("data");
	}

	/**
	 *
	 * @param url
	 * @return
	 */
	public JSONObject getJSONResult(Context ctx, final String url) {

		if (!isNetworkAvailable(ctx))
			return null;

		try {
			final HttpClient client = new DefaultHttpClient();
			final HttpGet get = new HttpGet(url);
			final HttpResponse responsePost = client.execute(get);
			final HttpEntity resEntity = responsePost.getEntity();

			boolean DEBUG = true;
			if (DEBUG) {
				Log.d("", "[JSON-ENV] url:  " + url);
			}

			final String str = EntityUtils.toString(resEntity);
			Log.d("Zajil", "response str: " + str);

			if (resEntity != null) {

				JSONObject obj = new JSONObject(str);

				return obj;

			} else {
				return null;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	// ==================================================================
	// ZipShot Insert
	// ==================================================================
	public JSONObject getZipshotDetailJSON(Context ctx, String url) {

		JSONObject jObject = null;

		try {
			final HttpClient client = new DefaultHttpClient();
			final HttpGet get = new HttpGet(url);
			ServerCom.setHTTPHeaderGet(get);
			HttpParams httpParameters = new BasicHttpParams();
			HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

			final HttpResponse responsePost = client.execute(get);
			final HttpEntity resEntity = responsePost.getEntity();

			boolean DEBUG = true;
			if (DEBUG) {
				Log.d("", "[JSON-ENV] url:  " + url);
			}

			final String str = EntityUtils.toString(resEntity);
			Log.d("ZipShot", "response str: " + str);

			if (resEntity != null) {

				jObject = new JSONObject(str);

				return jObject;

			} else {
				return null;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return jObject;

	}

	public JSONArray getGsonArray(Context ctx, String url) {

		JSONArray jArray = null;

		try {
			final HttpClient client = new DefaultHttpClient();

			//String encodedURL = URLEncoder.encode(url, "utf-8");
			url = url.replaceAll(" ", "%20");
			final HttpGet get = new HttpGet(url);
			//setHTTPHeaderGet(get);

			ServerCom.setHTTPHeaderGet(get);
			//HttpParams httpParameters = new BasicHttpParams();
			//HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

			final HttpResponse responsePost = client.execute(get);
			final HttpEntity resEntity = responsePost.getEntity();

			boolean DEBUG = true;
			if (DEBUG) {
				Log.d("", "[JSON-ENV] url:  " + url);
			}

			final String str = EntityUtils.toString(resEntity);
			Log.d("ZipShot", "response str: " + str);

			if (resEntity != null) {

				jArray = new JSONArray(str);

				return jArray;

			} else {
				return null;
			}

		} catch (Exception e) {
			e.printStackTrace();
			// return null;
		}

		return jArray;

	}
	public String createHttpPost(String url, String email, String password) {
	    HttpClient httpclient = new DefaultHttpClient();
	    HttpPost httppost = new HttpPost(url);
	    String jsonResult = null;
	    try {

	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	        nameValuePairs.add(new BasicNameValuePair("email", email));
	        nameValuePairs.add(new BasicNameValuePair("password", password));
	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

	        // Execute HTTP Post Request
	        HttpResponse response = httpclient.execute(httppost);
	        jsonResult = inputStreamToString(response.getEntity().getContent())
					.toString();
	    } catch (ClientProtocolException e) {

	    } catch (IOException e) {

	    }
	    return jsonResult;
	}

	/*
	 * Posting Complete JSON Object instead of separate Params
	 */
	public String postJSONObject(String url, JSONObject obj) {

		url = url.replaceAll(" ", "%20");

		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(url);
		String jsonResult = null;
		String jsonStr = "";


		try {
			HttpParams httpParams = new BasicHttpParams();
			HttpConnectionParams
			.setConnectionTimeout(httpParams, timeoutSocket);
			httppost.setHeader("Accept", "application/json");
			httppost.setHeader("Content-type", "application/json");

			jsonStr = obj.toString();

			StringEntity se = new StringEntity(jsonStr);

			se.setContentEncoding("UTF-8");
			httppost.setEntity(se);

			HttpResponse response = httpclient.execute(httppost);
			jsonResult = inputStreamToString(response.getEntity().getContent())
					.toString();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return jsonResult;
	}
	public String Json_GetPOST(String jsonURL, JSONObject obj)
    {
		String jsonString = obj.toString();
        if (jsonURL.trim() == "")
        {
            throw new IllegalArgumentException("Json URL is not valid");
        }

        try
        {
            // defaultHttpClient
            HttpPost post = new HttpPost(jsonURL);
            post.setHeader("Accept", "application/json");
            post.setHeader("Content-type", "application/json");
            post.setEntity(new StringEntity(jsonString));

            DefaultHttpClient client = new DefaultHttpClient();
            HttpResponse httpresponse = client.execute(post);
            if (httpresponse != null)
            {
                HttpEntity entity = httpresponse.getEntity();
                json = convertStreamToStr(entity.getContent());
            }
            else
            {
                json = "Error";
            }

        }
        catch (UnsupportedEncodingException e)
        {
            json = "Error";
            e.printStackTrace();
        }
        catch (IOException e)
        {
            json = "Error";
            e.printStackTrace();
        }
        return jsonString;
    }

    public static String convertStreamToStr(InputStream iStream) throws IOException
    {
        if (iStream == null)
        {
            throw new IOException("No Response");
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(iStream));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try
        {
            while ((line = reader.readLine()) != null)
            {
                sb.append(line + "\n");
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                iStream.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
	private StringBuilder inputStreamToString(InputStream is) {
		String rLine = "";
		StringBuilder answer = new StringBuilder();
		BufferedReader rd = new BufferedReader(new InputStreamReader(is));

		try {
			while ((rLine = rd.readLine()) != null) {
				answer.append(rLine);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return answer;
	}

	/**
	 * Receive response and parse JSON string as UTF_8 format, for French
	 * characters
	 *
	 * @param url
	 * @return
	 */
	public JSONObject getDecodedJSONResponse(Context ctx, String url) {
		if (!isNetworkAvailable(ctx))
			return null;

		// initialize
		InputStream is = null;
		String result = "";
		JSONObject jArray = null;

		// http post
		try {
			HttpClient client = new DefaultHttpClient();
			HttpGet httpget = new HttpGet(url);
			HttpResponse response = client.execute(httpget);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();

			Log.d("", "*___ url is " + url);

		} catch (Exception e) {
			Log.e("log_tag", "Error in http connection " + e.toString());
		}

		// convert response to string
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, HTTP.UTF_8), 16);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			result = sb.toString();
			Log.d("", "*___json convrsion results are " + sb);

		} catch (Exception e) {
			Log.e("", "Error converting result " + e.toString());
		}

		// try parse the string to a JSON object
		try {

			jArray = new JSONObject(result);
		} catch (JSONException e) {
			Log.e("", "Error parsing data " + e.toString());
			return null;
		}

		return jArray;
	}

	/**
	 *
	 * apiName is not used for anything, just helps to see which request is
	 * being made.
	 *
	 * @param url
	 * @param apiName
	 * @param params
	 * @return
	 */
	public JSONObject postJSONResult(Context ctx, final String url,
			final String apiName, final ArrayList<NameValuePair> params) {

		if (!isNetworkAvailable(ctx))
			return null;

		final HttpClient client = new DefaultHttpClient();
		final HttpPost httpPost = new HttpPost(url);

		final HttpResponse responsePost;
		final HttpEntity resEntity;

		try {

			boolean DEBUG = true;
			if (DEBUG) {
				final StringBuffer buffer = new StringBuffer();
				buffer.append(url + "?");

				for (int i = 0; i < params.size(); i++) {
					if (i != 0) {
						buffer.append("&");
					}
					buffer.append(params.get(i).getName() + "="
							+ params.get(i).getValue());
				}
				Log.d("", "[JSON-ENV] url:  " + buffer.toString());
			}

			HttpConnectionParams.setConnectionTimeout(httpPost.getParams(),
					timeoutConnection);

			// Set the default socket timeout (SO_TIMEOUT)
			// in milliseconds which is the timeout for waiting for data.

			HttpConnectionParams.setSoTimeout(httpPost.getParams(),
					timeoutSocket);
			final UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params,
					HTTP.UTF_8);
			httpPost.setEntity(ent);

			ServerCom.setHTTPHeaderPost(httpPost);

			responsePost = client.execute(httpPost);
			resEntity = responsePost.getEntity();

			if (resEntity != null)
				return new JSONObject(EntityUtils.toString(resEntity));

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return null;
	}

	/**
	 * Encode the parameters we're sending in ISO_8859_1 format
	 *
	 * @param url
	 * @param apiName
	 * @param params
	 * @return
	 */
	public JSONObject getJSONResultEncoded(Context ctx, final String url,
			String apiName, final ArrayList<NameValuePair> params) {

		if (!isNetworkAvailable(ctx))
			return null;

		final HttpEntity resEntity;
		String combinedParams = "";

		try {

			boolean DEBUG = true;
			if (DEBUG) {
				if (!params.isEmpty()) {
					combinedParams += "?";
					for (NameValuePair p : params) {
						String paramString = p.getName()
								+ "="
								+ URLEncoder.encode(p.getValue(),
										HTTP.ISO_8859_1);
						if (combinedParams.length() > 1) {
							combinedParams += "&" + paramString;
						} else {
							combinedParams += paramString;
						}
					}
				}
				// String url =
				// "http://208.97.122.187:7004/CAA-war/caa_gateway";
				// HttpGet request = new HttpGet(url + combinedParams);

				final StringBuffer buffer = new StringBuffer();
				buffer.append(url + "?");

				for (int i = 0; i < params.size(); i++) {
					if (i != 0) {
						buffer.append("&");
					}
					String paramString = params.get(i).getName()
							+ "="
							+ URLEncoder.encode(params.get(i).getValue(),
									HTTP.ISO_8859_1);
					buffer.append(paramString);

				}
				Log.d("", "[JSON-ENV] url:  " + buffer.toString());
			}

			HttpGet request = new HttpGet(url + combinedParams);
			HttpClient client = new DefaultHttpClient();

			ServerCom.setHTTPHeaderGet(request);

			HttpResponse responsePost = client.execute(request);
			resEntity = responsePost.getEntity();

			HttpConnectionParams.setConnectionTimeout(request.getParams(),
					timeoutConnection);

			if (resEntity != null)
				return new JSONObject(EntityUtils.toString(resEntity));

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return null;
	}

	/**
	 *
	 * @param url
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public JSONObject postJSONResult(Context ctx, final String url,
			final ArrayList<NameValuePair> params) {

		if (!isNetworkAvailable(ctx))
			return null;

		final HttpClient client = new DefaultHttpClient();
		final HttpPost httpPost = new HttpPost(url);

		final HttpResponse responsePost;
		final HttpEntity resEntity;

		try {

			Log.d("ZipShots", "REQUEST URL in connection " + params);

			boolean DEBUG = true;
			if (DEBUG) {
				final StringBuffer buffer = new StringBuffer();
				buffer.append(url + "?");

				for (int i = 0; i < params.size(); i++) {
					if (i != 0) {
						buffer.append("&");
					}
					buffer.append(params.get(i).getName() + "="
							+ params.get(i).getValue());
				}
				Log.d("", "[JSON-ENV] url:  " + buffer.toString());
			}

			HttpConnectionParams.setConnectionTimeout(httpPost.getParams(),
					timeoutConnection);
			// Set the default socket timeout (SO_TIMEOUT)
			// in milliseconds which is the timeout for waiting for data.

			HttpConnectionParams.setSoTimeout(httpPost.getParams(),
					timeoutSocket);
			final UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params,
					HTTP.UTF_8);
			httpPost.setEntity(ent);

			ServerCom.setHTTPHeaderPost(httpPost);

			responsePost = client.execute(httpPost);
			resEntity = responsePost.getEntity();

			if (resEntity != null) {
				InputStream instream = resEntity.getContent();
				String result = ServerCom.convertStreamToString(instream);
				instream.close();
				JSONObject jsonObject = new JSONObject(result);
				return jsonObject;
			}
			return null;
		} catch (Exception e) {
			// return null;
			e.printStackTrace();
			return null;
		}
	}


	public String postFile(Context ctx, String url, File file,
			String contentType) {
		String ret = null;
		// if (!isNetworkAvailable(ctx))
		// return null;

		final HttpClient httpClient = new DefaultHttpClient();
		// httpClient.getParams().setParameter(ClientPNames.COOKIE_POLICY,
		// CookiePolicy.RFC_2109);

		HttpPost httpPost = new HttpPost(url);
		setHTTPHeaderPost(httpPost);
		HttpResponse response = null;

		FileEntity tmp = null;

		tmp = new FileEntity(file, contentType);

		httpPost.setEntity(tmp);

		try {
			response = httpClient.execute(httpPost);

			HttpEntity resEntity = response.getEntity();
			if (resEntity != null) {
				InputStream instream = resEntity.getContent();
				ret = ServerCom.convertStreamToString(instream);
				instream.close();

				Log.d("", "[JSON-ENV] response:  " + ret);
			}
		} catch (ClientProtocolException e) {
			System.out.println("HTTPHelp : ClientProtocolException : " + e);
		} catch (IOException e) {
			System.out.println("HTTPHelp : IOException : " + e);
		}

		return ret;
	}


	/**
	 *
	 * @param is
	 * @return
	 */
	public static String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			while ((line = reader.readLine()) != null)
				sb.append(line + "\n");

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	public boolean isNetworkAvailable(Context ctx) {
		ConnectivityManager cm = (ConnectivityManager) ctx
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		return cm.getActiveNetworkInfo() != null
				&& cm.getActiveNetworkInfo().isConnectedOrConnecting();
	}

	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			if (width > height) {
				inSampleSize = Math.round((float) height / (float) reqHeight);
			} else {
				inSampleSize = Math.round((float) width / (float) reqWidth);
			}
		}
		return inSampleSize;
	}

	public static Bitmap decodeBitmapFromUrl(InputStream inputStream,
			int reqWidth, int reqHeight) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(inputStream);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeStream(inputStream);
	}

	public Uri getUriFromUrl(String thisUrl) {
		URL url = null;
		try {
			url = new URL(thisUrl);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		builder = new Uri.Builder().scheme(url.getProtocol())
				.authority(url.getAuthority()).appendPath(url.getPath());
		return builder.build();
	}

}
