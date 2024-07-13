package com.example.movies;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class QueryRequest extends StringRequest {
    private Map<String, String> args;
    private static Response.ErrorListener err = new Response.ErrorListener(){
        @Override
        public void onErrorResponse(VolleyError error){
            Log.d("please","Error listener response: " + error.getMessage());
        }
    };

    public QueryRequest(String dept_name, String name, String email, String password, String student_id, String url, Response.Listener<String> listener){
        super(Method.POST, url, listener, err);
        args = new HashMap<String, String>();
        args.put("dept_name", dept_name);
        args.put("name", name);
        args.put("email", email);
        args.put("password", password);
        args.put("student_id", student_id);
    }

    public QueryRequest(String id, String course_id, String section_id, String url, Response.Listener<String> listener){
        super(Method.POST, url, listener, err);
        args = new HashMap<String, String>();
        args.put("id", id);
        args.put("c_id", course_id);
        args.put("s_id", section_id);
    }

    public QueryRequest(String email, String password, String url, Response.Listener<String> listener){
        super(Method.POST, url, listener, err);
        args = new HashMap<String, String>();
        args.put("email", email);
        args.put("password", password);
    }

    public QueryRequest(String id, String url, Response.Listener<String> listener){
        super(Method.POST, url, listener, err);
        args = new HashMap<String, String>();
        args.put("id", id);
    }

    public QueryRequest(String id, JSONArray jsonData, JSONArray unjsonData, String url, Response.Listener<String> listener){
        super(Method.POST, url, listener, err);
        args = new HashMap<String, String>();
        args.put("id", id);
        args.put("json_data", jsonData.toString());
        args.put("unjson_data", unjsonData.toString());
    }


    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return args;
    }
}