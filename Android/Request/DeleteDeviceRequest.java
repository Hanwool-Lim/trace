package com.example.tracking.Request;


import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class DeleteDeviceRequest extends StringRequest {

    final static private String URL = "http://ip:port/DeviceDelete.php";
    private Map<String, String> map;

    public DeleteDeviceRequest(String DeviceID, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("DeviceID",DeviceID);


    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
