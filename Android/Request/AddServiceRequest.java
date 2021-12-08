package com.example.tracking.Request;


import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class AddServiceRequest extends StringRequest {

    final static private String URL = "http://122.46.129.53:727/AddService.php";
    private Map<String, String> map;

    public AddServiceRequest(String ServiceID, String DeviceID,  String UserID ,Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("ServiceID",ServiceID);
        map.put("DeviceID",DeviceID);
        map.put("UserID",UserID);

    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}