package com.example.vaibhav.magna;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  {


    private EditText username_TV, password_TV;
    private Button login_BTN;
    private TextView register_TV, forgot_tv;
    ProgressDialog dialog;
    String username, password;
    String URL = "http://10.0.2.2/vaibhav/login.php";
    String TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        username_TV = (EditText) findViewById(R.id.username_ET);
        password_TV = (EditText) findViewById(R.id.password_ET);
        login_BTN = (Button) findViewById(R.id.login_BTN);
        register_TV = (TextView) findViewById(R.id.register_TV);
        forgot_tv = (TextView) findViewById(R.id.forgot_tv);

        login_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username=username_TV.getText().toString();
                password=password_TV.getText().toString();
                new LoginUser().execute();
            }
        });
        register_TV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Register.class));
            }
        });
        forgot_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Forgot_main.class));
            }
        });


    }





    class LoginUser extends AsyncTask<String,Void,String>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog=new ProgressDialog(MainActivity.this);
            dialog.setMessage("Logging In");
            dialog.setIndeterminate(false);
            dialog.setCancelable(false);
            //dialog.create();
            dialog.show();


        }

        @Override
        protected String doInBackground(String... params) {


            List<NameValuePair> data=new ArrayList<>();
            data.add(new BasicNameValuePair("username",username));
            data.add(new BasicNameValuePair("password",password));
            JSONObject jobj=JSONParser.makeHttpRequest(URL,"POST",data);
            try {
                int response=jobj.getInt("success");
                if(response==1)
                {
                    TAG="success";
                }
                else
                {
                    TAG="fail";
                }


            } catch (JSONException e) {
                Toast.makeText(MainActivity.this,"No Response",Toast.LENGTH_LONG).show();
            }


            return TAG;
        }


        @Override
        protected void onPostExecute(String s) {

            dialog.dismiss();

            if(s.equalsIgnoreCase("success"))
            {
                Toast.makeText(MainActivity.this,"Login Successful",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MainActivity.this, Home_main.class);
                startActivity(intent);

            }

            else
            {

                Toast.makeText(MainActivity.this,"Some Error",Toast.LENGTH_LONG).show();

            }
        }
    }




}




