package com.example.sensor;

import static java.lang.Math.abs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

public class SensorActivity extends AppCompatActivity implements SensorEventListener {
    TextView textDimensionX,textDimensionY,textDimensionZ,textLumen;
    SensorManager sensorManager;
    Sensor sensorLight,sensorXYZ;
    float dimensionX,dimensionY,dimensionZ,lightLumen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);
        init();

    }
    private void init(){
        textDimensionX = findViewById(R.id.textX);
        textDimensionY = findViewById(R.id.textY);
        textDimensionZ = findViewById(R.id.textZ);
        textLumen = findViewById(R.id.textLight);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorXYZ = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        sensorLight = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
    }
    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this,sensorXYZ,SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this,sensorLight,SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(SensorActivity.this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(sensorEvent.sensor.getType() == Sensor.TYPE_LIGHT){
            lightLumen = sensorEvent.values[0];
            textLumen.setText("Lumen: " + lightLumen);
        }else if(sensorEvent.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION){
            dimensionX = sensorEvent.values[0];
            dimensionY = sensorEvent.values[1];
            dimensionZ = sensorEvent.values[2];
            textDimensionX.setText("X: " + dimensionX);
            textDimensionY.setText("Y: " + dimensionY);
            textDimensionZ.setText("Z: " + dimensionZ);
        }

        if(abs(dimensionX) < 1.0 && abs(dimensionY) < 1.0 && abs(dimensionZ) < 1.0 && lightLumen < 10 ){
            // karanlık(cepte) + hareketsiz
            Intent intent  = new Intent("com.example.homework1.SEND_MESSAGE");
            intent.putExtra("olay","ceptevehareketsiz");
            textLumen.setText("Lumen: " + lightLumen + "\nOlay: \nceptevehareketsiz");
            sendBroadcast(intent);
        }else if( abs(dimensionX) < 1.0 && abs(dimensionY) < 1.0 && abs(dimensionZ) < 1.0 && lightLumen > 10 ){
            // aydınlık(masada) + hareketsiz
            Intent intent  = new Intent("com.example.homework1.SEND_MESSAGE");
            intent.putExtra("olay","masadavehareketsiz");
            textLumen.setText("Lumen: " + lightLumen+ "\nOlay: \nmasadavehareketsiz");

            sendBroadcast(intent);
        }else if((abs(dimensionX) > 1.0 || abs(dimensionY) > 1.0 || abs(dimensionZ) > 1.0 ) && lightLumen < 10){
            // karanlık(cepte) + hareketli
            Intent intent  = new Intent("com.example.homework1.SEND_MESSAGE");
            intent.putExtra("olay","ceptevehareketli");
            textLumen.setText("Lumen: " + lightLumen + "\nOlay: \nceptevehareketli");

            sendBroadcast(intent);
        }else{
            Intent intent  = new Intent("com.example.homework1.SEND_MESSAGE");
            intent.putExtra("olay","eldevehareketli");
            textLumen.setText("Lumen: " + lightLumen + "\nOlay: \neldevehareketli");
            sendBroadcast(intent);
            // aydınlık(elde) + hareketli
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}