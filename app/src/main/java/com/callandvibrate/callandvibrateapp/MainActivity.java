package com.callandvibrate.callandvibrateapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText phoneNumberEditText;
    private Vibrator v;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        phoneNumberEditText = findViewById(R.id.phoneNumber);
        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
    }
    private static final int PERMISSIONS_REQUEST = 100;

    public void call(View view){

        // Check permission (Api 22 check in Manifest, Api 23 check by requestPermissions)
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // Dont have permission => request one or many permissions (String[])
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, PERMISSIONS_REQUEST);
        }else {
            // Have permission
            openCallApplication();
        }
    }

    // On request permissions result
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults){
        switch (requestCode) {
            case PERMISSIONS_REQUEST: {
                if (permissions[0].equals(Manifest.permission.CALL_PHONE)) {
                    if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                        openCallApplication();
                    }else{
                        Toast.makeText(this, "CALL_PHONE permissions declined!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    public void openCallApplication(){
        String phoneNumber = phoneNumberEditText.getText().toString();
        if(phoneNumber.isEmpty()){
            Toast.makeText(this, "Phone number required!", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:"+phoneNumber));

        // Is necesary to check permission again before startActivity
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED)
            startActivity(intent);
    }

    public void vibrate(View view){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            v.vibrate(VibrationEffect.createOneShot(500,VibrationEffect.DEFAULT_AMPLITUDE));
        }else
            v.vibrate(500);
    }
}
