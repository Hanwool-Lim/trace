package com.example.tracking.Request;


import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class LogDeleteRequest extends StringRequest {

    final static private String URL = "http://122.46.129.53:727/LogDelete.php";
    private Map<String, String> map;

    public LogDeleteRequest(String Date, String DeviceID, String ServiceID, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("Date",Date);
        map.put("DeviceID",DeviceID);
        map.put("ServiceID",ServiceID);


    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}