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

public class PageGrades extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_grades);
        Intent intent = getIntent();
        final String email2 = intent.getStringExtra("email2");
        final String password2 = intent.getStringExtra("password2");

        Button buttonLogout = (Button) findViewById(R.id.buttonLogout);
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PageGrades.this, PageLogin.class);
                PageGrades.this.startActivity(intent);
            }
        });
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
                                String email2 = jsonResponse.getString("email");
                                String password2 = jsonResponse.getString("password");

                                Intent intent = new Intent(PageGrades.this, PageMenu.class);

                                intent.putExtra("name", name);
                                intent.putExtra("id", id);
                                intent.putExtra("type", type);
                                intent.putExtra("email2", email2);
                                intent.putExtra("password2", password2);
                                PageGrades.this.startActivity(intent);
                            } else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(PageGrades.this);
                                builder.setMessage("Sign In Failed").setNegativeButton("Retry", null).create().show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                QueryRequest queryRequest = new QueryRequest(email2, password2, getString(R.string.url) + "login.php", responseListener);
                RequestQueue queue = Volley.newRequestQueue(PageGrades.this);
                queue.add(queryRequest);
            }
        });

        TableLayout tableLayout = findViewById(R.id.tableI);

        String jsonString = intent.getStringExtra("courses");
        String id = intent.getStringExtra("id");
        TableRow headerRow = new TableRow(this);

        TextView courseIdHeader = new TextView(this);
        TextView sectionIdHeader = new TextView(this);
        TextView semesterHeader = new TextView(this);
        TextView yearHeader = new TextView(this);
        TextView gradeHeader = new TextView(this);

        courseIdHeader.setText(" Course ID");
        courseIdHeader.setTypeface(null, Typeface.BOLD);
        courseIdHeader.setBackgroundResource(R.drawable.border);

        sectionIdHeader.setText(" Section ID");
        sectionIdHeader.setTypeface(null, Typeface.BOLD);
        sectionIdHeader.setBackgroundResource(R.drawable.border);

        semesterHeader.setText(" Semester");
        semesterHeader.setTypeface(null, Typeface.BOLD);
        semesterHeader.setBackgroundResource(R.drawable.border);

        yearHeader.setText(" Year");
        yearHeader.setTypeface(null, Typeface.BOLD);
        yearHeader.setBackgroundResource(R.drawable.border);

        gradeHeader.setText(" Grade");
        gradeHeader.setTypeface(null, Typeface.BOLD);
        gradeHeader.setBackgroundResource(R.drawable.border);

        headerRow.addView(courseIdHeader, 200, 75);
        headerRow.addView(sectionIdHeader, 200, 75);
        headerRow.addView(semesterHeader, 200, 75);
        headerRow.addView(yearHeader, 150, 75);
        headerRow.addView(gradeHeader, 150, 75);

        tableLayout.addView(headerRow);
        try {

            JSONArray jsonArray = new JSONArray(jsonString);

            for (int i = 0; i < jsonArray.length(); i++) {
                TableRow tableRow = new TableRow(this);

                JSONObject jsonObject = jsonArray.getJSONObject(i);

                String course_id = jsonObject.getString("course_id");
                String section_id = jsonObject.getString("section_id");
                String semester = jsonObject.getString("semester");
                String grade = jsonObject.getString("grade");
                int year = jsonObject.getInt("year");

                TextView course_idTextView = new TextView(this);
                course_idTextView.setText(course_id);

                TextView section_idTextView = new TextView(this);
                section_idTextView.setText(section_id);

                TextView semesterTextView = new TextView(this);
                semesterTextView.setText(semester);

                TextView gradeTextView = new TextView(this);
                gradeTextView.setText(grade);

                TextView yearTextView = new TextView(this);
                yearTextView.setText(String.valueOf(year));

                course_idTextView.setBackgroundResource(R.drawable.border);
                section_idTextView.setBackgroundResource(R.drawable.border);
                semesterTextView.setBackgroundResource(R.drawable.border);
                yearTextView.setBackgroundResource(R.drawable.border);
                gradeTextView.setBackgroundResource(R.drawable.border);

                tableRow.addView(course_idTextView, 200, 75);
                tableRow.addView(section_idTextView, 200, 75);
                tableRow.addView(semesterTextView, 200, 75);
                tableRow.addView(yearTextView, 150, 75);
                tableRow.addView(gradeTextView, 150, 75);

                tableLayout.addView(tableRow);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
