package com.wisdomlanna.testcamera;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;


public class LogUserDataImage {
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    Context context;

    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "LogUserFormData";

    public LogUserDataImage(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    private String getKey(int userId, String branchId, String formId) {
        return userId + "_" + branchId + "_" + formId;
    }

    public String[] getData(int userId, String branchId, String formId, int size) {
        String key = getKey(userId, branchId, formId);
        String data = pref.getString(key, null);
        if (data == null) {
            return new String[size];
        } else {
            Log.i("BANK", "get : " + key + " => " + data);
            return new Gson().fromJson(data, String[].class);
        }
    }

    public void setData(int userId, String branchId, String formId, String[] data) {
        Log.i("BANK", "save : " + getKey(userId, branchId, formId) + " <= " + new Gson().toJson(data));
        editor.putString(getKey(userId, branchId, formId), new Gson().toJson(data)).commit();
    }

    public void removeData(int userId, String branchId, String formId) {
        editor.remove(getKey(userId, branchId, formId)).commit();
    }
}