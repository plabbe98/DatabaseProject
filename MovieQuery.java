package com.example.movies;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MovieQuery extends AppCompatActivity {

    EditText etDept_name;
    EditText etName;
    EditText etEmail;
    EditText etPassword;
    EditText etStudent_ID;
    Button submitQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_query);

        etStudent_ID = (EditText) findViewById(R.id.student_id);
        etName = (EditText) findViewById(R.id.name);
        etDept_name = (EditText) findViewById(R.id.dept_name);
        etEmail = (EditText) findViewById(R.id.email);
        etPassword = (EditText) findViewById(R.id.password);

        submitQuery = (Button) findViewById(R.id.submitQuery);

        Button buttonLogout = (Button) findViewById(R.id.buttonLogout);
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MovieQuery.this, PageLogin.class);
                MovieQuery.this.startActivity(intent);
            }
        });

        submitQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                String name = etName.getText().toString();
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                String student_id = etStudent_ID.getText().toString();
                String dept_name = etDept_name.getText().toString();


                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("SubmitQueryHelp", response);
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if(success){
                                AlertDialog.Builder builder = new AlertDialog.Builder(MovieQuery.this);
                                builder.setMessage("Successfully created account!")
                                        .setNegativeButton("Okay", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intent = new Intent(MovieQuery.this, PageLogin.class);
                                                startActivity(intent);
                                            }
                                        })
                                        .create()
                                        .show();

                            } else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(MovieQuery.this);
                                builder.setMessage("Sign In Failed").setNegativeButton("Retry", null).create().show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                QueryRequest queryRequest = new QueryRequest(dept_name, name, email, password, student_id,getString(R.string.url) + "create_account.php", responseListener);
                RequestQueue queue = Volley.newRequestQueue(MovieQuery.this);
                queue.add(queryRequest);
            }

        });
    }
}
