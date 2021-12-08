package com.example.tracking;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.tracking.Request.LoginRequest;


import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.security.MessageDigest;

public class LoginActivity extends Activity {

    private EditText Userid, Userpw;
    private Button loginbutton, registration;
    private String saltHash, hashPw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);


        Userid = (EditText)findViewById(R.id.userid);
        Userpw = (EditText)findViewById(R.id.userpw);

        loginbutton = (Button)findViewById(R.id.loginbutton);
        registration = (Button)findViewById(R.id.registration);



        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String UserID = Userid.getText().toString();
                String UserPass = Userpw.getText().toString();

                saltHash = "sejongWonjun"+UserPass+"sejongHanwul";
                SHA512_Hash_InCode hash_inCode = new SHA512_Hash_InCode();
                hashPw = hash_inCode.SHA512_Hash_InCode(saltHash);


                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if(success){
                                String UserID = jsonObject.getString("UserID");
                                String UserName = jsonObject.getString("UserName");

                                Toast.makeText(getApplicationContext(),"로그인에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, DeviceActivity.class);
                                //전달할 내용.
                                intent.putExtra("UserID",UserID);
                                intent.putExtra("UserName",UserName);

                                startActivity(intent);
                            } else{
                                Toast.makeText(getApplicationContext(),"로그인에 실패 하였습니다.", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                LoginRequest loginRequest = new LoginRequest(UserID,hashPw,responseListener);
                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                queue.add(loginRequest);
            }
        });

    }

    public class SHA512_Hash_InCode{
        public  String SHA512_Hash_InCode(String pwd){
            try{
                String hexSapassword = pwd;
                String mixPassword = hexSapassword;
                MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
                messageDigest.reset();
                messageDigest.update(mixPassword.getBytes("utf8"));
                String enPassword = String.format("%0128x", new BigInteger(1, messageDigest.digest()));
                return enPassword;
            } catch (Exception e){
                throw new RuntimeException(e);
            }
        }
    }

}
