package com.example.smart_alert;

import android.content.Context;
import android.os.StrictMode;

import androidx.browser.trusted.sharing.ShareTarget;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FCMSend {
    private static String BASE_URL = "https://fcm.googleapis.com/fcm/send";
    private static String SERVER_KEY = "key=AAAA84FX9Wk:APA91bE-YnORs_317TYu7kGeXBAKTsHK5ug4TaIS1i_RQizARI7qb9Cyi7SwfZlgYIsT-M5zCKuwhpYMwo_osBuMedcrE1qiqre40yEXSd3bNo64PMCSo6h0atdVx0b0AjRdb1Fu3AUm";

    public static void pushNotification(Context context, String token,String title, String message){

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            RequestQueue queue = Volley.newRequestQueue(context);
            try{
                JSONObject json = new JSONObject();
                json.put("to",token);
                JSONObject notification = new JSONObject();
                notification.put("title",title);
                notification.put("body",message);
                json.put("notification",notification);

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, BASE_URL, json, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("FCM " + response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
                ){
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String,String> params = new HashMap<>();
                        params.put("Content-Type","application/json");
                        params.put("Authorization",SERVER_KEY);
                        return params;
                    }
                };
                queue.add(jsonObjectRequest);
            }catch (JSONException e){
                e.printStackTrace();
            }

    }

}
