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

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class GraphActivity extends AppCompatActivity implements SensorEventListener {


    int [] sensorList;
    String [] sensorNames;
    int [] sensors;
    LineGraphSeries<DataPoint>[] sensorViews;
    private final int maxData = 200;
    int [] lastx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        sensorList = new int[]{-1, -1, -1, -1};
        lastx = new int[]{0, 0, 0, 0};
        sensors = new int[]{Sensor.TYPE_LIGHT, Sensor.TYPE_PRESSURE, Sensor.TYPE_AMBIENT_TEMPERATURE, Sensor.TYPE_RELATIVE_HUMIDITY, Sensor.TYPE_ACCELEROMETER, Sensor.TYPE_GYROSCOPE, Sensor.TYPE_MAGNETIC_FIELD, Sensor.TYPE_PROXIMITY};
        sensorNames = new String[]{"Light", "Pressure", "Temperature", "Humidity", "Accelerometer", "Gyroscope", "Magnetometer", "Proximity"};
        sensorViews = new LineGraphSeries[4];
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

        TextView l1, l2, l3, l4;
        l1 = findViewById(R.id.lv1);
        l2 = findViewById(R.id.lv2);
        l3 = findViewById(R.id.lv3);
        l4 = findViewById(R.id.lv4);
        LineGraphSeries<DataPoint> series, series2, series3, series4;
        series = new LineGraphSeries<>();
        series2 = new LineGraphSeries<>();
        series3 = new LineGraphSeries<>();
        series4 = new LineGraphSeries<>();
        series.appendData(new DataPoint(0, 0), true, maxData);
        series2.appendData(new DataPoint(0, 0), true, maxData);
        series3.appendData(new DataPoint(0, 0), true, maxData);
        series4.appendData(new DataPoint(0, 0), true, maxData);
        GraphView g1, g2, g3, g4;
        g1 = findViewById(R.id.g1);
        g2 = findViewById(R.id.g2);
        g3 = findViewById(R.id.g3);
        g4 = findViewById(R.id.g4);
        g1.addSeries(series);
        g2.addSeries(series2);
        g3.addSeries(series3);
        g4.addSeries(series4);
        g1.getViewport().setXAxisBoundsManual(true);
        g1.getViewport().setMinX(0);
        g1.getViewport().setMaxX(maxData);
        g2.getViewport().setXAxisBoundsManual(true);
        g2.getViewport().setMinX(0);
        g2.getViewport().setMaxX(maxData);
        g3.getViewport().setXAxisBoundsManual(true);
        g3.getViewport().setMinX(0);
        g3.getViewport().setMaxX(maxData);
        g4.getViewport().setXAxisBoundsManual(true);
        g4.getViewport().setMinX(0);
        g4.getViewport().setMaxX(maxData);
        if(sensorList[0]==-1) {
            l1.setText("");
            g1.setVisibility(View.INVISIBLE);
        }
        else {
            l1.setText(sensorNames[sensorList[0]]);
            sensorViews[0] = series;
        }
        if(sensorList[1]==-1) {
            l2.setText("");
            g2.setVisibility(View.INVISIBLE);
        }
        else {
            l2.setText(sensorNames[sensorList[1]]);
            sensorViews[1] = series2;
        }
        if(sensorList[2]==-1) {
            l3.setText("");
            g3.setVisibility(View.INVISIBLE);
        }
        else {
            l3.setText(sensorNames[sensorList[2]]);
            sensorViews[2] = series3;
        }
        if(sensorList[3]==-1) {
            l4.setText("");
            g4.setVisibility(View.INVISIBLE);
        }
        else {
            l4.setText(sensorNames[sensorList[3]]);
            sensorViews[3] = series4;
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
        LineGraphSeries<DataPoint> v = sensorViews[i];
        float x = sensorEvent.values[0];
        float y = sensorEvent.values[1];
        float z = sensorEvent.values[2];
        double mag = Math.sqrt(x*x+y*y+z*z);
        lastx[i]++;
        v.appendData(new DataPoint(lastx[i], mag), true, maxData);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public void back(View v) {
        Intent k = new Intent(GraphActivity.this, MainActivity.class);
        startActivity(k);
        finish();
    }

}
