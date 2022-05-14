package com.drayano.card_ocr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity
{
    private EditText user,password;
    private Button btnLogin;
    private ProgressBar loading;
    private String URL_LOGIN = "http://testcard.aeadz.net/android/login.php";
    //private String URL_LOGIN = "https://ismail-houari.000webhostapp.com/login.php";
    //private String URL_LOGIN = "http://card.aeadz.net/android/login.php";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SharedPreferences userDetails = getSharedPreferences("userDetails", MODE_PRIVATE);
        if (!userDetails.getString("username","").isEmpty())
        {
            Intent i = new Intent(LoginActivity.this,MainActivity.class);
            startActivity(i);
        }

        loading = findViewById(R.id.loading);
        user = findViewById(R.id.email_edit);
        password = findViewById(R.id.password_edit);
        btnLogin = findViewById(R.id.btn_login);

        btnLogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                String username = user.getText().toString().trim();
                String pass = password.getText().toString().trim();

                if (!username.isEmpty() && !pass.isEmpty())
                {
                    login(username,pass);
                }

                else
                {
                    user.setError("SVP inerer votre email");
                    password.setError("SVP inerer votre mot de passe");
                }
            }
        });
    }

    private void login(final String user, final String password)
    {
        loading.setVisibility(View.VISIBLE);
        btnLogin.setVisibility(View.GONE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_LOGIN,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        try
                        {
                            Log.i("tagconvertstr", "["+response+"]");
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1"))
                            {
                                Toast.makeText(LoginActivity.this,"Connecter avec succée",Toast.LENGTH_LONG).show();
                                SharedPreferences userDetails = getSharedPreferences("userDetails", MODE_PRIVATE);
                                SharedPreferences.Editor editor = userDetails.edit();
                                editor.putString("username",user);
                                editor.putString("password",password);
                                editor.apply();
                                Intent i = new Intent(LoginActivity.this,MainActivity.class);
                                startActivity(i);
                                loading.setVisibility(View.GONE);
                                btnLogin.setVisibility(View.VISIBLE);
                            }

                            else
                            {
                                String message = jsonObject.getString("message");
                                Toast.makeText(LoginActivity.this,"Compte introuvable "+message,Toast.LENGTH_LONG).show();
                                loading.setVisibility(View.GONE);
                                btnLogin.setVisibility(View.VISIBLE);
                            }
                        }

                        catch (JSONException e)
                        {
                            e.printStackTrace();
                            Toast.makeText(LoginActivity.this,"Erreur JSON"+response,Toast.LENGTH_LONG).show();
                            loading.setVisibility(View.GONE);
                            btnLogin.setVisibility(View.VISIBLE);
                        }

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Toast.makeText(LoginActivity.this,"Erreur" + error.toString(),Toast.LENGTH_LONG).show();
                        Log.i("Login", error.toString());
                        loading.setVisibility(View.GONE);
                        btnLogin.setVisibility(View.VISIBLE);
                    }
                })
                {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError
                    {
                        Map<String, String> params = new HashMap<>();
                        params.put("email",user);
                        params.put("password",password);

                        return params;
                    }
                };

        stringRequest.setRetryPolicy(new RetryPolicy()
        {
            @Override
            public int getCurrentTimeout()
            {
                return 10000;
            }

            @Override
            public int getCurrentRetryCount()
            {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError
            {

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
