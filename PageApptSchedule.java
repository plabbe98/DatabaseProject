package com.example.movies;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PageApptSchedule extends Activity implements AdapterView.OnItemSelectedListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_schedule);

        Button buttonLogout = (Button) findViewById(R.id.buttonLogout);
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PageApptSchedule.this, PageLogin.class);
                PageApptSchedule.this.startActivity(intent);
            }
        });
        final Spinner spinner =  (Spinner) findViewById(R.id.dropdownCourse);
        spinner.setOnItemSelectedListener(this);

        Intent intent = getIntent();
        final String id = intent.getStringExtra("student_id");

        final String email2 = intent.getStringExtra("email2");
        final String password2 = intent.getStringExtra("password2");
        Button buttonBack = (Button) findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("SubmitQueryHelp", response);
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if(success){
                                String name = jsonResponse.getString("name");
                                String id = jsonResponse.getString("id");
                                String type = jsonResponse.getString("type");

                                Intent intent = new Intent(PageApptSchedule.this, PageMenu.class);

                                intent.putExtra("name", name);
                                intent.putExtra("id", id);
                                intent.putExtra("type", type);
                                intent.putExtra("email2", email2);
                                intent.putExtra("password2", password2);
                                PageApptSchedule.this.startActivity(intent);
                            } else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(PageApptSchedule.this);
                                builder.setMessage("Sign In Failed").setNegativeButton("Retry", null).create().show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                QueryRequest queryRequest = new QueryRequest(email2, password2, getString(R.string.url) + "login.php", responseListener);
                RequestQueue queue = Volley.newRequestQueue(PageApptSchedule.this);
                queue.add(queryRequest);
            }
        });

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("SubmitQueryHelp", response);
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if(success){
                                JSONArray courses = jsonResponse.getJSONArray("courses");
                                List<String> categories = new ArrayList<String>();
                                for (int i = 0; i < courses.length(); i++) {
                                    JSONObject courseObject = courses.getJSONObject(i);
                                    String courseId = courseObject.getString("course_id");
                                    categories.add(courseId);
                                }


                                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, categories);
                                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner.setAdapter(dataAdapter);


                            } else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(PageApptSchedule.this);
                                builder.setMessage("Sign In Failed").setNegativeButton("Retry", null).create().show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                QueryRequest queryRequest = new QueryRequest(id, getString(R.string.url) + "checkCourses.php", responseListener);
                RequestQueue queue = Volley.newRequestQueue(PageApptSchedule.this);
                queue.add(queryRequest);


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("SubmitQueryHelp", response);
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");

                    if(success){
                        TableLayout tableLayout = findViewById(R.id.tableInfo);
                        tableLayout.removeAllViews();

                        TableRow headerRow = new TableRow(PageApptSchedule.this);

                        TextView dayHeader = new TextView(PageApptSchedule.this);
                        TextView timeHeader = new TextView(PageApptSchedule.this);

                        dayHeader.setText(" Days");
                        dayHeader.setTypeface(null, Typeface.BOLD);
                        dayHeader.setBackgroundResource(R.drawable.border);

                        timeHeader.setText(" Times");
                        timeHeader.setTypeface(null, Typeface.BOLD);
                        timeHeader.setBackgroundResource(R.drawable.border);

                        headerRow.addView(timeHeader, 200, 75);
                        headerRow.addView(dayHeader, 200, 75);

                        tableLayout.addView(headerRow);
                        try {

                            JSONArray jsonArray = jsonResponse.getJSONArray("times");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                TableRow tableRow = new TableRow(PageApptSchedule.this);

                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                String day = jsonObject.getString("day");
                                String time = jsonObject.getString("start_time");

                                TextView dayTextView = new TextView(PageApptSchedule.this);
                                dayTextView.setText(day);

                                TextView timeTextView = new TextView(PageApptSchedule.this);
                                timeTextView.setText(time);

                                dayTextView.setBackgroundResource(R.drawable.border);
                                timeTextView.setBackgroundResource(R.drawable.border);

                                tableRow.addView(dayTextView, 200, 75);
                                tableRow.addView(timeTextView, 200, 75);

                                tableLayout.addView(tableRow);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(PageApptSchedule.this);
                        builder.setMessage("Sign In Failed").setNegativeButton("Retry", null).create().show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        QueryRequest queryRequest = new QueryRequest(item, getString(R.string.url) + "checkAvailTimes.php", responseListener);
        RequestQueue queue = Volley.newRequestQueue(PageApptSchedule.this);
        queue.add(queryRequest);
    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }
}
