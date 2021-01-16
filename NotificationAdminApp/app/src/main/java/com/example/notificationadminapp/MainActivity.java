package com.example.notificationadminapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText header,body;
    Button send;
    public String url = "https://fcm.googleapis.com/fcm/send";
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        header = findViewById(R.id.header);
        body = findViewById(R.id.body);
        send = findViewById(R.id.send);

        requestQueue = Volley.newRequestQueue(this);

        FirebaseMessaging.getInstance().subscribeToTopic("news");

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String s_header = header.getText().toString();
                String s_body = body.getText().toString();

                if (s_header.isEmpty() || s_body.isEmpty()){
                    Toast.makeText(MainActivity.this, "Field should not empty", Toast.LENGTH_SHORT).show();
                } else {

                    send.setEnabled(false);
                    sendNotification(s_header,s_body);

                }
            }
        });
    }

    private void sendNotification(String header,String body){

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("to","/topics/"+"news");

            JSONObject jsonNotification = new JSONObject();

            jsonNotification.put("head",header);
            jsonNotification.put("title",body);
            jsonNotification.put("sound","default");

            jsonObject.put("notification",jsonNotification);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    url,
                    jsonObject,
                    new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String,String> header = new HashMap<>();
                    header.put("content-type","application/json");
                    header.put("authorization","key=AAAAFIJXB-E:APA91bEC4StafZCJu8qrRwBxNMw8VHa4G-maUx0TrDkzYu8aoYT38zeq_sfDyVDJlk47QFYOhllRTgZERbq0uLOKAnmZ5lhzCy0oP5ir1yhhts7AsFCsS6E6r9rAxMnP0-mSWs_aP7uc");
                    return header;
                }
            };

            requestQueue.add(jsonObjectRequest);
            send.setEnabled(true);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}