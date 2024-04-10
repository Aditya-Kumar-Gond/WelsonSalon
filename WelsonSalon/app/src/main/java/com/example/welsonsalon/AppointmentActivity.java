package com.example.welsonsalon;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AppointmentActivity extends AppCompatActivity {
    TextView Selected_service,Selected_pro,Selected_date,Selected_time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);

        Selected_service = findViewById(R.id.selected_service);
        Selected_pro = findViewById(R.id.selected_pro);
        Selected_service = findViewById(R.id.selected_date);
        Selected_service = findViewById(R.id.selected_time);

        Intent intent = getIntent();
        //String service_intent = intent.getStringExtra("")
    }
}