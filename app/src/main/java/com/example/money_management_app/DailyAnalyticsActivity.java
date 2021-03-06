package com.example.money_management_app;

import static com.example.money_management_app.MainActivity.url_firebase;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.anychart.enums.Align;
import com.anychart.enums.LegendLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.joda.time.DateTime;
import org.joda.time.MutableDateTime;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

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
        getSupportActionBar().setTitle("Today analytics");

        monthSpentAmount = findViewById(R.id.monthSpentAmount);

//analyticsTextViews
        analyticsTransportAmount = findViewById(R.id.analyticsTransportAmount);
        analyticsGroceriesAmount = findViewById(R.id.analyticsGroceriesAmount);
        analyticsHouseAmount = findViewById(R.id.analyticsHouseAmount);
        analyticsEntertainmentAmount = findViewById(R.id.analyticsEntertainmentAmount);
        analyticsEducationAmount = findViewById(R.id.analyticsEntertainmentAmount);
        analyticsDonationAmount = findViewById(R.id.analyticsDonationAmount);
        analyticsPersonalAmount = findViewById(R.id.analyticsPersonalAmount);
        analyticsEconomiesAmount = findViewById(R.id.analyticsEconomiesAmount);
        analyticsInvestmentAmount = findViewById(R.id.analyticsInvestmentAmount);
        analyticsOtherAmount = findViewById(R.id.analyticsOtherAmount);

//Database references
        mAuth = FirebaseAuth.getInstance();
        onlineUserId = mAuth.getCurrentUser().getUid();
        expenseRef = FirebaseDatabase.getInstance(url_firebase).getReference("expenses").child(onlineUserId);
        personalRef = FirebaseDatabase.getInstance(url_firebase).getReference("personal").child(onlineUserId);

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

        getTotalWeekTransportExpense();
        getTotalWeekGroceriesExpense();
        getTotalWeekHouseExpense();
        getTotalWeekEntertainmentExpense();
        getTotalWeekEducationExpense();
        getTotalWeekDonationExpense();
        getTotalWeekPersonalExpense();
        getTotalWeekEconomiesExpense();
        getTotalWeekInvestmentExpense();
        getTotalWeekOtherExpense();
        getTotalDaySpending();

        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        loadGraph();
                        setStatusAndImageResource();
                    }
                },
                2000
        );
    }

    private void getTotalWeekTransportExpense() {
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0); //Set to Epoch time
        DateTime now = new DateTime();

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Calendar cal = Calendar.getInstance();
        String date = dateFormat.format(cal.getTime());
        String itemDay = "Transport"+date;

        DatabaseReference reference = FirebaseDatabase.getInstance(url_firebase).getReference("expenses").child(onlineUserId);
        Query query = reference.orderByChild("itemDay").equalTo(itemDay);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    int totalAmount = 0;
                    for (DataSnapshot ds :  snapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = Integer.parseInt(String.valueOf(total));
                        totalAmount += pTotal;
                        analyticsTransportAmount.setText("Spent: " + totalAmount);
                    }
                    personalRef.child("dayTransport").setValue(totalAmount);
                }
                else {
                    relativeLayoutTransport.setVisibility(View.GONE);
                    personalRef.child("dayTransport").setValue(0);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DailyAnalyticsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTotalWeekGroceriesExpense() {
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0); //Set to Epoch time
        DateTime now = new DateTime();

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Calendar cal = Calendar.getInstance();
        String date = dateFormat.format(cal.getTime());
        String itemDay = "Groceries"+date;

        DatabaseReference reference = FirebaseDatabase.getInstance(url_firebase).getReference("expenses").child(onlineUserId);
        Query query = reference.orderByChild("itemDay").equalTo(itemDay);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    int totalAmount = 0;
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = Integer.parseInt(String.valueOf(total));
                        totalAmount += pTotal;
                        analyticsGroceriesAmount.setText("Spent: " + totalAmount);
                    }
                    personalRef.child("dayGroceries").setValue(totalAmount);
                }
                else {
                    relativeLayoutGroceries.setVisibility(View.GONE);
                    personalRef.child("dayGroceries").setValue(0);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DailyAnalyticsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTotalWeekHouseExpense() {
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0); //Set to Epoch time
        DateTime now = new DateTime();

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Calendar cal = Calendar.getInstance();
        String date = dateFormat.format(cal.getTime());
        String itemDay = "House"+date;

        DatabaseReference reference = FirebaseDatabase.getInstance(url_firebase).getReference("expenses").child(onlineUserId);
        Query query = reference.orderByChild("itemDay").equalTo(itemDay);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    int totalAmount = 0;
                    for (DataSnapshot ds :  snapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = Integer.parseInt(String.valueOf(total));
                        totalAmount += pTotal;
                        analyticsHouseAmount.setText("Spent: " + totalAmount);
                    }
                    personalRef.child("dayHouse").setValue(totalAmount);
                }
                else {
                    relativeLayoutHouse.setVisibility(View.GONE);
                    personalRef.child("dayHouse").setValue(0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DailyAnalyticsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTotalWeekEntertainmentExpense() {
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0); //Set to Epoch time
        DateTime now = new DateTime();

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Calendar cal = Calendar.getInstance();
        String date = dateFormat.format(cal.getTime());
        String itemDay = "Entertainment"+date;

        DatabaseReference reference = FirebaseDatabase.getInstance(url_firebase).getReference("expenses").child(onlineUserId);
        Query query = reference.orderByChild("itemDay").equalTo(itemDay);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    int totalAmount = 0;
                    for (DataSnapshot ds :  snapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = Integer.parseInt(String.valueOf(total));
                        totalAmount += pTotal;
                        analyticsEntertainmentAmount.setText("Spent: " + totalAmount);
                    }
                    personalRef.child("dayEntertainment").setValue(totalAmount);
                }
                else {
                    relativeLayoutEntertainment.setVisibility(View.GONE);
                    personalRef.child("dayEntertainment").setValue(0);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DailyAnalyticsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTotalWeekEducationExpense() {
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0); //Set to Epoch time
        DateTime now = new DateTime();

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Calendar cal = Calendar.getInstance();
        String date = dateFormat.format(cal.getTime());
        String itemDay = "Education"+date;

        DatabaseReference reference = FirebaseDatabase.getInstance(url_firebase).getReference("expenses").child(onlineUserId);
        Query query = reference.orderByChild("itemDay").equalTo(itemDay);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    int totalAmount = 0;
                    for (DataSnapshot ds :  snapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = Integer.parseInt(String.valueOf(total));
                        totalAmount += pTotal;
                        analyticsEducationAmount.setText("Spent: " + totalAmount);
                    }
                    personalRef.child("dayEducation").setValue(totalAmount);
                }
                else {
                    relativeLayoutEducation.setVisibility(View.GONE);
                    personalRef.child("dayEducation").setValue(0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DailyAnalyticsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTotalWeekDonationExpense() {
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0); //Set to Epoch time
        DateTime now = new DateTime();

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Calendar cal = Calendar.getInstance();
        String date = dateFormat.format(cal.getTime());
        String itemDay = "Donation" + date;

        DatabaseReference reference = FirebaseDatabase.getInstance(url_firebase).getReference("expenses").child(onlineUserId);
        Query query = reference.orderByChild("itemDay").equalTo(itemDay);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    int totalAmount = 0;
                    for (DataSnapshot ds :  snapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = Integer.parseInt(String.valueOf(total));
                        totalAmount += pTotal;
                        analyticsDonationAmount.setText("Spent: " + totalAmount);
                    }
                    personalRef.child("dayDonation").setValue(totalAmount);
                }
                else {
                    relativeLayoutDonation.setVisibility(View.GONE);
                    personalRef.child("dayDonation").setValue(0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DailyAnalyticsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTotalWeekPersonalExpense() {
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0); //Set to Epoch time
        DateTime now = new DateTime();

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Calendar cal = Calendar.getInstance();
        String date = dateFormat.format(cal.getTime());
        String itemDay = "Personal"+date;

        DatabaseReference reference = FirebaseDatabase.getInstance(url_firebase).getReference("expenses").child(onlineUserId);
        Query query = reference.orderByChild("itemDay").equalTo(itemDay);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    int totalAmount = 0;
                    for (DataSnapshot ds :  snapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = Integer.parseInt(String.valueOf(total));
                        totalAmount += pTotal;
                        analyticsPersonalAmount.setText("Spent: " + totalAmount);
                    }
                    personalRef.child("dayPersonal").setValue(totalAmount);
                }
                else {
                    relativeLayoutPersonal.setVisibility(View.GONE);
                    personalRef.child("dayPersonal").setValue(0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DailyAnalyticsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTotalWeekEconomiesExpense() {
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0); //Set to Epoch time
        DateTime now = new DateTime();

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Calendar cal = Calendar.getInstance();
        String date = dateFormat.format(cal.getTime());
        String itemDay = "Economies"+date;

        DatabaseReference reference = FirebaseDatabase.getInstance(url_firebase).getReference("expenses").child(onlineUserId);
        Query query = reference.orderByChild("itemDay").equalTo(itemDay);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    int totalAmount = 0;
                    for (DataSnapshot ds :  snapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = Integer.parseInt(String.valueOf(total));
                        totalAmount += pTotal;
                        analyticsEconomiesAmount.setText("Spent: " + totalAmount);
                    }
                    personalRef.child("dayEconomies").setValue(totalAmount);
                }
                else {
                    relativeLayoutEconomies.setVisibility(View.GONE);
                    personalRef.child("dayEconomies").setValue(0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DailyAnalyticsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTotalWeekInvestmentExpense() {
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0); //Set to Epoch time
        DateTime now = new DateTime();

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Calendar cal = Calendar.getInstance();
        String date = dateFormat.format(cal.getTime());
        String itemDay = "Investment"+date;

        DatabaseReference reference = FirebaseDatabase.getInstance(url_firebase).getReference("expenses").child(onlineUserId);
        Query query = reference.orderByChild("itemDay").equalTo(itemDay);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    int totalAmount = 0;
                    for (DataSnapshot ds :  snapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = Integer.parseInt(String.valueOf(total));
                        totalAmount += pTotal;
                        analyticsInvestmentAmount.setText("Spent: " + totalAmount);
                    }
                    personalRef.child("dayInvestment").setValue(totalAmount);
                }
                else {
                    relativeLayoutInvestment.setVisibility(View.GONE);
                    personalRef.child("dayInvestment").setValue(0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DailyAnalyticsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTotalWeekOtherExpense() {
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0); //Set to Epoch time
        DateTime now = new DateTime();

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Calendar cal = Calendar.getInstance();
        String date = dateFormat.format(cal.getTime());
        String itemDay = "Other"+date;

        DatabaseReference reference = FirebaseDatabase.getInstance(url_firebase).getReference("expenses").child(onlineUserId);
        Query query = reference.orderByChild("itemDay").equalTo(itemDay);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    int totalAmount = 0;
                    for (DataSnapshot ds :  snapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = Integer.parseInt(String.valueOf(total));
                        totalAmount += pTotal;
                        analyticsOtherAmount.setText("Spent: " + totalAmount);
                    }
                    personalRef.child("dayOther").setValue(totalAmount);
                }
                else {
                    relativeLayoutOther.setVisibility(View.GONE);
                    personalRef.child("dayOther").setValue(0);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DailyAnalyticsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTotalDaySpending() {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Calendar cal = Calendar.getInstance();
        String date = dateFormat.format(cal.getTime());
        DatabaseReference reference = FirebaseDatabase.getInstance(url_firebase).getReference("expenses").child(onlineUserId);
        Query query = reference.orderByChild("date").equalTo(date);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0){
                    int totalAmount = 0;
                    for (DataSnapshot ds :  dataSnapshot.getChildren()){
                        Map<String, Object> map = (Map<String, Object>)ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = Integer.parseInt(String.valueOf(total));
                        totalAmount+=pTotal;

                    }
                    totalBudgetAmountTextView.setText("Total day's spending: " + totalAmount + " lei");
                    monthSpentAmount.setText("Total Spent: " + totalAmount + " lei");
                }
                else {
                    totalBudgetAmountTextView.setText("You've not spent any money today");
                    anyChartView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void loadGraph() {
        personalRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    int transportTotal;
                    if (snapshot.hasChild("dayTransport")){
                        transportTotal = Integer.parseInt(snapshot.child("dayTransport").getValue().toString());
                    } else {
                        transportTotal = 0;
                    }

                    int groceriesTotal;
                    if (snapshot.hasChild("dayGroceries")){
                        groceriesTotal = Integer.parseInt(snapshot.child("dayGroceries").getValue().toString());
                    } else {
                        groceriesTotal = 0;
                    }

                    int houseTotal;
                    if (snapshot.hasChild("dayHouse")){
                        houseTotal = Integer.parseInt(snapshot.child("dayHouse").getValue().toString());
                    } else {
                        houseTotal = 0;
                    }

                    int entertainmentTotal;
                    if (snapshot.hasChild("dayEntertainment")){
                        entertainmentTotal = Integer.parseInt(snapshot.child("dayEntertainment").getValue().toString());
                    } else {
                        entertainmentTotal = 0;
                    }

                    int educationTotal;
                    if (snapshot.hasChild("dayEducation")){
                        educationTotal = Integer.parseInt(snapshot.child("dayEducation").getValue().toString());
                    } else {
                        educationTotal = 0;
                    }

                    int donationTotal;
                    if (snapshot.hasChild("dayDonation")){
                        donationTotal = Integer.parseInt(snapshot.child("dayDonation").getValue().toString());
                    } else {
                        donationTotal = 0;
                    }

                    int personalTotal;
                    if (snapshot.hasChild("dayPersonal")){
                        personalTotal = Integer.parseInt(snapshot.child("dayPersonal").getValue().toString());
                    } else {
                        personalTotal = 0;
                    }

                    int economiesTotal;
                    if (snapshot.hasChild("dayEconomies")){
                        economiesTotal = Integer.parseInt(snapshot.child("dayEconomies").getValue().toString());
                    } else {
                        economiesTotal = 0;
                    }

                    int investmentTotal;
                    if (snapshot.hasChild("dayInvestment")){
                        investmentTotal = Integer.parseInt(snapshot.child("dayInvestment").getValue().toString());
                    } else {
                        investmentTotal = 0;
                    }
                    int otherTotal;
                    if (snapshot.hasChild("dayOther")){
                        otherTotal = Integer.parseInt(snapshot.child("dayOther").getValue().toString());
                    } else {
                        otherTotal = 0;
                    }

                    Pie pie = AnyChart.pie();
                    List<DataEntry> data = new ArrayList<>();
                    data.add(new ValueDataEntry("Transport", transportTotal));
                    data.add(new ValueDataEntry("House", houseTotal));
                    data.add(new ValueDataEntry("Groceries", groceriesTotal));
                    data.add(new ValueDataEntry("Entertainment", entertainmentTotal));
                    data.add(new ValueDataEntry("Education", educationTotal));
                    data.add(new ValueDataEntry("Donation", donationTotal));
                    data.add(new ValueDataEntry("Personal", personalTotal));
                    data.add(new ValueDataEntry("Economies", economiesTotal));
                    data.add(new ValueDataEntry("Investment", investmentTotal));
                    data.add(new ValueDataEntry("Other", otherTotal));


                    pie.data(data);

                    pie.title("Daily Analytics");

                    pie.labels().position("outside");

                    pie.legend().title().enabled(true);
                    pie.legend().title()
                            .text("Items Spent On")
                            .padding(0d, 0d, 10d, 0d);

                    pie.legend()
                            .position("center-bottom")
                            .itemsLayout(LegendLayout.HORIZONTAL)
                            .align(Align.CENTER);

                    anyChartView.setChart(pie);
                }
                else {
                    Toast.makeText(DailyAnalyticsActivity.this,"Child does not exist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DailyAnalyticsActivity.this,"Child does not exist", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setStatusAndImageResource() {
        personalRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() ){
                    float transportTotal;
                    if (snapshot.hasChild("dayTransport")) {
                        transportTotal = Integer.parseInt(snapshot.child("dayTransport").getValue().toString());
                    } else {
                        transportTotal = 0;
                    }

                    float groceriesTotal;
                    if (snapshot.hasChild("dayGroceries")) {
                        groceriesTotal = Integer.parseInt(snapshot.child("dayGroceries").getValue().toString());
                    } else {
                        groceriesTotal = 0;
                    }

                    float houseTotal;
                    if (snapshot.hasChild("dayHouse")) {
                        houseTotal = Integer.parseInt(snapshot.child("dayHouse").getValue().toString());
                    } else {
                        houseTotal = 0;
                    }

                    float entertainmentTotal;
                    if (snapshot.hasChild("dayEntertainment")) {
                        entertainmentTotal = Integer.parseInt(snapshot.child("dayEntertainment").getValue().toString());
                    } else {
                        entertainmentTotal=0;
                    }

                    float educationTotal;
                    if (snapshot.hasChild("dayEducation")) {
                        educationTotal = Integer.parseInt(snapshot.child("dayEducation").getValue().toString());
                    } else {
                        educationTotal = 0;
                    }

                    float donationTotal;
                    if (snapshot.hasChild("dayDonation")) {
                        donationTotal = Integer.parseInt(snapshot.child("dayDonation").getValue().toString());
                    } else {
                        donationTotal = 0;
                    }

                    float personalTotal;
                    if (snapshot.hasChild("dayPersonal")) {
                        personalTotal = Integer.parseInt(snapshot.child("dayPersonal").getValue().toString());
                    } else {
                        personalTotal = 0;
                    }

                    float economiesTotal;
                    if (snapshot.hasChild("dayEconomies")) {
                        economiesTotal = Integer.parseInt(snapshot.child("dayEconomies").getValue().toString());
                    } else {
                        economiesTotal =0;
                    }

                    float investmentTotal;
                    if (snapshot.hasChild("dayInvestment")) {
                        investmentTotal = Integer.parseInt(snapshot.child("dayInvestment").getValue().toString());
                    } else {
                        investmentTotal=0;
                    }
                    float otherTotal;
                    if (snapshot.hasChild("dayOther")) {
                        otherTotal = Integer.parseInt(snapshot.child("dayOther").getValue().toString());
                    } else {
                        otherTotal = 0;
                    }

                    float monthTotalSpentAmount;
                    if (snapshot.hasChild("today")) {
                        monthTotalSpentAmount = Integer.parseInt(snapshot.child("today").getValue().toString());
                    } else {
                        monthTotalSpentAmount = 0;
                    }



                    //GETTING RATIOS
                    float transportRatio;
                    if (snapshot.hasChild("dayTransportRatio")) {
                        transportRatio = Integer.parseInt(snapshot.child("dayTransportRatio").getValue().toString());
                    } else {
                        transportRatio = 0;
                    }

                    float groceriesRatio;
                    if (snapshot.hasChild("dayGroceriesRatio")) {
                        groceriesRatio = Integer.parseInt(snapshot.child("dayGroceriesRatio").getValue().toString());
                    }else {
                        groceriesRatio = 0;
                    }

                    float houseRatio;
                    if (snapshot.hasChild("dayHouseRatio")) {
                        houseRatio = Integer.parseInt(snapshot.child("dayHouseRatio").getValue().toString());
                    } else {
                        houseRatio = 0;
                    }

                    float entertainmentRatio;
                    if (snapshot.hasChild("dayEntertainmentRatio")) {
                        entertainmentRatio= Integer.parseInt(snapshot.child("dayEntertainmentRatio").getValue().toString());
                    } else {
                        entertainmentRatio = 0;
                    }

                    float educationRatio;
                    if (snapshot.hasChild("dayEducationRatio")) {
                        educationRatio= Integer.parseInt(snapshot.child("dayEducationRatio").getValue().toString());
                    } else {
                        educationRatio = 0;
                    }

                    float donationRatio;
                    if (snapshot.hasChild("dayDonationRatio")) {
                        donationRatio = Integer.parseInt(snapshot.child("dayChardayDonationRatioRatio").getValue().toString());
                    } else {
                        donationRatio = 0;
                    }

                    float personalRatio;
                    if (snapshot.hasChild("dayPersonalRatio")) {
                        personalRatio = Integer.parseInt(snapshot.child("dayPersonalRatio").getValue().toString());
                    } else {
                        personalRatio = 0;
                    }

                    float economiesRatio;
                    if (snapshot.hasChild("dayEconomiesRatio")) {
                        economiesRatio = Integer.parseInt(snapshot.child("dayEconomiesRatio").getValue().toString());
                    } else {
                        economiesRatio = 0;
                    }

                    float investmentRatio;
                    if (snapshot.hasChild("dayInvestmentRatio")) {
                        investmentRatio = Integer.parseInt(snapshot.child("dayInvestmentRatio").getValue().toString());
                    } else {
                        investmentRatio = 0;
                    }

                    float otherRatio;
                    if (snapshot.hasChild("dayOtherRatio")) {
                        otherRatio = Integer.parseInt(snapshot.child("dayOtherRatio").getValue().toString());
                    } else {
                        otherRatio = 0;
                    }

                    float monthTotalSpentAmountRatio;
                    if (snapshot.hasChild("dailyBudget")){
                        monthTotalSpentAmountRatio = Integer.parseInt(snapshot.child("dailyBudget").getValue().toString());
                    } else {
                        monthTotalSpentAmountRatio = 0;
                    }

                    float monthPercent = (monthTotalSpentAmount/monthTotalSpentAmountRatio)*100;
                    if (monthPercent < 50){
                        monthRatioSpending.setText(monthPercent + " %" + " used of " + monthTotalSpentAmountRatio + ". Status:");
                        monthRatioSpending_Image.setImageResource(R.drawable.green);
                    } else if (monthPercent >= 50 && monthPercent < 100){
                        monthRatioSpending.setText(monthPercent + " %" + " used of " + monthTotalSpentAmountRatio + ". Status:");
                        monthRatioSpending_Image.setImageResource(R.drawable.brown);
                    } else {
                        monthRatioSpending.setText(monthPercent+" %" +" used of "+monthTotalSpentAmountRatio + ". Status:");
                        monthRatioSpending_Image.setImageResource(R.drawable.red);

                    }

                    float transportPercent = (transportTotal/transportRatio) * 100;
                    if (transportPercent<50){
                        progress_ratio_transport.setText(transportPercent+" %" +" used of "+ transportRatio + ". Status:");
                        status_Image_transport.setImageResource(R.drawable.green);
                    } else if (transportPercent >= 50 && transportPercent < 100){
                        progress_ratio_transport.setText(transportPercent + " %" + " used of " + transportRatio + ". Status:");
                        status_Image_transport.setImageResource(R.drawable.brown);
                    } else {
                        progress_ratio_transport.setText(transportPercent + " %" +" used of " + transportRatio + ". Status:");
                        status_Image_transport.setImageResource(R.drawable.red);
                    }

                    float groceriesPercent = (groceriesTotal/groceriesRatio) * 100;
                    if (groceriesPercent < 50) {
                        progress_ratio_groceries.setText(groceriesPercent + " %" +" used of " + groceriesRatio + ". Status:");
                        status_Image_groceries.setImageResource(R.drawable.green);
                    } else if (groceriesPercent >= 50 && groceriesPercent < 100){
                        progress_ratio_groceries.setText(groceriesPercent + " %" +" used of " + groceriesRatio + ". Status:");
                        status_Image_groceries.setImageResource(R.drawable.brown);
                    } else {
                        progress_ratio_groceries.setText(groceriesPercent + " %" +" used of " + groceriesRatio + ". Status:");
                        status_Image_groceries.setImageResource(R.drawable.red);
                    }

                    float housePercent = (houseTotal/houseRatio) * 100;
                    if (housePercent < 50){
                        progress_ratio_house.setText(housePercent +" %" +" used of "+ houseRatio + ". Status:");
                        status_Image_house.setImageResource(R.drawable.green);
                    } else if (housePercent >= 50 && housePercent < 100){
                        progress_ratio_house.setText(housePercent +" %" +" used of "+ houseRatio + ". Status:");
                        status_Image_house.setImageResource(R.drawable.brown);
                    } else {
                        progress_ratio_house.setText(housePercent +" %" +" used of "+ houseRatio + ". Status:");
                        status_Image_house.setImageResource(R.drawable.red);
                    }

                    float entertainmentPercent = (entertainmentTotal/entertainmentRatio) * 100;
                    if (entertainmentPercent < 50){
                        progress_ratio_entertainment.setText(entertainmentPercent + " %" + " used of " + entertainmentRatio + ". Status:");
                        status_Image_entertainment.setImageResource(R.drawable.green);
                    } else if (entertainmentPercent >= 50 && entertainmentPercent < 100){
                        progress_ratio_entertainment.setText(entertainmentPercent + " %" + " used of " + entertainmentRatio + ". Status:");
                        status_Image_entertainment.setImageResource(R.drawable.brown);
                    } else {
                        progress_ratio_entertainment.setText(entertainmentPercent + " %" + " used of " + entertainmentRatio + ". Status:");
                        status_Image_entertainment.setImageResource(R.drawable.red);
                    }

                    float educationPercent = (educationTotal/educationRatio) * 100;
                    if (educationPercent < 50){
                        progress_ratio_education.setText(educationPercent + " %" + " used of " + educationRatio + ". Status:");
                        status_Image_education.setImageResource(R.drawable.green);
                    } else if (educationPercent >= 50 && educationPercent < 100){
                        progress_ratio_education.setText(educationPercent + " %" +" used of " + educationRatio + ". Status:");
                        status_Image_education.setImageResource(R.drawable.brown);
                    } else {
                        progress_ratio_education.setText(educationPercent + " %" +" used of " + educationRatio + ". Status:");
                        status_Image_education.setImageResource(R.drawable.red);
                    }

                    float donationPercent = (donationTotal/donationRatio) * 100;
                    if (donationPercent<50){
                        progress_ratio_donation.setText(donationPercent + " %" + " used of " + donationRatio + ". Status:");
                        status_Image_donation.setImageResource(R.drawable.green);
                    } else if (donationPercent >= 50 && donationPercent < 100){
                        progress_ratio_donation.setText(donationPercent + " %" + " used of " + donationRatio + ". Status:");
                        status_Image_donation.setImageResource(R.drawable.brown);
                    } else {
                        progress_ratio_donation.setText(donationPercent + " %" + " used of " + donationRatio + ". Status:");
                        status_Image_donation.setImageResource(R.drawable.red);
                    }

                    float personalPercent = (personalTotal/personalRatio) * 100;
                    if (personalPercent < 50){
                        progress_ratio_personal.setText(personalPercent + " %" + " used of " + personalRatio + ". Status:");
                        status_Image_personal.setImageResource(R.drawable.green);
                    } else if (personalPercent >= 50 && personalPercent < 100){
                        progress_ratio_personal.setText(personalPercent + " %" + " used of " + personalRatio + ". Status:");
                        status_Image_personal.setImageResource(R.drawable.brown);
                    } else {
                        progress_ratio_personal.setText(personalPercent + " %" + " used of " + personalRatio + ". Status:");
                        status_Image_personal.setImageResource(R.drawable.red);
                    }

                    float economiesPercent = (economiesTotal/economiesRatio) * 100;
                    if (economiesPercent < 50){
                        progress_ratio_economies.setText(economiesPercent + " %" + " used of "+ economiesRatio + ". Status:");
                        status_Image_economies.setImageResource(R.drawable.green);
                    } else if (economiesPercent >= 50 && economiesPercent < 100){
                        progress_ratio_economies.setText(economiesPercent + " %" + " used of "+ economiesRatio + ". Status:");
                        status_Image_economies.setImageResource(R.drawable.brown);
                    } else {
                        progress_ratio_economies.setText(economiesPercent + " %" + " used of "+ economiesRatio + ". Status:");
                        status_Image_economies.setImageResource(R.drawable.red);
                    }


                    float investmentPercent = (investmentTotal/investmentRatio) * 100;
                    if (investmentPercent < 50){
                        progress_ratio_investment.setText(investmentPercent + " %" + " used of " + investmentRatio + " . Status:");
                        status_Image_investment.setImageResource(R.drawable.green);
                    } else if (investmentPercent >= 50 && investmentPercent < 100){
                        progress_ratio_investment.setText(investmentPercent + " %" + " used of " + investmentRatio + " . Status:");
                        status_Image_investment.setImageResource(R.drawable.brown);
                    } else {
                        progress_ratio_investment.setText(investmentPercent + " %" + " used of " + investmentRatio + " . Status:");
                        status_Image_investment.setImageResource(R.drawable.red);
                    }


                    float otherPercent = (otherTotal/otherRatio) * 100;
                    if (otherPercent < 50){
                        progress_ratio_other.setText(otherPercent + " %" + " used of " + otherRatio + ". Status:");
                        status_Image_other.setImageResource(R.drawable.green);
                    } else if (otherPercent >= 50 && otherPercent < 100){
                        progress_ratio_other.setText(otherPercent + " %" + " used of " + otherRatio + ". Status:");
                        status_Image_other.setImageResource(R.drawable.brown);
                    } else {
                        progress_ratio_other.setText(otherPercent + " %" + " used of " + otherRatio + ". Status:");
                        status_Image_other.setImageResource(R.drawable.red);
                    }

                }
                else {
                    Toast.makeText(DailyAnalyticsActivity.this, "setStatusAndImageResource Errors", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
