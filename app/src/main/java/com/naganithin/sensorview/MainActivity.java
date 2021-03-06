package com.naganithin.sensorview;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    int [] sensorList;
    String [] sensorNames;
    int [] sensors;
    TextView [] sensorViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sensorList = new int[]{-1, -1, -1, -1};
        sensors = new int[]{Sensor.TYPE_LIGHT, Sensor.TYPE_PRESSURE, Sensor.TYPE_AMBIENT_TEMPERATURE, Sensor.TYPE_RELATIVE_HUMIDITY, Sensor.TYPE_ACCELEROMETER, Sensor.TYPE_GYROSCOPE, Sensor.TYPE_MAGNETIC_FIELD, Sensor.TYPE_PROXIMITY};
        sensorNames = new String[]{"Light", "Pressure", "Temperature", "Humidity", "Accelerometer", "Gyroscope", "Magnetometer", "Proximity"};
        sensorViews = new TextView[4];
    }

    @Override
    protected void onResume() {
        super.onResume();
        int count = 0;
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        int [] range = {0, 1, 2, 3, 4, 5, 6, 7};
        for(int i: range) {
            if (sensorManager != null && sensorManager.getDefaultSensor(sensors[i]) != null && count < 4) {
                sensorList[count] = i;
                count++;
                Sensor sensor = sensorManager.getDefaultSensor(sensors[i]);
                sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI);
            }
            if(count==4) break;
        }

        TextView l1, l2, l3, l4, v1, v2, v3, v4;
        l1 = findViewById(R.id.l1);
        l2 = findViewById(R.id.l2);
        l3 = findViewById(R.id.l3);
        l4 = findViewById(R.id.l4);
        v1 = findViewById(R.id.v1);
        v2 = findViewById(R.id.v2);
        v3 = findViewById(R.id.v3);
        v4 = findViewById(R.id.v4);
        v1.setText("");
        v2.setText("");
        v3.setText("");
        v4.setText("");
        if(sensorList[0]==-1) {
            l1.setText("");
        }
        else {
            l1.setText(sensorNames[sensorList[0]]);
            sensorViews[0] = v1;
        }
        if(sensorList[1]==-1) {
            l2.setText("");
        }
        else {
            l2.setText(sensorNames[sensorList[1]]);
            sensorViews[1] = v2;
        }
        if(sensorList[2]==-1) {
            l3.setText("");
        }
        else {
            l3.setText(sensorNames[sensorList[2]]);
            sensorViews[2] = v3;
        }
        if(sensorList[3]==-1) {
            l4.setText("");
        }
        else {
            l4.setText(sensorNames[sensorList[3]]);
            sensorViews[3] = v4;
        }
    }

    @Override
    protected void onPause() {
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        for(int i: sensorList) {
            if (sensorManager != null && sensorManager.getDefaultSensor(i) != null) {
                Sensor sensor = sensorManager.getDefaultSensor(i);
                sensorManager.unregisterListener(this, sensor);
            }
            else {
                Toast.makeText(getApplicationContext(), sensorNames[i]+" Error", Toast.LENGTH_LONG).show();
                System.exit(1);
            }
        }
        super.onPause();
    }



    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent==null) return;
        int i = 0;
        int sensor = sensorEvent.sensor.getType();
        for (int j: sensorList) {
            if(sensors[j]==sensor) break;
            i++;
        }
        if(i==4) return;
        TextView v = sensorViews[i];
        float x = sensorEvent.values[0];
        float y = sensorEvent.values[1];
        float z = sensorEvent.values[2];
        double mag = Math.sqrt(x*x+y*y+z*z);
        //v.setText(String.format(Locale.getDefault(), "%.3f %.3f %.3f", x, y, z));
        v.setText(String.format(Locale.getDefault(), "%.9f", mag));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    public void graph(View v) {
        Intent k = new Intent(MainActivity.this, GraphActivity.class);
        startActivity(k);
        finish();
    }


}
