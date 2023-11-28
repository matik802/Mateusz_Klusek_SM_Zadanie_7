package com.example.sensorapp;

import static android.hardware.Sensor.TYPE_MAGNETIC_FIELD;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;



public class SensorActivity extends AppCompatActivity {
    private SensorManager sensorManager;
    private List<Sensor> sensorList;
    private SensorAdapter adapter;
    private RecyclerView recyclerView;
    private boolean textVisible;
    public static final String KEY_EXTRA_SENSOR_NAME = "SensorActivity.sensor";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sensor_activity);

        recyclerView = findViewById(R.id.sensor_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);

        if (adapter == null) {
            adapter = new SensorAdapter(sensorList);
            recyclerView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
        ActionBar actionBar;
        actionBar = getSupportActionBar();
        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#019361"));
        actionBar.setBackgroundDrawable(colorDrawable);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sensor_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            default:
                return super.onOptionsItemSelected(item);
            case R.id.show_sensor_count:
                textVisible = !textVisible;
                String subtitle = null;
                invalidateOptionsMenu();
                if (textVisible)
                    subtitle = getString(R.string.sensor_count, sensorList.size());
                getSupportActionBar().setSubtitle(subtitle);
                return true;
        }
    }
    private class SensorHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView iconImageView;
        private TextView nameTextView;
        private Sensor sensor;
        public SensorHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.sensor_list_item, parent, false));
            itemView.setOnClickListener(this);

            iconImageView = itemView.findViewById(R.id.sensor_icon);
            nameTextView = itemView.findViewById(R.id.sensor_name);
            nameTextView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    // TODO Auto-generated method stub
                    AlertDialog.Builder builder = new AlertDialog.Builder(SensorActivity.this);

                    builder.setMessage("Producent: "+sensor.getVendor() +
                            "Maximum range: "+sensor.getMaximumRange());
                    builder.setTitle("Alert !");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Ok", (DialogInterface.OnClickListener) (dialog, which) -> {
                        dialog.cancel();
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    return true;
                }
            });
        }

        public void bind(Sensor sensor) {
            this.sensor = sensor;
            iconImageView.setImageResource(R.drawable.icon_sensor);
            nameTextView.setText(sensor.getName());
            if (sensor == sensorList.get(1) || sensor == sensorList.get(5) || sensor == sensorList.get(2))
                nameTextView.setBackgroundColor(Color.rgb(255,255,0));
            else
                nameTextView.setBackgroundColor(Color.TRANSPARENT);
        }

        @Override
        public void onClick(View v) {
            if (sensor.getType() == TYPE_MAGNETIC_FIELD) {
                Intent intent = new Intent(SensorActivity.this, LocationActivity.class);
                startActivity(intent);
            }
            else {
                Intent intent = new Intent(SensorActivity.this, SensorDetailsActivity.class);
                intent.putExtra(KEY_EXTRA_SENSOR_NAME, sensor.getName());
                startActivity(intent);
            }

        }
    }
    private class SensorAdapter extends RecyclerView.Adapter<SensorHolder> {
        private List<Sensor> sensors;

        public SensorAdapter(List<Sensor> tasks) {

            this.sensors = tasks;
        }

        @NonNull
        @Override
        public SensorHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(SensorActivity.this);
            return new SensorHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull SensorHolder holder, int position) {
            Sensor sensor = sensors.get(position);
            holder.bind(sensor);
        }

        @Override
        public int getItemCount() {
            return sensors.size();
        }
    }
}