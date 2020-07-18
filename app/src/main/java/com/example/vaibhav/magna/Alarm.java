package com.example.vaibhav.magna;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.GregorianCalendar;

public class Alarm extends AppCompatActivity {

    protected static final int RESULT_SPEECH = 1;

    private ImageButton btnSpeak;
    private TextView txtText;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        txtText = (TextView) findViewById(R.id.txtText);

        btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);

        btnSpeak.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(
                        RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");

                try {
                    startActivityForResult(intent, RESULT_SPEECH);
                    txtText.setText("");
                } catch (ActivityNotFoundException a) {
                    Toast t = Toast.makeText(getApplicationContext(),
                            "Ops! Your device doesn't support Speech to Text",
                            Toast.LENGTH_SHORT);
                    t.show();
                }
            }
        });


        /*OnClickListener setClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent i = new Intent("in.wptrafficanalyzer.servicealarmdemo.demoactivity");


				PendingIntent operation = PendingIntent.getActivity(getBaseContext(), 0, i, Intent.FLAG_ACTIVITY_NEW_TASK);


				AlarmManager alarmManager = (AlarmManager) getBaseContext().getSystemService(ALARM_SERVICE);


				DatePicker dpDate = (DatePicker) findViewById(R.id.dp_date);


			//	TimePicker tpTime = (TimePicker) findViewById(R.id.tp_time);

				int year = dpDate.getYear();
				int month = dpDate.getMonth();
				int day = dpDate.getDayOfMonth();
				//int hour = tpTime.getCurrentHour();
				//int minute = tpTime.getCurrentMinute();
				int hour =11;
				int minute = 0;
				GregorianCalendar calendar = new GregorianCalendar(year,month,day, hour, minute);


				long alarm_time = calendar.getTimeInMillis();





		        alarmManager.set(AlarmManager.RTC_WAKEUP  , alarm_time , operation);

			    Toast.makeText(getBaseContext(),String.valueOf(alarm_time),Toast.LENGTH_SHORT).show();
		       Toast.makeText(getBaseContext(), "Alarm is set successfully",Toast.LENGTH_SHORT).show();

			}
		};



        Button btnSetAlarm = ( Button ) findViewById(R.id.btn_set_alarm);
        btnSetAlarm.setOnClickListener(setClickListener);*/
        View.OnClickListener quitClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

                //Toast.makeText(getApplicationContext(), String.valueOf(date), 1000).show();
            }
        };
        Button btnQuitAlarm = ( Button ) findViewById(R.id.btn_quit_alarm);
        btnQuitAlarm.setOnClickListener(quitClickListener);

    }

    void showToastMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RecognizerIntent.RESULT_AUDIO_ERROR)
        {
            showToastMessage("Audio Error");
        }
        else if(resultCode == RecognizerIntent.RESULT_CLIENT_ERROR)
        {
            showToastMessage("Client Error");
        }
        else if(resultCode == RecognizerIntent.RESULT_NETWORK_ERROR)
        {
            showToastMessage("Network Error");
        }
        else if(resultCode == RecognizerIntent.RESULT_NO_MATCH)
        {
            showToastMessage("No Match");
        }
        else if(resultCode == RecognizerIntent.RESULT_SERVER_ERROR)
        {
            showToastMessage("Server Error");
        }

        super.onActivityResult(requestCode, resultCode, data);

        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case RESULT_SPEECH: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> text = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    txtText.setText(text.get(0));
                    Intent i = new Intent("com.example.voiceapplication");


                    PendingIntent operation = PendingIntent.getActivity(getBaseContext(), 0, i, Intent.FLAG_ACTIVITY_NEW_TASK);


                    AlarmManager alarmManager = (AlarmManager) getBaseContext().getSystemService(ALARM_SERVICE);


                    DatePicker dpDate = (DatePicker) findViewById(R.id.dp_date);


                    //	TimePicker tpTime = (TimePicker) findViewById(R.id.tp_time);

                    int year = dpDate.getYear();
                    int month = dpDate.getMonth();
                    int day = dpDate.getDayOfMonth();
                    //int hour = tpTime.getCurrentHour();
                    //int minute = tpTime.getCurrentMinute();
                    int hour =Integer.parseInt(txtText.getText().toString());
                    int minute = 0;
                    GregorianCalendar calendar = new GregorianCalendar(year,month,day, hour, minute);


                    long alarm_time = calendar.getTimeInMillis();





                    alarmManager.set(AlarmManager.RTC_WAKEUP  , alarm_time , operation);
                    Toast.makeText(getBaseContext(), "Alarm is set successfully",Toast.LENGTH_SHORT).show();
                    //Toast.makeText(getBaseContext(),String.valueOf(alarm_time),Toast.LENGTH_SHORT).show();
                }
                break;
            }

        }
    }
}
