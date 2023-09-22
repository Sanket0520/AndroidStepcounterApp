package com.example.androidstepcounterapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager mSensorManager = null;
    private Sensor StepSensor ;
    private int totalSteps=0;
    private int previewTotalstep=0;
    private ProgressBar progressbar;
    private TextView steps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressbar = findViewById(R.id.progressbar);
        steps =findViewById(R.id.step);

        resetSteps();
        loadData();

        mSensorManager=(SensorManager)getSystemService(SENSOR_SERVICE);
        StepSensor=mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
    }
    protected  void onResume(){
        super.onResume();

        if(StepSensor == null){
            Toast.makeText(this, "This Device ", Toast.LENGTH_SHORT).show();
        }
        else {
            mSensorManager.registerListener(this,StepSensor,SensorManager.SENSOR_DELAY_NORMAL);
        }
    }
    protected void onPause(){
        super.onPause();
        mSensorManager.unregisterListener(this);
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_STEP_COUNTER){
            totalSteps=(int) event.values[0];
            int currentSteps= totalSteps-previewTotalstep;
            steps.setText(String.valueOf(currentSteps));

            progressbar.setProgress(currentSteps);
        }
    }
    private void resetSteps(){
        steps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Long Press To Reset Steps", Toast.LENGTH_SHORT).show();
            }
        });

        steps.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                previewTotalstep = totalSteps;
                steps.setText("0");
                progressbar.setProgress(0);
                saveData();
                return true;
            }
        });
    }
    private void saveData(){
        SharedPreferences sharePref= getSharedPreferences("myPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharePref.edit();
        editor.putString("key1",String.valueOf(previewTotalstep));
        editor.apply();

    }

    private void loadData(){
        SharedPreferences sharePref= getSharedPreferences("myPref", Context.MODE_PRIVATE);
        int savedNumber=(int)sharePref.getFloat("key2",0f);
        previewTotalstep =savedNumber;
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}