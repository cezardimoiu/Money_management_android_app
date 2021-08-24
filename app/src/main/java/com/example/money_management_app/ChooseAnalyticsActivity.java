package com.example.money_management_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ChooseAnalyticsActivity extends AppCompatActivity {

    private CardView todayCardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_analytics);

        todayCardView = findViewById(R.id.todayCardView);

        todayCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChooseAnalyticsActivity.this, DailyAnalyticsActivity.class);
                startActivity(intent);
            }
        });
    }


}