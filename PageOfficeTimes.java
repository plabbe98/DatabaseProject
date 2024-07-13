package com.example.movies;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.util.Objects;
public class PageOfficeTimes extends AppCompatActivity {

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        final String timesString = intent.getStringExtra("times");
        final String busytimesString = intent.getStringExtra("busytimes");
        final String instructor_id = intent.getStringExtra("id");
        final String checkedtimesString = intent.getStringExtra("checkedtimes");

        setContentView(R.layout.activity_page_office_times);
        TableLayout tableLayout = findViewById(R.id.tableTimes);
        Button buttonSave = (Button) findViewById(R.id.buttonSave);

        Button buttonLogout = (Button) findViewById(R.id.buttonLogout);
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PageOfficeTimes.this, PageLogin.class);
                PageOfficeTimes.this.startActivity(intent);
            }
        });

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

                                Intent intent = new Intent(PageOfficeTimes.this, PageMenu.class);

                                intent.putExtra("name", name);
                                intent.putExtra("id", id);
                                intent.putExtra("type", type);
                                intent.putExtra("email2", email2);
                                intent.putExtra("password2", password2);
                                PageOfficeTimes.this.startActivity(intent);
                            } else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(PageOfficeTimes.this);
                                builder.setMessage("Sign In Failed").setNegativeButton("Retry", null).create().show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                QueryRequest queryRequest = new QueryRequest(email2, password2, getString(R.string.url) + "login.php", responseListener);
                RequestQueue queue = Volley.newRequestQueue(PageOfficeTimes.this);
                queue.add(queryRequest);
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                JSONArray checkedCheckboxes = new JSONArray();
                JSONArray uncheckedCheckboxes = new JSONArray();
                int rowCount = tableLayout.getChildCount();
                for (int i = 1; i < rowCount; i++) {
                    View view = tableLayout.getChildAt(i);
                    if (view instanceof TableRow) {
                        TableRow row = (TableRow) view;
                        CheckBox checkBox = (CheckBox) row.getChildAt(2);
                        if(checkBox != null) {
                            TextView dayTextView = (TextView) row.getChildAt(0);
                            TextView timeTextView = (TextView) row.getChildAt(1);
                            String day = dayTextView.getText().toString();
                            String time = timeTextView.getText().toString();
                            if (checkBox.isChecked()) {
                                JSONObject checkedCheckbox = new JSONObject();
                                try {
                                    checkedCheckbox.put("day", day);
                                    checkedCheckbox.put("time", time);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                checkedCheckboxes.put(checkedCheckbox);
                            } else {
                                JSONObject uncheckedCheckbox = new JSONObject();
                                try {
                                    uncheckedCheckbox.put("day", day);
                                    uncheckedCheckbox.put("time", time);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                uncheckedCheckboxes.put(uncheckedCheckbox);
                            }
                        }
                    }
                }


                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("SubmitQueryHelp", response);
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if(success){
                                AlertDialog.Builder builder = new AlertDialog.Builder(PageOfficeTimes.this);
                                builder.setMessage("Office Hours Updated!").setNegativeButton("Okay", null).create().show();

                            } else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(PageOfficeTimes.this);
                                builder.setMessage("Error Updating Hours :(").setNegativeButton("Retry", null).create().show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                QueryRequest queryRequest = new QueryRequest(instructor_id, checkedCheckboxes, uncheckedCheckboxes, getString(R.string.url) + "updateHours.php", responseListener);
                RequestQueue queue = Volley.newRequestQueue(PageOfficeTimes.this);

                queue.add(queryRequest);
            }
        });

        TableRow headerRow = new TableRow(PageOfficeTimes.this);

        TextView dayHeader = new TextView(PageOfficeTimes.this);
        TextView timeHeader = new TextView(PageOfficeTimes.this);

        dayHeader.setText(" Day");
        dayHeader.setTypeface(null, Typeface.BOLD);
        dayHeader.setBackgroundResource(R.drawable.border);

        timeHeader.setText(" Time");
        timeHeader.setTypeface(null, Typeface.BOLD);
        timeHeader.setBackgroundResource(R.drawable.border);

        headerRow.addView(dayHeader, 200, 75);
        headerRow.addView(timeHeader, 200, 75);

        tableLayout.addView(headerRow);
        JSONArray jsonArray = null;
        JSONArray jsonArray2 = null;
        JSONArray jsonArray3 = null;
        try {
            jsonArray = new JSONArray(timesString);
            jsonArray2 = new JSONArray(busytimesString);
            jsonArray3 = new JSONArray(checkedtimesString);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        try {

            for (int i = 0; i < jsonArray.length(); i++) {
                TableRow tableRow = new TableRow(PageOfficeTimes.this);

                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String day = jsonObject.getString("day");
                Time time = Time.valueOf(jsonObject.getString("start_time"));
                Time endTime = Time.valueOf(jsonObject.getString("end_time"));
                String office_id = jsonObject.getString("office_id");

                boolean isOverlap = false; // Flag to track overlap

                for (int j = 0; j < jsonArray2.length(); j++) {
                    JSONObject jsonObject2 = jsonArray2.getJSONObject(j);
                    Time time2 = Time.valueOf(jsonObject2.getString("start_time"));
                    Time endTime2 = Time.valueOf(jsonObject2.getString("end_time"));

                    if ((time.before(endTime2) && endTime.after(time2)) ||
                            (time2.before(endTime) && endTime2.after(time))) {
                        isOverlap = true;
                        break;
                    }
                }

                if (!isOverlap) {
                    CheckBox checkBox = new CheckBox(PageOfficeTimes.this);

                    for (int k = 0; k < jsonArray3.length(); k++) {
                        JSONObject jsonObject3 = jsonArray3.getJSONObject(k);
                        String time_id = jsonObject3.getString("time_id");

                        if(office_id.equals(time_id)){
                            checkBox.setChecked(true);
                        }
                    }
                    TextView dayTextView = new TextView(PageOfficeTimes.this);
                    dayTextView.setText(day);

                    TextView timeTextView = new TextView(PageOfficeTimes.this);
                    timeTextView.setText(time.toString());

                    dayTextView.setBackgroundResource(R.drawable.border);
                    timeTextView.setBackgroundResource(R.drawable.border);

                    tableRow.addView(dayTextView, 200, 75);
                    tableRow.addView(timeTextView, 200, 75);
                    tableRow.addView(checkBox);

                    tableLayout.addView(tableRow);
                    tableRow.setClickable(true);

                    tableRow.setBackgroundResource(R.drawable.newtextview);
                }
            }
} catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}