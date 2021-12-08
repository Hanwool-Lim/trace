package com.example.tracking.Request;


import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class LogRequest extends StringRequest {

    final static private String URL = "http://122.46.129.53:727/Log.php";
    private Map<String, String> map;

    public LogRequest(String ServiceID, String Date, String DeviceID, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("ServiceID",ServiceID);
        map.put("Date",Date);
        map.put("DeviceID",DeviceID);

    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
