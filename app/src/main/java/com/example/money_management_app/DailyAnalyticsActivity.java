package com.example.money_management_app;

import static com.example.money_management_app.MainActivity.url_firebase;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.anychart.AnyChartView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DailyAnalyticsActivity extends AppCompatActivity {

    private Toolbar settingsToolbar;

    private FirebaseAuth mAuth;
    private String onlineUserId = "";
    private DatabaseReference expenseRef, personalRef;

    private TextView totalBudgetAmountTextView, analyticsTransportAmount, analyticsGroceriesAmount, analyticsHouseAmount, analyticsEntertainmentAmount;
    private TextView analyticsEducationAmount, analyticsDonationAmount, analyticsPersonalAmount, analyticsEconomiesAmount,analyticsInvestmentAmount, analyticsOtherAmount;
    private TextView monthSpentAmount;

    private RelativeLayout relativeLayoutTransport, relativeLayoutGroceries, relativeLayoutHouse, relativeLayoutEntertainment, relativeLayoutEducation, relativeLayoutDonation;
    private RelativeLayout relativeLayoutPersonal, relativeLayoutEconomies, relativeLayoutInvestment, relativeLayoutOther, linearLayoutAnalysis;

    private AnyChartView anyChartView;

    private TextView progress_ratio_transport, progress_ratio_groceries, progress_ratio_house, progress_ratio_entertainment, progress_ratio_education, progress_ratio_donation;
    private TextView progress_ratio_personal, progress_ratio_economies, progress_ratio_investment, progress_ratio_other, monthRatioSpending;

    private ImageView status_Image_transport, status_Image_groceries, status_Image_house, status_Image_entertainment, status_Image_education, status_Image_donation;
    private ImageView status_Image_personal, status_Image_economies, status_Image_investment, status_Image_other, monthRatioSpending_Image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_analytics);

        settingsToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(settingsToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Today Analytics");

        mAuth = FirebaseAuth.getInstance();
        onlineUserId = mAuth.getCurrentUser().getUid();
        expenseRef = FirebaseDatabase.getInstance(url_firebase).getReference("expenses").child(onlineUserId);
        expenseRef = FirebaseDatabase.getInstance(url_firebase).getReference("personal").child(onlineUserId);

        totalBudgetAmountTextView = findViewById(R.id.totalBudgetAmountTextView);
        linearLayoutAnalysis = findViewById(R.id.linearLayoutAnalysis);
        monthRatioSpending = findViewById(R.id.monthRatioSpending);
        monthRatioSpending_Image = findViewById(R.id.monthRatioSpending_Image);

//textViews
        progress_ratio_transport = findViewById(R.id.progress_ratio_transport);
        progress_ratio_groceries = findViewById(R.id.progress_ratio_groceries);
        progress_ratio_house = findViewById(R.id.progress_ratio_house);
        progress_ratio_entertainment = findViewById(R.id.progress_ratio_entertainment);
        progress_ratio_education = findViewById(R.id.progress_ratio_education);
        progress_ratio_donation = findViewById(R.id.progress_ratio_donation);
        progress_ratio_personal = findViewById(R.id.progress_ratio_personal);
        progress_ratio_economies = findViewById(R.id.progress_ratio_economies);
        progress_ratio_investment = findViewById(R.id.progress_ratio_investment);
        progress_ratio_other = findViewById(R.id.progress_ratio_other);

//layouts
        relativeLayoutTransport = findViewById(R.id.relativeLayoutTransport);
        relativeLayoutGroceries = findViewById(R.id.relativeLayoutGroceries);
        relativeLayoutHouse = findViewById(R.id.relativeLayoutHouse);
        relativeLayoutEntertainment = findViewById(R.id.relativeLayoutEntertainment);
        relativeLayoutEducation = findViewById(R.id.relativeLayoutEducation);
        relativeLayoutDonation = findViewById(R.id.relativeLayoutDonation);
        relativeLayoutPersonal = findViewById(R.id.relativeLayoutPersonal);
        relativeLayoutEconomies = findViewById(R.id.relativeLayoutEconomies);
        relativeLayoutInvestment = findViewById(R.id.relativeLayoutInvestment);
        relativeLayoutOther= findViewById(R.id.relativeLayoutOther);

//imageViews
        status_Image_transport = findViewById(R.id.transport_status);
        status_Image_groceries = findViewById(R.id.groceries_status);
        status_Image_house = findViewById(R.id.house_status);
        status_Image_entertainment = findViewById(R.id.entertainment_status);
        status_Image_education = findViewById(R.id.education_status);
        status_Image_donation = findViewById(R.id.donation_status);
        status_Image_personal = findViewById(R.id.personal_status);
        status_Image_economies = findViewById(R.id.economies_status);
        status_Image_investment = findViewById(R.id.investment_status);
        status_Image_other = findViewById(R.id.other_status);

        anyChartView = findViewById(R.id.anyChartView);
    }

}
