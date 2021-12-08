package com.example.tracking;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.tracking.Request.AddDeviceRequest;
import com.example.tracking.Request.DeleteAppRequest;
import com.example.tracking.Request.DeleteDeviceRequest;
import com.example.tracking.Request.ServiceRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Device_ItemActivity extends AppCompatActivity {

    private ListView listView;
    private EditText et;
    private Button btn;
    List<String> data = new ArrayList<>();
    ArrayAdapter<String> adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device__item);


        listView = (ListView)findViewById(R.id.list);
        btn =(Button)findViewById(R.id.addDevice);
        et = (EditText)findViewById(R.id.addDeviceID);


        Intent intent = getIntent();
        final String UserID = intent.getStringExtra("UserID");
        final ArrayList<String> device = (ArrayList<String>)intent.getSerializableExtra("DeviceID");

        adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,data);

        listView.setAdapter(adapter);

        if(intent != null){
            if(device != null){
                for(int i=0 ; i<device.size();i++){
                    //리스트 뷰에 추가.
                    data.add(device.get(i));
                }
                adapter.notifyDataSetChanged();

            }
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String DeviceID = et.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if(success){

                                Toast.makeText(getApplicationContext(),"디바이스 추가에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                                adapter.notifyDataSetChanged();


                            } else{
                                Toast.makeText(getApplicationContext(),"디바이스 추가에 실패하였습니다", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                AddDeviceRequest addDeviceRequest = new AddDeviceRequest(DeviceID,UserID,responseListener);
                RequestQueue queue = Volley.newRequestQueue(Device_ItemActivity.this);
                queue.add(addDeviceRequest);



            }

        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONArray jsonArray = new JSONArray (response);

                            //Toast.makeText(getApplicationContext(),"디바이스 확인", Toast.LENGTH_SHORT).show();
                            Intent intent1 = new Intent(Device_ItemActivity.this, App_itemActivity.class);

                            ArrayList<String> service = new ArrayList<String>();


                            for(int i=0; i<jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                String ServiceID =  jsonObject.getString("ServiceID");

                                service.add(ServiceID);

                            }
                            intent1.putExtra("ServiceID",service);
                            intent1.putExtra("DeviceID",data.get(position));
                            intent1.putExtra("UserID",UserID);


                            startActivity(intent1);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };


                ServiceRequest serviceRequest = new ServiceRequest(data.get(position),responseListener);
                RequestQueue queue = Volley.newRequestQueue(Device_ItemActivity.this);
                queue.add(serviceRequest);

                Toast.makeText(getApplicationContext(),data.get(position)+"정보가 넘어갔습니다", Toast.LENGTH_SHORT).show();

            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

                builder.setTitle("디바이스 제거").setMessage( data.get(position) + "을 삭제 하시겠습니까?" );

                builder.setPositiveButton("삭제", new DialogInterface.OnClickListener(){
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
                                        Toast.makeText(getApplicationContext(),"디바이스 삭제에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                                        adapter.notifyDataSetChanged();
                                    }else{
                                        Toast.makeText(getApplicationContext(),"디바이스 삭제에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                                        return;
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        };

                        DeleteDeviceRequest deleteDeviceRequest = new DeleteDeviceRequest(data.get(position), responseListener);
                        RequestQueue queue = Volley.newRequestQueue(Device_ItemActivity.this);
                        queue.add(deleteDeviceRequest);


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

                return true;
            }
        });



   }



}