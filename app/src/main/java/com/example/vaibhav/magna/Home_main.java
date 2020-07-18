package com.example.vaibhav.magna;

import android.Manifest;
import android.app.SearchManager;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Home_main extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    final int VOICE_RECOGNITION_REQUEST_CODE = 1001;
    ImageButton mbtSpeak;
    GoogleApiClient client;
    String id;
    String wordStr = null;
    String[] words = null;
    String firstWord = null;
    String secondWord = null;
    String phoneNumber;
    private static final int CAMERA_REQUEST = 1888;
    // Activity request codes
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    // directory name to store captured images and videos
    private static final String IMAGE_DIRECTORY_NAME = "ring";
    private Uri fileUri; // file url to store image/video

    private ImageView imgPreview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mbtSpeak = (ImageButton) findViewById(R.id.btSpeak);
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Add Your Custom Command", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();


            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
       /* if (id == R.id.action_settings) {
            return true;
        }
*/
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_settings) {
            // Handle the camera action
        } else if (id == R.id.nav_help) {

        } else if (id == R.id.nav_customcommands) {

        } else if (id == R.id.nav_about) {

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void checkVoiceRecognition() {
        // Check if voice recognition is present
        PackageManager pm = getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(
                RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        if (activities.size() == 0) {
            mbtSpeak.setEnabled(false);
            Toast.makeText(this, "Voice recognizer not present",
                    Toast.LENGTH_SHORT).show();
        }
    }


    public void speak(View view) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        // Specify the calling package to identify your application
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getClass()
                .getPackage().getName());

        // Display an hint to the user about what he should say.
        //intent.putExtra(RecognizerIntent.EXTRA_PROMPT, metTextHint.getText()
        //  .toString());

        // Given an hint to the recognizer about what the user is going to say
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);

        // If number of Matches is not selected then return show toast message
        //	if (msTextMatches.getSelectedItemPosition() == AdapterView.INVALID_POSITION) {
        //	Toast.makeText(this, "Please select No. of Matches from spinner",
        //		Toast.LENGTH_SHORT).show();
        //	return;
        //}

        //int noOfMatches = Integer.parseInt(msTextMatches.getSelectedItem()
        //	.toString());
        // Specify how many results you want to receive. The results will be
        // sorted where the first result is the one with higher confidence.

        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 5);

        startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
    }


    //To take a photo.
    public void takephoto(View v) {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // successfully captured the image
                // display it in image view
                previewCapturedImage();
            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled Image capture
                Toast.makeText(getApplicationContext(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();
            } else {
                // failed to capture image
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }
        } else if (requestCode == VOICE_RECOGNITION_REQUEST_CODE)

        //If Voice recognition is successful then it returns RESULT_OK
        {
            if (resultCode == RESULT_OK) {

                ArrayList<String> textMatchList = data
                        .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                if (!textMatchList.isEmpty()) {
                    // If first Match contains the 'search' word
                    // Then start web search.
				/*	if (textMatchList.get(0).contains("search")) {

						String searchQuery = textMatchList.get(0).replace("search",
						" ");
						Intent search = new Intent();
						search.putExtra(SearchManager.QUERY, searchQuery);
						startActivity(search);
					}
					*/

                    wordStr = textMatchList.get(0);
                    words = wordStr.split(" ");
                    firstWord = words[0];
                    secondWord = words[0];
                    if (firstWord.equals("hi")) {

                        Intent i = new Intent(getApplicationContext(), Alarm.class);
                        i.putExtra("tt", secondWord);
                        startActivityForResult(i, 0);
                        Toast.makeText(getApplicationContext(), "hi", Toast.LENGTH_LONG).show();
                    }
                    if (firstWord.equals("alarm")) {
                        Intent i = new Intent(getApplicationContext(), Alarm.class);

                        startActivityForResult(i, 0);

                    }
                    if (firstWord.equals("date")) {
                        //View v = null;

                        Intent i = new Intent(getApplicationContext(), DateTime.class);

                        startActivityForResult(i, 0);

                    }
                    if (firstWord.equals("bluetooth")) {
                        //View v = null;
                        //takephoto(v);
                        String term = secondWord.trim();
                        if (term.equals("on")) {
                            BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                            if (!mBluetoothAdapter.isEnabled()) {
                                mBluetoothAdapter.enable();
                            }
                        } else if (term.equals("off")) {

                            BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                            if (mBluetoothAdapter.isEnabled()) {
                                mBluetoothAdapter.disable();
                            }
                        }

                    }


	               /* if (firstWord.equals("wifi")) {
	                	//View v = null;
	                	//takephoto(v);
	                	String term = secondWord.trim();
	                	if(term.equals("on"))
	                	{
	                		WifiP2pManager mBluetoothAdapter = Wifi.getDefault();
	                		if (!mBluetoothAdapter.isEnabled()){
	                	        mBluetoothAdapter.enable();
	                	    }
	                	}
	                	else if(term.equals("off"))
	                	{
	                		BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	                		if (mBluetoothAdapter.isEnabled()){
	                	        mBluetoothAdapter.disable();
	                	    }
	                	}

	                }*/

                    if (firstWord.equals("camera")) {
                        //View v = null;
                        //takephoto(v);
                        captureImage();
                    }
                    if (firstWord.equals("open")) {
                        PackageManager packageManager = getPackageManager();
                        List<PackageInfo> packs = packageManager
                                .getInstalledPackages(0);
                        Intent i;
                        PackageManager manager = getPackageManager();
                        try {
                            i = manager.getLaunchIntentForPackage("app package name");
                            if (i == null)
                                throw new PackageManager.NameNotFoundException();
                            i.addCategory(Intent.CATEGORY_LAUNCHER);
                            startActivity(i);
                        } catch (PackageManager.NameNotFoundException e) {

                        }


                        int size = packs.size();
                        boolean uninstallApp = false;
                        boolean exceptFlg = false;
                        for (int v = 0; v < size; v++) {
                            PackageInfo p = packs.get(v);
                            String tmpAppName = p.applicationInfo.loadLabel(
                                    packageManager).toString();
                            String pname = p.packageName;
                            Object urlAddress = "0";
                            urlAddress = ((String) urlAddress).toLowerCase();
                            tmpAppName = tmpAppName.toLowerCase();
                            if (tmpAppName.trim().toLowerCase().
                                    equals(secondWord.trim().toLowerCase())) {
                                PackageManager pm = this.getPackageManager();
                                Intent appStartIntent = pm.getLaunchIntentForPackage(pname);
                                if (null != appStartIntent) {
                                    try {
                                        this.startActivity(appStartIntent);
                                    } catch (Exception e) {
                                    }
                                }
                            }
                        }
                    } // end of open app code
                    if (firstWord.equals("search")) {
                        String term = secondWord.trim();
                        try {
                            Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                            //String term = editTextInput.getText().toString();
                            intent.putExtra(SearchManager.QUERY, term);
                            startActivity(intent);
                        } catch (Exception e) {
                            // TODO: handle exception
                        }
                        //	Toast.makeText(getApplicationContext(),"hi",1000).show();

                    }
                    if (firstWord.equals("message")) {
                        String contname = secondWord.trim();

                        Log.e("name", contname);
                        Uri lkup = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_FILTER_URI, contname);
                        Cursor idCursor = getContentResolver().query(lkup, null, null, null, null);
                        if (idCursor.getCount() == 0)
                            Toast.makeText(getApplicationContext(), contname + "  not Found", Toast.LENGTH_LONG).show();
                        else {
                            while (idCursor.moveToNext()) {
                                id = idCursor.getString(idCursor.getColumnIndex(ContactsContract.Contacts._ID));
                                String key = idCursor.getString(idCursor.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
                                String name = idCursor.getString(idCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                                //Toast.makeText(getApplicationContext(), id + "  "+ name , Toast.LENGTH_LONG).show();
                            }

                            idCursor.close();
                        }
                        Cursor phoneCursor = getContentResolver().query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "= ? ",
                                new String[]{id}, null);
                        if (phoneCursor.moveToFirst()) {
                            phoneNumber = phoneCursor
                                    .getString(phoneCursor
                                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                            Toast.makeText(getApplicationContext(), phoneNumber, Toast.LENGTH_SHORT).show();

                            //Intent i = new Intent(getApplicationContext(), MainActivity1.class);
                            //i.putExtra("no", phoneNumber);
                            //startActivity(i);
                        }

                    }
                    //emergency
                    if (firstWord.equals("help") || firstWord.equals("emergency")) {
                        //Toast.makeText(getApplicationContext(), "hi", 1000).show();
                        String to = "";
                        String to1 = "";
                        String to2 = "";
                        String to3 = "";
                        String to4 = "";
                        String subject = " EMERGENCY!! ";
                        String message = "I am in an EMERGENCY!! Please Contact me!!";

                        Intent email = new Intent(Intent.ACTION_SEND);
                        email.putExtra(Intent.EXTRA_EMAIL, new String[]{to, to1, to2, to3, to4});

                        email.putExtra(Intent.EXTRA_SUBJECT, subject);
                        email.putExtra(Intent.EXTRA_TEXT, message);

                        //need this to prompts email client only
                        email.setType("message/rfc822");

                        startActivity(Intent.createChooser(email, "Choose an Email client :"));
	    				  /*sms code */
                        String phoneNo = "";
                        String phoneNo1 = "";
                        String phoneNo2 = "";
                        String phoneNo3 = "";
                        String phoneNo4 = "";
                        String sms = "I am in an EMERGENCY!! Please Contact me!!";

                        try {
                            SmsManager smsManager = SmsManager.getDefault();
                            smsManager.sendTextMessage(phoneNo, null, sms, null, null);
                            smsManager.sendTextMessage(phoneNo1, null, sms, null, null);
                            smsManager.sendTextMessage(phoneNo2, null, sms, null, null);
                            smsManager.sendTextMessage(phoneNo3, null, sms, null, null);
                            smsManager.sendTextMessage(phoneNo4, null, sms, null, null);

                            Toast.makeText(getApplicationContext(), " Alert SMS Sent!",
                                    Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(),
                                    "SMS failed, please try again later!",
                                    Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }


                    }

                    if (firstWord.equals("call")) {


                        String contname = secondWord.trim();

                        Log.e("name", contname);
                        Uri lkup = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_FILTER_URI, contname);
                        Cursor idCursor = getContentResolver().query(lkup, null, null, null, null);
                        if (idCursor.getCount() == 0)
                            Toast.makeText(getApplicationContext(), contname + "  not Found", Toast.LENGTH_LONG).show();
                        else {
                            while (idCursor.moveToNext()) {
                                id = idCursor.getString(idCursor.getColumnIndex(ContactsContract.Contacts._ID));
                                String key = idCursor.getString(idCursor.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
                                String name = idCursor.getString(idCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                                //Toast.makeText(getApplicationContext(), id + "  "+ name , Toast.LENGTH_LONG).show();
                            }

                            idCursor.close();
                        }
                        Cursor phoneCursor = getContentResolver().query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "= ? ",
                                new String[]{id}, null);
                        if (phoneCursor.moveToFirst()) {
                            phoneNumber = phoneCursor
                                    .getString(phoneCursor
                                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                            Toast.makeText(getApplicationContext(), phoneNumber, Toast.LENGTH_SHORT).show();

                            Intent i = new Intent(Intent.ACTION_CALL);
                            i.setData(Uri.parse("tel:" + phoneNumber));
                            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling
                                //    ActivityCompat#requestPermissions
                                // here to request the missing permissions, and then overriding
                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                //                                          int[] grantResults)
                                // to handle the case where the user grants the permission. See the documentation
                                // for ActivityCompat#requestPermissions for more details.
                                return;
                            }
                            startActivity(i);
                        }


                    }

                } /*  else if (requestCode == CAMERA_REQUEST) {
	                Bitmap photo = (Bitmap) data.getExtras().get("data");
	                img.setImageBitmap(photo);


	    }*/


                //Result code for various error.
            } else if (resultCode == RecognizerIntent.RESULT_AUDIO_ERROR) {
                showToastMessage("Audio Error");
            } else if (resultCode == RecognizerIntent.RESULT_CLIENT_ERROR) {
                showToastMessage("Client Error");
            } else if (resultCode == RecognizerIntent.RESULT_NETWORK_ERROR) {
                showToastMessage("Network Error");
            } else if (resultCode == RecognizerIntent.RESULT_NO_MATCH) {
                showToastMessage("No Match");
            } else if (resultCode == RecognizerIntent.RESULT_SERVER_ERROR) {
                showToastMessage("Server Error");
            }

            super.onActivityResult(requestCode, resultCode, data);
        }
    }




    void showToastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }/*
	 * Display image from a path to ImageView
	 */

    private void previewCapturedImage() {
        try {
            // hide video preview
            //videoPreview.setVisibility(View.GONE);

            imgPreview.setVisibility(View.VISIBLE);

            // bimatp factory
            BitmapFactory.Options options = new BitmapFactory.Options();

            // downsizing image as it throws OutOfMemory Exception for larger
            // images
            options.inSampleSize = 8;

            final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(),
                    options);

            imgPreview.setImageBitmap(bitmap);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    /**
     * ------------ Helper Methods ----------------------
     */

	/*
	 * Creating file uri to store image/video
	 */
    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /*
     * returning image / video
     */
    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.vaibhav.magna/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.vaibhav.magna/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }}
