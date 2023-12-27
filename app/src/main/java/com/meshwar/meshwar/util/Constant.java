package com.meshwar.meshwar.util;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Constant {
    public static final String TAG_V = "TAG_V";
    public static final String WEATHER_KEY = "900e61e649b5797a2a5094c6ca29dfae";
    public static final String PLACES_KEY = "AIzaSyAbi35_q3IWWeWSmfh08K4GWo13O1OynLI";
    public static final String WEATHER_URI = "https://api.openweathermap.org/data/2.5/weather?";
    public static final String NO_PLACE_IMAGE = "https://firebasestorage.googleapis.com/v0/b/yallatour-4baf8.appspot.com/o/noImage%2Facb907ceb1d75d6297681e6b0962777f.png?alt=media&token=c76cece4-71da-4232-a7a0-daf174885e89" ;
    public static final String NO_USER_IMAGE = "https://firebasestorage.googleapis.com/v0/b/yallatour-4baf8.appspot.com/o/noImage%2Fl60Hf.png?alt=media&token=925f058c-63d5-467d-8910-81083b346357";
    public static final int INCREASE_PLACE = 1;
    public static final int INCREASE_NAVIGATION = 2;
    public static final int INCREASE_COMMENT = 3;
    public static final int INCREASE_USER = 4;
    public static final int INCREASE_NEARBY = 5;
    public static final int DECREASE_PLACE = -1;
    public static final int DECREASE_NAVIGATION = -2;
    public static final int DECREASE_COMMENT = -3;
    public static final int DECREASE_USER = -4;
    public static final int DECREASE_NEARBY = -5;


    public static String LATITUDE_BLOCK = "lat={#}";
    public static final String AND = "&";
    public static String LONGITUDE_BLOCK = "lon={#}";
    public static String KEY_BLOCK = "appid={#}";

    public static final String REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\." + "[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-z" + "A-Z]{2,7}$";


    public static double LONGITUDE;
    public static double LATITUDE;

    public static final String INVALID_PASSWORD_LENGTH_MESSAGE = "password should be more than 6 characters";
    public static final String PASSWORD_DOES_NOT_MATCH_MESSAGE = "Password does not match! ";
    public static final String INVALID_EMAIL_PATTERN_MESSAGE = "Please insert a valid email";
    public static final String EMPTY_FIELD_MESSAGE = "This field is required!";

    public static FirebaseAuth AUTH = FirebaseAuth.getInstance();

    public static FirebaseUser USER;
    public static FirebaseFirestore firebaseDatabase = FirebaseFirestore.getInstance();
    public static CollectionReference users = firebaseDatabase.collection("Users");
    public static CollectionReference places = firebaseDatabase.collection("Places");
    public static CollectionReference reviews = firebaseDatabase.collection("Reviews");
    public static CollectionReference dashboard = firebaseDatabase.collection("Dashboard");
    public static CollectionReference nearby = firebaseDatabase.collection("Nearby");
    public static FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    public static StorageReference placesImagesFolder = firebaseStorage.getReference("Places");
    public static StorageReference usersImagesFolder = firebaseStorage.getReference("Users");
    public static StorageReference nearbyImagesFolder = firebaseStorage.getReference("Nearby");

    public static boolean isAdmin = false;
    public static final String LOREN_EXAMPLE = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Amet risus nullam eget felis eget nunc lobortis mattis. Placerat duis ultricies lacus sed turpis tincidunt id. Sit amet cursus sit amet dictum. Quam elementum pulvinar etiam non quam. Elementum sagittis vitae et leo duis ut diam quam nulla. Amet risus nullam eget felis eget nunc. Sed vulputate odio ut enim blandit volutpat maecenas volutpat. Congue eu consequat ac felis donec et. Accumsan in nisl nisi scelerisque eu ultrices vitae auctor eu.";
    public static final int ALERT_TYPE_YES_NO = 1001;
    public static final int ALERT_TYPE_YES = 1002;
    public static final String PASSING_OBJECT_KEY = "KEY";
    public static final String PASSING_REF_KEY = "REF_KEY";


}
