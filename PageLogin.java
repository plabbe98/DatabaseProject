package com.example.movies;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
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

public class PageLogin extends AppCompatActivity {

    EditText etEmail;
    EditText etPassword;
    Button createAccount;
    Button loginAccount;
     protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_page_login);

         etEmail = (EditText) findViewById(R.id.email_login);
         etPassword = (EditText) findViewById(R.id.password_login);

         createAccount = (Button) findViewById(R.id.create_account_button);
         loginAccount = (Button) findViewById(R.id.login_button);

         loginAccount.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v){
                 String email = etEmail.getText().toString();
                 String password = etPassword.getText().toString();

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

                                 Intent intent = new Intent(PageLogin.this, PageMenu.class);

                                 intent.putExtra("name", name);
                                 intent.putExtra("id", id);
                                 intent.putExtra("type", type);
                                 intent.putExtra("email2", email2);
                                 intent.putExtra("password2", password2);
                                 PageLogin.this.startActivity(intent);
                             } else{
                                 AlertDialog.Builder builder = new AlertDialog.Builder(PageLogin.this);
                                 builder.setMessage("Sign In Failed").setNegativeButton("Retry", null).create().show();
                             }
                         } catch (JSONException e) {
                             e.printStackTrace();
                         }
                     }
                 };

                 QueryRequest queryRequest = new QueryRequest(email, password, getString(R.string.url) + "login.php", responseListener);
                 RequestQueue queue = Volley.newRequestQueue(PageLogin.this);
                 queue.add(queryRequest);
             }

         });



         createAccount.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 Intent intent = new Intent(PageLogin.this, MovieQuery.class);
                 PageLogin.this.startActivity(intent);


             }
         });
     }
}