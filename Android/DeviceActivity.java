package com.example.tracking;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.tracking.Request.DeleteDeviceRequest;
import com.example.tracking.Request.DeleteUserRequest;
import com.example.tracking.Request.DeviceRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DeviceActivity extends AppCompatActivity {

    private TextView tv;
    private Button button1, button2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);

        button1 = (Button) findViewById(R.id.button1); // 디바이스 확인
        button2 = (Button) findViewById(R.id.button2); // 회원 탈퇴
        tv = (TextView) findViewById(R.id.textView1);


        Intent intent = getIntent();
        final  String UserID = intent.getStringExtra("UserID");
        final String UserName = intent.getStringExtra("UserName");

        tv.setText(UserID + " , " + UserName + " 님 ");

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                                JSONArray jsonArray = new JSONArray (response);

                                //Toast.makeText(getApplicationContext(),"디바이스 확인", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(DeviceActivity.this, Device_ItemActivity.class);

                                ArrayList<String> device = new ArrayList<String>();

                                for(int i=0; i<jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                String DeviceID =  jsonObject.getString("DeviceID");

                                device.add(DeviceID);

                                }
                                intent.putExtra("DeviceID",device);
                                intent.putExtra("UserID",UserID);


                                startActivity(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                DeviceRequest deviceRequest = new DeviceRequest(UserID,responseListener);
                RequestQueue queue = Volley.newRequestQueue(DeviceActivity.this);
                queue.add(deviceRequest);

            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(DeviceActivity.this);

                builder.setTitle("회원 탈퇴").setMessage("정말 탈퇴 하시겠습니까?" );

                builder.setPositiveButton("탈퇴", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int id)
                    {
                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {

                                    JSONObject jsonObject = new JSONObject(response);
                                    boolean success = jsonObject.getBoolean("success");
                                    if(success){
                                        Toast.makeText(getApplicationContext(),"회원 탈퇴에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(DeviceActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                    }else{
                                        Toast.makeText(getApplicationContext(),"회원 탈퇴에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                                        return;
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        };

                        DeleteUserRequest deleteUserRequest = new DeleteUserRequest(UserID, responseListener);
                        RequestQueue queue = Volley.newRequestQueue(DeviceActivity.this);
                        queue.add(deleteUserRequest);
                    }
                });

                builder.setNegativeButton("취소", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int id)
                    {
                        Toast.makeText(getApplicationContext(), "취소 되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();


            }
        });

    }

}