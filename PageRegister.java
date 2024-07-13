package com.example.movies;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

public class PageRegister extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_register);

        Button buttonLogout = (Button) findViewById(R.id.buttonLogout);
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PageRegister.this, PageLogin.class);
                PageRegister.this.startActivity(intent);
            }
        });
        Intent intent = getIntent();
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
                                AlertDialog.Builder builder = new AlertDialog.Builder(PageRegister.this);
                                builder.setMessage("Successfully registered!").setNegativeButton("Okay", null).create().show();
                                String name = jsonResponse.getString("name");
                                String id = jsonResponse.getString("id");
                                String type = jsonResponse.getString("type");

                                Intent intent = new Intent(PageRegister.this, PageMenu.class);

                                intent.putExtra("name", name);
                                intent.putExtra("id", id);
                                intent.putExtra("type", type);
                                intent.putExtra("email2", email2);
                                intent.putExtra("password2", password2);
                                PageRegister.this.startActivity(intent);
                            } else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(PageRegister.this);
                                builder.setMessage("Sign In Failed").setNegativeButton("Retry", null).create().show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                QueryRequest queryRequest = new QueryRequest(email2, password2, getString(R.string.url) + "login.php", responseListener);
                RequestQueue queue = Volley.newRequestQueue(PageRegister.this);
                queue.add(queryRequest);
            }
        });
        final String id = intent.getStringExtra("id");
        TableLayout tableLayout = findViewById(R.id.tableI);

        String jsonString = intent.getStringExtra("courses");
        TableRow headerRow = new TableRow(this);

        TextView courseIdHeader = new TextView(this);
        TextView sectionIdHeader = new TextView(this);
        TextView buttonHeader = new TextView(this);

        courseIdHeader.setText(" Course ID");
        courseIdHeader.setTypeface(null, Typeface.BOLD);
        courseIdHeader.setBackgroundResource(R.drawable.border);

        sectionIdHeader.setText(" Section ID");
        sectionIdHeader.setTypeface(null, Typeface.BOLD);
        sectionIdHeader.setBackgroundResource(R.drawable.border);

        sectionIdHeader.setText(" Register");
        sectionIdHeader.setTypeface(null, Typeface.BOLD);
        sectionIdHeader.setBackgroundResource(R.drawable.border);

        headerRow.addView(courseIdHeader, 225, 75);
        headerRow.addView(sectionIdHeader, 225, 75);
        headerRow.addView(buttonHeader, 225, 75);

        tableLayout.addView(headerRow);
        try {

            JSONArray jsonArray = new JSONArray(jsonString);

            for (int i = 0; i < jsonArray.length(); i++) {
                TableRow tableRow = new TableRow(this);

                JSONObject jsonObject = jsonArray.getJSONObject(i);

                String course_id = jsonObject.getString("course_id");
                String section_id = jsonObject.getString("section_id");

                TextView course_idTextView = new TextView(this);
                course_idTextView.setText(course_id);

                TextView section_idTextView = new TextView(this);
                section_idTextView.setText(section_id);

                course_idTextView.setBackgroundResource(R.drawable.border);
                section_idTextView.setBackgroundResource(R.drawable.border);

                tableRow.addView(course_idTextView, 225, 75);
                tableRow.addView(section_idTextView, 225, 75);
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
                                        AlertDialog.Builder builder = new AlertDialog.Builder(PageRegister.this);
                                        builder.setMessage("Successfully registered!")
                                                .setNegativeButton("Okay", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        Intent intent = new Intent(PageRegister.this, PageGrades.class);
                                                        startActivity(intent);
                                                    }
                                                })
                                                .create()
                                                .show();

                                    } else{
                                        AlertDialog.Builder builder = new AlertDialog.Builder(PageRegister.this);
                                        builder.setMessage("Sign In Failed").setNegativeButton("Retry", null).create().show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        };

                        QueryRequest queryRequest = new QueryRequest(id, c_id, s_id, getString(R.string.url) + "register.php", responseListener);
                        RequestQueue queue = Volley.newRequestQueue(PageRegister.this);
                        queue.add(queryRequest);
                    }
                });

                tableLayout.addView(tableRow);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}