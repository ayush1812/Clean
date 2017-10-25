package com.example.aayush.clean;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Aayush on 9/23/2017.
 */

//TODO : THIS IS THE SINGLETON PATTERN AND IT WILL CREATE AN INSTANCE OF THE REQUEST QUE METHOD AND THAT OBJECT WILL BE LIVE AS LONG AS THE APPS RUN SO MORE EFFICIENT WAY TO HANDLE THE NETWORK REQUEST



public class RequestHandler {
    private static RequestHandler mInstance;
    private RequestQueue mRequestQueue;
    private static Context mCtx;

    private RequestHandler(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();

    }

    public static synchronized RequestHandler getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new RequestHandler(context);
        }
        return mInstance;
    }

    //TODO: GIVE US THE REQUEST QUEUE

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    //TODO: ADD THE REQUEST METHOD TO THE REQUEST QUEUE

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

}
