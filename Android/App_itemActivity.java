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
import com.example.tracking.Request.AddServiceRequest;
import com.example.tracking.Request.DeleteAppRequest;
import com.example.tracking.Request.DeleteUserRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class App_itemActivity extends AppCompatActivity {

    private ListView listView;
    private Button btn;
    private EditText et;
    List<String> data = new ArrayList<>();
    String device;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_item);

        listView = (ListView)findViewById(R.id.applist);
        btn = (Button)findViewById(R.id.addService);
        et = (EditText)findViewById(R.id.addServiceID);


        Intent intent = getIntent();
        ArrayList<String> app = (ArrayList<String>)intent.getSerializableExtra("ServiceID");
        device = intent.getStringExtra("DeviceID");
        final String UserID = intent.getStringExtra("UserID");

        adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,data);

        listView.setAdapter(adapter);

        if(intent != null){
            if(app != null){
                for(int i=0 ; i<app.size();i++){
                    //리스트 뷰에 추가.
                    data.add(app.get(i));
                    adapter.notifyDataSetChanged();
                }

            }
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String ServiceID = et.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if(success){

                                Toast.makeText(getApplicationContext(),"App 추가에 성공하였습니다.", Toast.LENGTH_SHORT).show();

                            } else{
                                Toast.makeText(getApplicationContext(),"App 추가에 실패하였습니다", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                AddServiceRequest addServiceRequest = new AddServiceRequest(ServiceID,device, UserID, responseListener);
                RequestQueue queue = Volley.newRequestQueue(App_itemActivity.this);
                queue.add(addServiceRequest);

                adapter.notifyDataSetChanged();

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent1 = new Intent(App_itemActivity.this, CalendarActivity.class);
                intent1.putExtra("ServiceID",data.get(position));
                intent1.putExtra("DeviceID",device);
                Toast.makeText(getApplicationContext(),data.get(position)+"정보가 넘어갔습니다", Toast.LENGTH_SHORT).show();

                startActivity(intent1);

            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

                builder.setTitle("어플리케이션 제거").setMessage( data.get(position) + "을 삭제 하시겠습니까?" );

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
                                        Toast.makeText(getApplicationContext(),"App 삭제에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(getApplicationContext(),"App 삭제에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                                        return;
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        };

                        DeleteAppRequest deleteAppRequest = new DeleteAppRequest(device, data.get(position), responseListener);
                        RequestQueue queue = Volley.newRequestQueue(App_itemActivity.this);
                        queue.add(deleteAppRequest);

                        adapter.notifyDataSetChanged();
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