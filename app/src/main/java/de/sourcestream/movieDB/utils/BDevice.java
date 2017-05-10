package de.sourcestream.movieDB.utils;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.os.Handler;
import android.support.v4.view.ViewConfigurationCompat;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

public class BDevice {

    public static DisplayMetrics getDisplayMetrics(Context context) {
        DisplayMetrics outMetrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics;
    }

    public static int getScreenHeight(Context context) {
        return getDisplayMetrics(context).heightPixels;
    }

    public static int getScreenWidth(Context context) {
        return getDisplayMetrics(context).widthPixels;
    }

    public static int getDensityDpi(Context context) {
        return getDisplayMetrics(context).densityDpi;
    }

    public static void hideSoftKeyboard(Context context, View paramView) {
        ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                paramView.getWindowToken(), 0);
    }

    public static void showSoftKeyboard(Dialog paramDialog) {
        paramDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    public static void hideSoftKeyboard(Dialog paramDialog) {
        paramDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    public static void showSoftKeyboard(Context context, View paramView) {
        ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(paramView,
                InputMethodManager.SHOW_IMPLICIT);
    }

    public static boolean isInternetConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        boolean isConnected = false;
        if (info != null && info.isConnectedOrConnecting()) {
            isConnected = true;
        }
        return isConnected;
    }

    public static int getPixelFromDp(Context context, int dpUnits) {
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpUnits, getDisplayMetrics(context));
        return (int) px;
    }

    public static float getPixelFromDp(Context context, float dpInFloat) {
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpInFloat, getDisplayMetrics(context));
        return px;
    }

    public static float getPixelFromSp(Context context, float spUnits) {
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spUnits, getDisplayMetrics(context));
        return px;
    }

    /**
     * @param delay (Note: delay should be more than 100)
     */
    public static void showSoftInput(final Context context, final EditText view, int delay) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showSoftKeyboard(context, view);
            }
        }, delay);
    }

    public static int getSDKVersion() {
        return Build.VERSION.SDK_INT;
    }

    public static String getMobileNo(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getLine1Number();
    }

    public static boolean isBluetoothEnabled() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        return (mBluetoothAdapter != null && mBluetoothAdapter.isEnabled());
    }

    public static int getNavigationBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    public static boolean hasSoftNavigation(Context context) {
        return ViewConfigurationCompat.hasPermanentMenuKey(ViewConfiguration.get(context));
    }

    public static int getWindowWidth(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        return width;
    }

    public static int getWindowHeight(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.y;
        return height;
    }

    /**
     * Api level 16
     */
    public static boolean hasJellyBean() {
        return Build.VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN;
    }

    public static String getPhoneLocale() {
        return Locale.getDefault().toString();
    }

    public static String getPhoneLocaleLanguage() {
        return Locale.getDefault().toString().substring(0, Locale.getDefault().toString().lastIndexOf("_"));
    }

    public static String getAppVersionName(Context mContext) throws PackageManager.NameNotFoundException {
        return mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionName;
    }

    public static int getActionBarSize(Context context) {
        final TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(
                new int[]{android.R.attr.actionBarSize});
        int mActionBarSize = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();
        return mActionBarSize;
    }

    public static int getStatusBarHeight(Context context) {
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return resourceId > 0 ? context.getResources().getDimensionPixelSize(resourceId) : 0;
    }

    public static void setSystemUiVisibility(View view){
        if(Build.VERSION.SDK_INT >= 21) {
            view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        }
    }

    public static String setWebViewWithImageFit(String content){

        // content is the content of the HTML or XML.
        String stringToAdd = "width=\"100%\" height=\"50%\" ";

        // Create a StringBuilder to insert string in the middle of content.
        StringBuilder sb = new StringBuilder(content);

        int i = 0;
        int cont = 0;

        // Check for the "src" substring, if it exists, take the index where
        // it appears and insert the stringToAdd there, then increment a counter
        // because the string gets altered and you should sum the length of the inserted substring
        while(i != -1){
            i = content.indexOf("src", i + 1);
            if(i != -1) sb.insert(i + (cont * stringToAdd.length()), stringToAdd );
            ++cont;
        }

       /* // Set the webView with the StringBuilder: sb.toString()
        WebView detailWebView = (WebView) findViewById(R.id.web_view);
        detailWebView.loadDataWithBaseURL(null, sb.toString(), "text/html", "utf-8", null);*/
        return  sb.toString();
    }

    public static boolean getOrientation(Context context){
        boolean isLandscapeOrientation = false;
        int rotation = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getRotation();
        // DisplayMetrics dm = new DisplayMetrics();
        // getWindowManager().getDefaultDisplay().getMetrics(dm);
        int orientation;
        CharSequence text;

        switch (rotation) {
            case Surface.ROTATION_0:
                isLandscapeOrientation = false;
//                text = "SCREEN_ORIENTATION_PORTRAIT";
                break;
            case Surface.ROTATION_90:
                isLandscapeOrientation = true;
//                text = "SCREEN_ORIENTATION_LANDSCAPE";
                break;
            case Surface.ROTATION_180:
                isLandscapeOrientation = false;
//                text = "SCREEN_ORIENTATION_REVERSE_PORTRAIT";
                break;
            case Surface.ROTATION_270:
                isLandscapeOrientation = true;
//                text = "SCREEN_ORIENTATION_REVERSE_LANDSCAPE";
                break;
            default:
                isLandscapeOrientation = false;
//                text = "SCREEN_ORIENTATION_PORTRAIT";
                break;
        }
        return isLandscapeOrientation;
    }

    public static void openDocViewer(Context context, File url)  {
        // Create URI
        Uri uri = Uri.fromFile(url);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        // Check what kind of file you are trying to open, by comparing the url with extensions.
        // When the if condition is matched, plugin sets the correct intent (mime) type,
        // so Android knew what application to use to open the file
        if (url.toString().contains(".doc") || url.toString().contains(".docx")) {
            // Word document
            intent.setDataAndType(uri, "application/msword");
        } else if(url.toString().contains(".pdf")) {
            // PDF file
            intent.setDataAndType(uri, "application/pdf");
        } else if(url.toString().contains(".ppt") || url.toString().contains(".pptx")) {
            // Powerpoint file
            intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
        } else if(url.toString().contains(".xls") || url.toString().contains(".xlsx")) {
            // Excel file
            intent.setDataAndType(uri, "application/vnd.ms-excel");
        } else if(url.toString().contains(".zip") || url.toString().contains(".rar")) {
            // WAV audio file
            intent.setDataAndType(uri, "application/x-wav");
        } else if(url.toString().contains(".rtf")) {
            // RTF file
            intent.setDataAndType(uri, "application/rtf");
        } else if(url.toString().contains(".wav") || url.toString().contains(".mp3")) {
            // WAV audio file
            intent.setDataAndType(uri, "audio/x-wav");
        } else if(url.toString().contains(".gif")) {
            // GIF file
            intent.setDataAndType(uri, "image/gif");
        } else if(url.toString().contains(".jpg") || url.toString().contains(".jpeg") || url.toString().contains(".png")) {
            // JPG file
            intent.setDataAndType(uri, "image/jpeg");
        } else if(url.toString().contains(".txt")) {
            // Text file
            intent.setDataAndType(uri, "text/plain");
        } else if(url.toString().contains(".3gp") || url.toString().contains(".mpg") || url.toString().contains(".mpeg") || url.toString().contains(".mpe") || url.toString().contains(".mp4") || url.toString().contains(".avi")) {
            // Video files
            intent.setDataAndType(uri, "video/*");
        } else {
            //if you want you can also define the intent type for any other file
            //additionally use else clause below, to manage other unknown extensions
            //in this case, Android will show all applications installed on the device
            //so you can choose which application to use
            intent.setDataAndType(uri, "*/*");
        }
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "Application not found for this type of file.", Toast.LENGTH_LONG).show();
        }
    }

    public static Map<String,String> convertJsonToHashMap(String jsonString){
        Map<String,String> outputMap = new HashMap<>();
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            Iterator<String> keysItr = jsonObject.keys();
            while(keysItr.hasNext()) {
                String key = keysItr.next();
                String value = (String) jsonObject.get(key);
                outputMap.put(key, value);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return outputMap;
    }

}
