package com.example.aa.electronicpatientscard;

/**
 * Created by MiroslawCzechorowski on 28.10.2015.
 */
public class Config {

    //Address of scripts of the CRUD
    public static final String URL_LOGIN="http://serwer1558258.home.pl/login.php";
    public static final String URL_ADD="http://serwer1558258.home.pl/addPatient.php";
    public static final String URL_GET_ALL = "http://serwer1558258.home.pl/getAllPatients.php";
    public static final String URL_GET_PATIENT = "http://serwer1558258.home.pl/getPatient.php?id=";
    public static final String URL_UPDATE_PATIENT = "http://serwer1558258.home.pl/updatePatient.php";
    public static final String URL_DELETE_PATIENT = "http://serwer1558258.home.pl/deletePatient.php?id=";
    public static final String URL_GET_IMAGE = "http://serwer1558258.home.pl/getImages.php?id=";
    public static final String URL_UPLOAD_IMAGES = "http://serwer1558258.home.pl/upload.php?id=";
    public static final String URL_GET_OBSERVATION= "http://serwer1558258.home.pl/getObservation.php?id=";
    public static final String URL_ADD_OBSERVATION= "http://serwer1558258.home.pl/addObservation.php";
    //Keys that will be used to send the request to php scripts
    public static final String KEY_PATIENT_ID = "id";
    public static final String KEY_PATIENT_NAME = "name";
    public static final String KEY_PATIENT_LASTNAME = "lastname";
    public static final String KEY_LOGIN="login";
    public static final String KEY_PASSWORD="password";
    public static final String KEY_BITMAP_ID = "BITMAP_ID";
    public static final String KEY_UPLOAD = "image";
    public static final String KEY_OBSERVATION_ID = "observation_id";
    public static final String KEY_DATE = "time";
    public static final String KEY_DESCRIPTION = "comment";
    public static final String KEY_VALUE="value";

    //JSON Tags
    public static final String TAG_JSON_ARRAY="result";
    public static final String TAG_ID = "id";
    public static final String TAG_NAME = "name";
    public static final String TAG_LASTNAME = "lastname";
    public static final String TAG_HISTORY = "history";
    public static final String TAG_IMAGE_URL = "url";
    public static final String TAG_TIME = "time";
    public static final String TAG_VALUE1 = "value";
    public static final String TAG_VALUE2 = "value2";
    public static final String TAG_COMMENT = "comment";

    //patient id to pass with intent
    public static final String PATIENT_ID = "patient_id";
}



