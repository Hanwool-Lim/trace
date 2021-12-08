package com.example.tracking;


import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class DeviceRequest extends StringRequest {

    final static private String URL = "http://192.168.0.26/Device.php";
    private Map<String, String> map;

    public DeviceRequest(String UserID, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("UserID",UserID);

    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
