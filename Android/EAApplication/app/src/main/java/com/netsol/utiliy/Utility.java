package com.netsol.utiliy;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

public class Utility {
    private static final String LOG_TAG = Utility.class.getSimpleName();

    /*
     * Utlity Method to round the corners of a given image
     *
     */
    public static int resourceNameToId(Context context, String name, String resType) {
        if (name != null && name.length() > 0) {
            return context.getResources().getIdentifier(name, resType, context.getPackageName());
        }

        return 0;
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);


        return output;
    }

    public static String getColorCodeForSender(String username) {

        char a = getFirstCharacter(username, 0);
        String colorNumber = "1";
        switch (a) {
            case 'A':
                colorNumber = "1";
                break;
            case 'B':
                colorNumber = "2";
                break;
            case 'C':
                colorNumber = "3";
                break;
            case 'D':
                colorNumber = "4";
                break;
            case 'E':
                colorNumber = "5";
                break;
            case 'F':
                colorNumber = "6";
                break;
            case 'G':
                colorNumber = "7";
                break;
            case 'H':
                colorNumber = "8";
                break;
            case 'I':
                colorNumber = "9";
                break;
            case 'J':
                colorNumber = "10";
                break;
            case 'K':
                colorNumber = "11";
                break;
            case 'L':
                colorNumber = "12";
                break;
            case 'M':
                colorNumber = "13";
                break;
            case 'N':
                colorNumber = "14";
                break;
            case 'O':
                colorNumber = "15";
                break;
            case 'P':
                colorNumber = "16";
                break;
            case 'Q':
                colorNumber = "17";
                break;
            case 'R':
                colorNumber = "18";
                break;
            case 'S':
                colorNumber = "19";
                break;
            case 'T':
                colorNumber = "20";
                break;
            case 'U':
                colorNumber = "21";
                break;
            case 'V':
                colorNumber = "22";
                break;
            case 'W':
                colorNumber = "23";
                break;
            case 'X':
                colorNumber = "24";
                break;
            case 'Y':
                colorNumber = "25";
                break;
            case 'Z':
                colorNumber = "26";
                break;
        }


        return "color" + colorNumber;
    }

    public static char getFirstCharacter(String username, int pos) {
        char a = username.toUpperCase().charAt(pos);
        if (a < 64 || a > 91) {
            a = getFirstCharacter(username, 1);
        } else {
            return a;
        }
        return a;
    }


    public static void backgroundTranslation(ImageView img_animation) {
        TranslateAnimation animation = new TranslateAnimation(-400.0f, 0.0f, 0.0f, 0.0f);
        animation.setDuration(50000);
        animation.setRepeatCount(5);
        animation.setRepeatMode(Animation.INFINITE);
        //animation.setFillAfter(true);
        img_animation.startAnimation(animation);
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height,
                matrix, false);

        return resizedBitmap;
    }

    public static String cleanHtml(String htmlString) {
        return htmlString.replaceAll("\\<.*?>", "");

    }


    public static String getDayName(String dayName) {
        int day = 0;
        try {
            day = Integer.parseInt(dayName);
        } catch (Exception e) {
            // TODO: handle exception
        }
        String name = "";
        switch (day) {
            case 7:
                name = "SATURDAY";
                break;
            case 1:
                name = "SUNDAY";
                break;
            case 2:
                name = "MONDAY";
                break;
            case 3:
                name = "TUESDAY";
                break;
            case 4:
                name = "WEDNESDAY";
                break;
            case 5:
                name = "THURSDAY";
                break;
            case 6:
                name = "FRIDAY";
                break;
            default:
                break;
        }

        return name;
    }

    public static void appendLog(String text) {

    }

    public static void logToFile(Context context, String msg, String type) {
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());
        //msg = context.toString() + "::" + msg;
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c.getTime());
        File logFile = new File("sdcard/yellowziplog.txt");
        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            //BufferedWriter for performance, true to set append to file flag
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
            buf.append("\r\n " + msg);
            buf.newLine();
            buf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        //		FileOutputStream fOut = null;
        //
        //		OutputStreamWriter osw = null;
        //
        //		try{
        //
        //		fOut = context.openFileOutput("yellowziplog.txt", Context.MODE_WORLD_READABLE);
        //
        //		osw = new OutputStreamWriter(fOut);
        //
        //		osw.write("\r\n "+type+"::"+ msg + "\n\r");
        //
        //		osw.close();
        //
        //		fOut.close();
        //
        //		}catch(Exception e){
        //
        //		e.printStackTrace(System.err);
        //
        //		}
    }

    public static String calculateDateInterval(String date) {


        String dateStart = date;
        Calendar c = Calendar.getInstance();
        String interval = "";

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c.getTime());

        //HH converts hour in 24 hours format (0-23), day calculation
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date d1 = new Date();
        Date d2 = new Date();
        //Date dateObj = new Date();
        //dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        //dateFormat.setLenient(false);
        //dateObj = dateFormat.parse("12/30/2011");
        try {
            d1 = format.parse(dateStart);
            d2 = format.parse(formattedDate);

            //in milliseconds
            long diff = d2.getTime() - d1.getTime();

            long diffSec = diff / 1000 % 60;
            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000) % 24;
            long diffDays = diff / (24 * 60 * 60 * 1000);

            if (diffDays <= 0) {
                if (diffHours > 0) {
                    if (diffHours == 1)
                        interval = diffHours + " hour ago ";
                    else
                        interval = diffHours + " hours ago ";
                } else if (diffMinutes > 0) {
                    if (diffMinutes == 1)
                        interval = diffHours + " minute ago ";
                    else
                        interval = diffHours + " minutes ago ";
                } else if (diffSec > 0) {
                    interval = "just now ";
                }

            } else if (diffDays < 30) {
                if (diffDays == 1)
                    interval = diffDays + " day ago ";
                else
                    interval = diffDays + " days ago ";

            } else {
                long diffMonths = diffDays / 30;
                if (diffMonths < 12) {
                    if (diffMonths == 1)
                        interval = diffMonths + " month ago ";
                    else
                        interval = diffMonths + " months ago ";
                } else {
                    interval = " a year ago ";
                }


            }


        } catch (Exception e) {
            e.printStackTrace();
        }


        return interval;

    }

    public static void CopyStream(InputStream is, OutputStream os) {
        final int buffer_size = 1024;
        try {
            byte[] bytes = new byte[buffer_size];
            for (; ; ) {
                int count = is.read(bytes, 0, buffer_size);
                if (count == -1)
                    break;
                os.write(bytes, 0, count);
            }
        } catch (Exception ex) {
        }
    }
//	public  static void showAlertDialog(Context context, String title, String message, Boolean status) {
//		AlertDialog alertDialog = new AlertDialog.Builder(context).create();
//
//		// Setting Dialog Title
//		alertDialog.setTitle(title);
//
//		// Setting Dialog Message
//		alertDialog.setMessage(message);
//
//		// Setting alert dialog icon
//		alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);
//
//		// Setting OK Button
//		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
//			public void onClick(DialogInterface dialog, int which) {
//			}
//		});
//
//		// Showing Alert Message
//		alertDialog.show();
//	}

}
