package com.example.tracking.Request;


import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class AddDeviceRequest extends StringRequest {

    final static private String URL = "http://ip:port/AddDevice.php";
    private Map<String, String> map;

    public AddDeviceRequest(String DeviceID, String UserID, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("DeviceID",DeviceID);
        map.put("UserID",UserID);

    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
