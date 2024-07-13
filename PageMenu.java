package com.example.movies;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class PageMenu extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        final String name = intent.getStringExtra("name");
        final String id = intent.getStringExtra("id");
        final String type = intent.getStringExtra("type");
        final String email2 = intent.getStringExtra("email2");
        final String password2 = intent.getStringExtra("password2");

        if (Objects.equals(type, "student")) {
            setContentView(R.layout.activity_page_menu);
            Button register = (Button) findViewById(R.id.ButtonRegister);
            Button mygrades = (Button) findViewById(R.id.ButtonCourses);
            Button buttonAppt = (Button) findViewById(R.id.buttonAppt);
            Button buttonLogout = (Button) findViewById(R.id.buttonLogout);

            register.setOnClickListener(new View.OnClickListener() {
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
                                    JSONArray courses = jsonResponse.getJSONArray("courses");
                                    String coursesString = courses.toString();
                                    Intent intent = new Intent(PageMenu.this, PageRegister.class);

                                    intent.putExtra("courses", coursesString);
                                    intent.putExtra("id", id);
                                    intent.putExtra("email2", email2);
                                    intent.putExtra("password2", password2);
                                    PageMenu.this.startActivity(intent);
                                } else{
                                    AlertDialog.Builder builder = new AlertDialog.Builder(PageMenu.this);
                                    builder.setMessage("Sign In Failed").setNegativeButton("Retry", null).create().show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };

                    QueryRequest queryRequest = new QueryRequest(id, getString(R.string.url) + "courses.php", responseListener);
                    RequestQueue queue = Volley.newRequestQueue(PageMenu.this);
                    queue.add(queryRequest);
                }
            });
            mygrades.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){

                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                Log.d("SubmitQueryHelp", response);
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");

                                if(success){
                                    JSONArray courses = jsonResponse.getJSONArray("courses");
                                    String coursesString = courses.toString();
                                    Intent intent = new Intent(PageMenu.this, PageGrades.class);
                                    intent.putExtra("id", id);
                                    intent.putExtra("courses", coursesString);
                                    intent.putExtra("email2", email2);
                                    intent.putExtra("password2", password2);

                                    PageMenu.this.startActivity(intent);
                                } else{
                                    AlertDialog.Builder builder = new AlertDialog.Builder(PageMenu.this);
                                    builder.setMessage("Sign In Failed").setNegativeButton("Retry", null).create().show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };

                    QueryRequest queryRequest = new QueryRequest(id, getString(R.string.url) + "grades.php", responseListener);
                    RequestQueue queue = Volley.newRequestQueue(PageMenu.this);
                    queue.add(queryRequest);
                }
            });
            buttonAppt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    Intent intent = new Intent(PageMenu.this, PageApptSchedule.class);
                    intent.putExtra("student_id", id);
                    intent.putExtra("email2", email2);
                    intent.putExtra("password2", password2);
                    PageMenu.this.startActivity(intent);
                }
            });
            buttonLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(PageMenu.this, PageLogin.class);
                    PageMenu.this.startActivity(intent);
                }
            });
            TextView tvName = (TextView) findViewById(R.id.tvMenuName);
            TextView tvID = (TextView) findViewById(R.id.tvMenuID);

            tvName.setText(name);
            tvID.setText(id);
        }
        else if (Objects.equals(type, "instructor")) {
            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        Log.d("SubmitQueryHelp", response);
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");

                        if(success){
                            JSONArray jsonArray = jsonResponse.getJSONArray("courses");

                            setContentView(R.layout.activity_page_instructor_menu);
                            TableLayout tableLayout = findViewById(R.id.tableI);
                            TableRow titleRow = new TableRow(PageMenu.this);

                            TextView nameHeader = new TextView(PageMenu.this);
                            TextView idHeader = new TextView(PageMenu.this);

                            nameHeader.setText(" Instructor: " + name);
                            nameHeader.setTypeface(null, Typeface.BOLD);
                            nameHeader.setBackgroundResource(R.drawable.border);

                            idHeader.setText(" ID: " + id);
                            idHeader.setTypeface(null, Typeface.BOLD);
                            idHeader.setBackgroundResource(R.drawable.border);

                            titleRow.addView(nameHeader, 300, 100);
                            titleRow.addView(idHeader, 200, 100);
                            tableLayout.addView(titleRow);
                            TableRow headerRow = new TableRow(PageMenu.this);

                            TextView courseIdHeader = new TextView(PageMenu.this);
                            TextView sectionIdHeader = new TextView(PageMenu.this);

                            courseIdHeader.setText(" Course ID");
                            courseIdHeader.setTypeface(null, Typeface.BOLD);
                            courseIdHeader.setBackgroundResource(R.drawable.border);

                            sectionIdHeader.setText(" Section ID");
                            sectionIdHeader.setTypeface(null, Typeface.BOLD);
                            sectionIdHeader.setBackgroundResource(R.drawable.border);

                            headerRow.addView(courseIdHeader, 200, 75);
                            headerRow.addView(sectionIdHeader, 200, 75);

                            tableLayout.addView(headerRow);
                            try {

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    TableRow tableRow = new TableRow(PageMenu.this);

                                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                                    String course_id = jsonObject.getString("course_id");
                                    String section_id = jsonObject.getString("section_id");

                                    TextView course_idTextView = new TextView(PageMenu.this);
                                    course_idTextView.setText(course_id);

                                    TextView section_idTextView = new TextView(PageMenu.this);
                                    section_idTextView.setText(section_id);

                                    course_idTextView.setBackgroundResource(R.drawable.border);
                                    section_idTextView.setBackgroundResource(R.drawable.border);

                                    tableRow.addView(course_idTextView, 200, 75);
                                    tableRow.addView(section_idTextView, 200, 75);

                                    tableLayout.addView(tableRow);
                                    tableRow.setClickable(true);

                                    tableRow.setBackgroundResource(R.drawable.newtextview);

                                    tableRow.setOnClickListener(new View.OnClickListener() {
                                        public void onClick(View view) {
                                            TableRow t = (TableRow) view;
                                            TextView firstTextView = (TextView) t.getChildAt(0);
                                            TextView secondTextView = (TextView) t.getChildAt(1);
                                            String c_id = firstTextView.getText().toString();
                                            String s_id = secondTextView.getText().toString();
                                            Response.Listener<String> responseListener = new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    try {
                                                        Log.d("SubmitQueryHelp", response);
                                                        JSONObject jsonResponse = new JSONObject(response);
                                                        boolean success = jsonResponse.getBoolean("success");

                                                        if(success){
                                                            JSONArray current = jsonResponse.getJSONArray("courses");
                                                            //JSONArray past = jsonResponse.getJSONArray("past");

                                                            String currentString = current.toString();
                                                            //String pastString = past.toString();

                                                            Intent intent = new Intent(PageMenu.this, PageSectionInfo.class);

                                                            intent.putExtra("current", currentString);
                                                            intent.putExtra("c_id", c_id);
                                                            intent.putExtra("s_id", s_id);
                                                            intent.putExtra("email2", email2);
                                                            intent.putExtra("password2", password2);

                                                            PageMenu.this.startActivity(intent);
                                                        } else{
                                                            AlertDialog.Builder builder = new AlertDialog.Builder(PageMenu.this);
                                                            builder.setMessage("Sign In Failed").setNegativeButton("Retry", null).create().show();
                                                        }
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            };

                                            QueryRequest queryRequest = new QueryRequest(id, c_id, s_id, getString(R.string.url) + "sectionInfo.php", responseListener);
                                            RequestQueue queue = Volley.newRequestQueue(PageMenu.this);
                                            queue.add(queryRequest);
                                        }
                                    });

                                    Button buttonOfficeHours = (Button) findViewById(R.id.buttonOfficeHours);
                                    Button buttonLogout = (Button) findViewById(R.id.buttonLogout);
                                    buttonLogout.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(PageMenu.this, PageLogin.class);
                                            PageMenu.this.startActivity(intent);
                                        }
                                    });

                                    buttonOfficeHours.setOnClickListener(new View.OnClickListener() {
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
                                                            JSONArray times = jsonResponse.getJSONArray("times");
                                                            String timesString = times.toString();
                                                            JSONArray busytimes = jsonResponse.getJSONArray("busy");
                                                            String busytimesString = busytimes.toString();
                                                            JSONArray checkedtimes = jsonResponse.getJSONArray("checked");
                                                            String checkedtimesString = checkedtimes.toString();

                                                            Intent intent = new Intent(PageMenu.this, PageOfficeTimes.class);

                                                            intent.putExtra("times", timesString);
                                                            intent.putExtra("busytimes", busytimesString);
                                                            intent.putExtra("checkedtimes", checkedtimesString);
                                                            intent.putExtra("id", id);
                                                            intent.putExtra("email2", email2);
                                                            intent.putExtra("password2", password2);

                                                            PageMenu.this.startActivity(intent);
                                                        } else{
                                                            AlertDialog.Builder builder = new AlertDialog.Builder(PageMenu.this);
                                                            builder.setMessage("Sign In Failed").setNegativeButton("Retry", null).create().show();
                                                        }
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            };

                                            QueryRequest queryRequest = new QueryRequest(id, getString(R.string.url) + "officeTimes.php", responseListener);
                                            RequestQueue queue = Volley.newRequestQueue(PageMenu.this);
                                            queue.add(queryRequest);
                                        }
                                    });
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        } else{
                            AlertDialog.Builder builder = new AlertDialog.Builder(PageMenu.this);
                            builder.setMessage("Sign In Failed").setNegativeButton("Retry", null).create().show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };

            QueryRequest queryRequest = new QueryRequest(id, getString(R.string.url) + "instructor.php", responseListener);
            RequestQueue queue = Volley.newRequestQueue(PageMenu.this);
            queue.add(queryRequest);
        }
    }

}
