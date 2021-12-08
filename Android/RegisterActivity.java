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
import com.example.tracking.Request.RegisterRequest;

import org.json.JSONException;
import org.json.JSONObject;



public class RegisterActivity extends Activity {

    private EditText registId, registPw, registName, registPhone, registNumber;
    private Button registerButton, registerBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        registId = (EditText)findViewById(R.id.registId);
        registPw = (EditText)findViewById(R.id.registPw);
        registName =(EditText)findViewById(R.id.registName);
        registPhone = (EditText)findViewById(R.id.registPhone);
        registNumber = (EditText)findViewById(R.id.registNumber);

        registerButton = (Button) findViewById(R.id.registerButton);
        registerBack = (Button) findViewById(R.id.registerBack);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userID = registId.getText().toString();
                String userPass = registPw.getText().toString();
                String userName = registName.getText().toString();
                int userPhone = Integer.parseInt(registPhone.getText().toString());
                int userNumber = Integer.parseInt(registNumber.getText().toString());


                Response.Listener<String> responsListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if(success){
                                Toast.makeText(getApplicationContext(),"회원등록에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(intent);
                            } else{
                                Toast.makeText(getApplicationContext(),"회원등록이 실패 하였습니다.", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                };



                RegisterRequest registerRequest = new RegisterRequest(userID,userPass,userName,userPhone,userNumber,responsListener);
                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                queue.add(registerRequest);

            }
        });

        registerBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                return;
            }
        });

    }


}
