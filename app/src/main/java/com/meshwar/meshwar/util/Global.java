package com.meshwar.meshwar.util;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.webkit.MimeTypeMap;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Global {

    public static boolean isValidEmail(String email) {
        // Define the email pattern
        String emailPattern = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

        // Create a Pattern object
        Pattern pattern = Pattern.compile(emailPattern);

        // Create a Matcher object
        Matcher matcher = pattern.matcher(email);

        // Check if the email matches the pattern
        return matcher.matches();
    }

    public static String formatDateFromTimestamp(long timestamp) {
        try {
            // Convert timestamp to Date
            Date date = new Date(timestamp);

            // Create a SimpleDateFormat to format the date
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

            // Format the date as a String
            return sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error converting date";
        }
    }

    public static boolean validField(@NonNull List<TextView> input) {

        List<TextView> passwords = new ArrayList<>();
        for (TextView textView : input) {
            if (textView.getText().toString().isEmpty()) {
                return showTextError(textView, Constant.EMPTY_FIELD_MESSAGE);
            }

            if (textView.getInputType() == 33) {
                Pattern pat = Pattern.compile(Constant.REGEX);
                if (!pat.matcher(textView.getText().toString()).matches()) {
                    return showTextError(textView, Constant.INVALID_EMAIL_PATTERN_MESSAGE);
                }
            }


            if (textView.getInputType() == 129) {
                if (textView.getText().toString().length() < 6) {
                    return showTextError(textView, Constant.INVALID_PASSWORD_LENGTH_MESSAGE);
                }

                char ch = textView.getText().toString().charAt(0);
                if (Character.isUpperCase(ch)) {
                    return showTextError(textView, Constant.INVALID_EMAIL_PATTERN_MESSAGE);
                }

                passwords.add(textView);
            }
        }
        for (int i = 0; i < passwords.size(); i++) {
            if (!passwords.get(0).getText().toString().equals(passwords.get(i).getText().toString())) {
                return showTextError(passwords.get(i), Constant.PASSWORD_DOES_NOT_MATCH_MESSAGE);

            }
        }

        return true;
    }

    public static boolean validPhone(String phone) {
        if (phone.isEmpty()) {
            return false;
        }
        if (!phone.matches("[0-9]+")) {
            return false;
        }
        if (phone.length() != 13) {
            return false;
        }
        if (!(phone.charAt(0) == '+' && phone.charAt(1) == '9' && phone.charAt(2) == '6' && phone.charAt(3) == '2')) {
            return false;
        }
        if (!(phone.charAt(4) == '7' && (phone.charAt(5) == '7' || phone.charAt(5) == '8' || phone.charAt(5) == '9'))) {
            return false;
        }
        return true;
    }

    public static boolean showTextError(TextView textView, String message) {
        textView.setError(message);
        textView.requestFocus();
        return false;
    }

    public static double distFrom(
            double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 3958.75;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double dist = earthRadius * c;

        return dist;
    }

    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        // Convert latitude and longitude from degrees to radians
        double lat1Rad = Math.toRadians(lat1);
        double lon1Rad = Math.toRadians(lon1);
        double lat2Rad = Math.toRadians(lat2);
        double lon2Rad = Math.toRadians(lon2);

        // Calculate differences
        double dLat = lat2Rad - lat1Rad;
        double dLon = lon2Rad - lon1Rad;

        // Haversine formula
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(lat1Rad) * Math.cos(lat2Rad) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        // Calculate distance
        double distance = 6371 * c;

        return distance;
    }
    public static String formatDistance(double distance) {
        // Format the distance to two decimal points
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(distance);
    }

    public static String getFileExtension(Uri uri, ContentResolver contentResolver) {
        ContentResolver cr = contentResolver;
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    public static AlertDialog.Builder dialogYesNo(Context context, String title, String message, boolean flag, DialogInterface positiveButton, DialogInterface negativeButton) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setCancelable(flag);

        builder.setPositiveButton("Ok", (DialogInterface.OnClickListener) positiveButton);
        builder.setNegativeButton("Cancel", (DialogInterface.OnClickListener) negativeButton);
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle(title);
        alert.show();
        return builder;
    }

    public static AlertDialog.Builder dialogYesNo(Context context, String title, String message, boolean flag, DialogInterface.OnClickListener positiveButton) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setCancelable(flag);

        builder.setPositiveButton("Ok", positiveButton);
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle(title);
        alert.show();
        return builder;
    }

    public static String getNullString(String title) {
        if (title == null) {
            return "";
        } else {
            return title;
        }
    }

    public static String getPlaceImageNotFound(String s) {
        if (s == null || s.isEmpty()) {
            return Constant.NO_PLACE_IMAGE;
        } else {
            return s;
        }

    }

    public static String getUserImageNotFound(String s) {
        if (s == null || s.isEmpty()) {
            return Constant.NO_USER_IMAGE;
        } else {
            return s;
        }

    }



    public static String getNullCity(String city) {
        if (city == null) {
            return "Cannot find a city ";
        }
        return city;
    }
}
