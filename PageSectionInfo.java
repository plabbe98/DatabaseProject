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

public class PageSectionInfo extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        final String c_id = intent.getStringExtra("c_id");
        final String s_id = intent.getStringExtra("s_id");
        final String current = intent.getStringExtra("current");

        setContentView(R.layout.activity_page_sectioninfo);

        Button buttonLogout = (Button) findViewById(R.id.buttonLogout);
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PageSectionInfo.this, PageLogin.class);
                PageSectionInfo.this.startActivity(intent);
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

                                Intent intent = new Intent(PageSectionInfo.this, PageMenu.class);

                                intent.putExtra("name", name);
                                intent.putExtra("id", id);
                                intent.putExtra("type", type);
                                intent.putExtra("email2", email2);
                                intent.putExtra("password2", password2);
                                PageSectionInfo.this.startActivity(intent);
                            } else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(PageSectionInfo.this);
                                builder.setMessage("Sign In Failed").setNegativeButton("Retry", null).create().show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                QueryRequest queryRequest = new QueryRequest(email2, password2, getString(R.string.url) + "login.php", responseListener);
                RequestQueue queue = Volley.newRequestQueue(PageSectionInfo.this);
                queue.add(queryRequest);
            }
        });

        TableLayout tableLayout2 = findViewById(R.id.tableInfoPast);
        TableLayout tableLayout = findViewById(R.id.tableInfoCurrent);

        //TITLES
        TextView title = findViewById(R.id.tvTitle);
        title.setText(c_id + " " +  s_id);
        title.setTypeface(null, Typeface.BOLD);

        //HEADERS
        TableRow headerRow = new TableRow(PageSectionInfo.this);
        TextView nameHeader = new TextView(PageSectionInfo.this);
        TextView semesterHeader = new TextView(PageSectionInfo.this);
        TextView yearHeader = new TextView(PageSectionInfo.this);
        nameHeader.setText(" Student");
        nameHeader.setTypeface(null, Typeface.BOLD);
        nameHeader.setBackgroundResource(R.drawable.border);
        semesterHeader.setText(" Semester");
        semesterHeader.setTypeface(null, Typeface.BOLD);
        semesterHeader.setBackgroundResource(R.drawable.border);
        yearHeader.setText(" Year");
        yearHeader.setTypeface(null, Typeface.BOLD);
        yearHeader.setBackgroundResource(R.drawable.border);
        headerRow.addView(nameHeader, 200, 75);
        headerRow.addView(semesterHeader, 200, 75);
        headerRow.addView(yearHeader, 150, 75);

        tableLayout.addView(headerRow);

        TableRow headerRow2 = new TableRow(PageSectionInfo.this);
        TextView nameHeader2 = new TextView(PageSectionInfo.this);
        TextView semesterHeader2 = new TextView(PageSectionInfo.this);
        TextView yearHeader2 = new TextView(PageSectionInfo.this);
        nameHeader2.setText(" Student");
        nameHeader2.setTypeface(null, Typeface.BOLD);
        nameHeader2.setBackgroundResource(R.drawable.border);
        semesterHeader2.setText(" Semester");
        semesterHeader2.setTypeface(null, Typeface.BOLD);
        semesterHeader2.setBackgroundResource(R.drawable.border);
        yearHeader2.setText(" Year");
        yearHeader2.setTypeface(null, Typeface.BOLD);
        yearHeader2.setBackgroundResource(R.drawable.border);
        TextView gradeHeader = new TextView(PageSectionInfo.this);
        gradeHeader.setText(" Grade");
        gradeHeader.setTypeface(null, Typeface.BOLD);
        gradeHeader.setBackgroundResource(R.drawable.border);

        headerRow2.addView(nameHeader2, 200, 75);
        headerRow2.addView(semesterHeader2, 200, 75);
        headerRow2.addView(yearHeader2, 100, 75);
        headerRow2.addView(gradeHeader, 50, 75);

        tableLayout2.addView(headerRow2);

        try {
            JSONArray currentjsonArray = new JSONArray(current);

            for (int i = 0; i < currentjsonArray.length(); i++) {
                TableRow tableRow = new TableRow(this);

                JSONObject jsonObject = currentjsonArray.getJSONObject(i);

                String name = jsonObject.getString("name");
                String semester = jsonObject.getString("semester");
                int year = jsonObject.getInt("year");
                String grade = jsonObject.getString("grade");

                TextView nameTextView = new TextView(this);
                nameTextView.setText(name);
                TextView semesterTextView = new TextView(this);
                semesterTextView.setText(semester);
                TextView yearTextView = new TextView(this);
                yearTextView.setText(String.valueOf(year));

                nameTextView.setBackgroundResource(R.drawable.border);
                semesterTextView.setBackgroundResource(R.drawable.border);
                yearTextView.setBackgroundResource(R.drawable.border);

                tableRow.addView(nameTextView, 200, 75);
                tableRow.addView(semesterTextView, 200, 75);
                tableRow.addView(yearTextView, 150, 75);

                if(!grade.equals("null")){
                    TextView gradeTextView = new TextView(this);
                    gradeTextView.setText(grade);

                    gradeTextView.setBackgroundResource(R.drawable.border);

                    tableRow.addView(gradeTextView, 150, 75);
                    tableLayout2.addView(tableRow);
                }
                else{
                    tableLayout.addView(tableRow);
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
