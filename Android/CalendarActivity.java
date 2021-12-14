package com.example.tracking;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import androidx.annotation.NonNull;
import android.os.Bundle;
import androidx.annotation.RequiresApi;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.tracking.Request.LogDeleteRequest;
import com.example.tracking.Request.LogRequest;
import com.example.tracking.decorators.EventDecorator;
import com.example.tracking.decorators.OneDayDecorator;
import com.example.tracking.decorators.SaturdayDecorator;
import com.example.tracking.decorators.SundayDecorator;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executors;

public class CalendarActivity extends Activity {

    private final OneDayDecorator oneDayDecorator = new OneDayDecorator();
    Cursor cursor;
    MaterialCalendarView materialCalendarView;
    private TextView calenderText;
    private Button btn;
    String shot_Day;

    String service, device;

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);


        materialCalendarView = (MaterialCalendarView) findViewById(R.id.calendarView);
        calenderText = (TextView)findViewById(R.id.calenderText);
        btn = (Button)findViewById(R.id.text_delete);


        materialCalendarView.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setMinimumDate(CalendarDay.from(2017, 00, 01)) // 달력의 시작
                .setMaximumDate(CalendarDay.from(2030, 11, 31)) // 달력의 끝
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();

        materialCalendarView.addDecorators(
                new SundayDecorator(),
                new SaturdayDecorator(),
                oneDayDecorator);

        String[] result = {"2017,03,18", "2017,04,18", "2017,05,18", "2017,06,18"};

        new ApiSimulator(result).executeOnExecutor(Executors.newSingleThreadExecutor());

        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {



                int Year = date.getYear();
                int Month = date.getMonth() + 1;
                int Day = date.getDay();

//                Log.i("Year test", Year + "");
//                Log.i("Month test", Month + "");
//                Log.i("Day test", Day + "");
                
                if (Month < 10 && Day > 10){
                    shot_Day = Year + "-" + "0"+ Month + "-" + Day;
                }else if(Month < 10 && Day <10){
                    shot_Day = Year + "-" + "0"+ Month + "-" + "0" + Day;
                }else if(Month > 10 && Day < 10){
                    shot_Day = Year + "-" +  Month + "-" + "0" + Day;
                }else{
                    shot_Day = Year + "-" + Month + "-" + Day;
                }
                
                
                

                shot_Day = Year + "-" + Month + "-" + Day;
                Intent intent = getIntent();
                service = intent.getStringExtra("ServiceID");
                device = intent.getStringExtra("DeviceID");

//                Log.i("shot_Day test", shot_Day + "");

                materialCalendarView.clearSelection();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONArray jsonArray = new JSONArray (response);

                            ArrayList<String> trackingLog = new ArrayList<String>();

                            for(int i=0; i<jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                String Date =  jsonObject.getString("Date");
                                String AgentID =  jsonObject.getString("AgentID");
                                String ServiceID =  jsonObject.getString("ServiceID");
                                String FileID =  jsonObject.getString("FileID");
                                String IO =  jsonObject.getString("IO");
                                String Time =  jsonObject.getString("Time");

                                trackingLog.add(Date);
                                trackingLog.add(Time);
                                trackingLog.add(AgentID);
                                trackingLog.add(ServiceID);
                                trackingLog.add(FileID);
                                trackingLog.add(IO);
                                trackingLog.add("\n");



                            }
                            calenderText.setText(trackingLog.toString());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                LogRequest logRequest = new LogRequest(service, shot_Day, device, responseListener);
                RequestQueue queue = Volley.newRequestQueue(CalendarActivity.this);
                queue.add(logRequest);
            }

        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if(success){
                                Toast.makeText(getApplicationContext(),"로그 삭제에 성공하였습니다.", Toast.LENGTH_SHORT).show();

                            }else{
                                Toast.makeText(getApplicationContext(),"로그 삭제에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                                return;
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                LogDeleteRequest logDeleteRequest = new LogDeleteRequest(shot_Day, device ,service,responseListener);
                RequestQueue queue = Volley.newRequestQueue(CalendarActivity.this);
                queue.add(logDeleteRequest);


            }
        });
    }

    private class ApiSimulator extends AsyncTask<Void, Void, List<CalendarDay>> {

        String[] Time_Result;

        ApiSimulator(String[] Time_Result) {
            this.Time_Result = Time_Result;
        }

        @Override
        protected List<CalendarDay> doInBackground(@NonNull Void... voids) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Calendar calendar = Calendar.getInstance();
            ArrayList<CalendarDay> dates = new ArrayList<>();

            /*특정날짜 달력에 점표시해주는곳*/
            /*월은 0이 1월 년,일은 그대로*/
            //string 문자열인 Time_Result 을 받아와서 ,를 기준으로짜르고 string을 int 로 변환
            for(int i = 0; i < Time_Result.length; i++) {
                CalendarDay day = CalendarDay.from(calendar);
                String[] time = Time_Result[i].split(",");
                int year = Integer.parseInt(time[0]);
                int month = Integer.parseInt(time[1]);
                int dayy = Integer.parseInt(time[2]);

                dates.add(day);
                calendar.set(year, month - 1, dayy);
            }


            return dates;
        }

        @Override
        protected void onPostExecute(@NonNull List<CalendarDay> calendarDays) {
            super.onPostExecute(calendarDays);

            if (isFinishing()) {
                return;
            }

            materialCalendarView.addDecorator(new EventDecorator(Color.GREEN, calendarDays, CalendarActivity.this));
        }
    }
}
