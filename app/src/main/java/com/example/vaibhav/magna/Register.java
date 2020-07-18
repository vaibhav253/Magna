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

public class Register extends AppCompatActivity   {
    ProgressDialog dialog;

    private EditText username_ET, password_ET, email_ET, cpassword_ET;
    private String username, password, cpassword, email, emailpattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private Button register_BTN;
    private TextView register_registerd_TV;

    String TAG=null;
    String URL="http://10.0.2.2/vaibhav/register.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        username_ET = (EditText) findViewById(R.id.register_username_ET);
        password_ET = (EditText) findViewById(R.id.register_password_ET);
        cpassword_ET = (EditText) findViewById(R.id.register_cpassword_ET);
        email_ET = (EditText) findViewById(R.id.register_email_ET);
        register_registerd_TV = (TextView) findViewById(R.id.register_registerd_TV);
        register_BTN = (Button) findViewById(R.id.register_register_BTN);

        register_BTN=(Button) findViewById(R.id.register_register_BTN);
        register_registerd_TV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Register.this,MainActivity.class));

            }
        });
        register_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                username = username_ET.getText().toString();
                password = password_ET.getText().toString();
                cpassword = cpassword_ET.getText().toString();
                email = email_ET.getText().toString();


                if (!password.equals(cpassword)) {
                    Toast.makeText(Register.this, "passwords does not match", Toast.LENGTH_LONG).show();
                } else if (username.isEmpty() || password.isEmpty() || cpassword.isEmpty() || email.isEmpty() ) {
                    Toast.makeText(Register.this, "fill all fields ", Toast.LENGTH_LONG).show();
                } else if (!email.matches(emailpattern)) {
                    Toast.makeText(Register.this, "Email is not valid", Toast.LENGTH_LONG).show();
                } else {

                    new RegisterUser().execute();
                }
            }
        });

    }



    class RegisterUser extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(Register.this);
            dialog.setMessage("Creating User");
            dialog.setIndeterminate(false);
            dialog.setCancelable(false);
            //dialog.create();
            dialog.show();


        }

        protected String doInBackground(String... params) {

            List<NameValuePair> data = new ArrayList<>();
            data.add(new BasicNameValuePair("email", email));
            data.add(new BasicNameValuePair("username", username));
            data.add(new BasicNameValuePair("password", password));

            JSONObject jObj = JSONParser.makeHttpRequest(URL, "POST", data);
            try {
                int response=jObj.getInt("success");
                if(response==1)
                {
                    TAG="success";
                }
                else
                {
                    TAG="fail";
                }


            } catch (JSONException e) {
                Toast.makeText(Register.this,"No Response",Toast.LENGTH_LONG).show();
            }


            return TAG;
        }



        @Override
        protected void onPostExecute(String s) {

            dialog.dismiss();

            if (s.equalsIgnoreCase("success")) {
                Toast.makeText(Register.this, "User Registered", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Register.this, MainActivity.class);
                startActivity(intent);

            } else {

                Toast.makeText(Register.this, "Some Error", Toast.LENGTH_LONG).show();

            }
        }

    }

}
