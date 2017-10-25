package com.example.aayush.clean;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

/**
 * Created by Aayush on 9/23/2017.
 */

//TODO : THIS IS THE SINGLETON PATTERN AND IT WILL CREATE AN INSTANCE OF THE REQUEST QUE METHOD AND THAT OBJECT WILL BE LIVE AS LONG AS THE APPS RUN SO MORE EFFICIENT WAY TO HANDLE THE NETWORK REQUEST



public class SharedPrefManager {
    private static SharedPrefManager mInstance;
    private static Context mCtx;
    private static final String SHARED_PREFERENCE_NAME="mySharedPref";
    private static final String KEY_USERNAME="username";
    private static final String KEY_USER_EMAIL="useremail";
    private static final String KEY_USER_ID="userid";

    private SharedPrefManager(Context context) {
        mCtx = context;


    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    public boolean userLogin(int id, String username ,String email){

        //TODO: shared Prefernce mode private so that only this application could access this sharedPreference
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREFERENCE_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_USER_ID,id);
        editor.putString(KEY_USER_EMAIL,email);
        editor.putString(KEY_USERNAME,username);
        editor.apply();
        return true;
    }

    public boolean isUserLoggedin(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREFERENCE_NAME,Context.MODE_PRIVATE);
        if (sharedPreferences.getString(KEY_USERNAME,null)!=null){
            return true;
        }
        return false;
    }

    public boolean Logout(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREFERENCE_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.clear();
        editor.apply();
        return true;
    }

    public String getUsername(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREFERENCE_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USERNAME,null);
    }

    public String getUserEmail(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREFERENCE_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USER_EMAIL,null);
    }
}
