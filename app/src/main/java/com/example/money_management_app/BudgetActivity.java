package com.example.money_management_app;

import static com.example.money_management_app.MainActivity.url_firebase;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.joda.time.DateTime;
import org.joda.time.Months;
import org.joda.time.MutableDateTime;
import org.joda.time.Weeks;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

public class BudgetActivity extends AppCompatActivity {

    private TextView totalBudgetAmountTextView;
    private RecyclerView recyclerView;
    private FloatingActionButton fab;

    public DatabaseReference budgetRef, personalRef;
    private FirebaseAuth mAuth;
    private Toolbar toolbar;

    private String post_key = "";
    private String item = "";
    private int amount = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);

        mAuth = FirebaseAuth.getInstance();
        budgetRef = FirebaseDatabase.getInstance(url_firebase).getReference().child("budget").child(mAuth.getCurrentUser().getUid());
        personalRef = FirebaseDatabase.getInstance(url_firebase).getReference().child("personal").child(mAuth.getCurrentUser().getUid());

        totalBudgetAmountTextView = findViewById(R.id.totalBudgetAmountTextView);
        recyclerView = findViewById(R.id.recyclerView);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Month Budget");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        budgetRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.getChildrenCount() > 0) {
                    int totalAmount = 0;

                    for (DataSnapshot snap : snapshot.getChildren()) {
                        Data data = snap.getValue(Data.class);
                        totalAmount += data.getAmount();
                        String sTotal = String.valueOf("Month budget: " + totalAmount + " lei");
                        totalBudgetAmountTextView.setText(sTotal);
                    }

                    int dailyBudget = totalAmount / 30;
                    int weeklyBudget = totalAmount / 4;

                    personalRef.child("dailyBudget").setValue(dailyBudget);
                    personalRef.child("weeklyBudget").setValue(weeklyBudget);
                    personalRef.child("budget").setValue(totalAmount);
                } else {
                    personalRef.child("dailyBudget").setValue(0);
                    personalRef.child("weeklyBudget").setValue(0);
                    personalRef.child("budget").setValue(0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                additem();
            }
        });

        getMonthTransportBudgetRatios();
        getMonthGroceriesBudgetRatios();
        getMonthHouseBudgetRatios();
        getMonthEntertainmentBudgetRatios();
        getMonthEducationBudgetRatios();
        getMonthDonationBudgetRatios();
        getMonthPersonalBudgetRatios();
        getMonthEconomiesBudgetRatios();
        getMonthInvestmentBudgetRatios();
        getMonthOtherBudgetRatios();

    }

    private void getMonthOtherBudgetRatios() {
        Query query = budgetRef.orderByChild("item").equalTo("Other");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    int pTotal = 0;
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        pTotal = Integer.parseInt(String.valueOf(total));
                    }

                    int dayOtherRatio = pTotal/30;
                    int weekOtherRatio = pTotal/4;
                    int monthOtherRatio = pTotal;

                    personalRef.child("dayOtherRatio").setValue(dayOtherRatio);
                    personalRef.child("weekOtherRatio").setValue(weekOtherRatio);
                    personalRef.child("monthOtherRatio").setValue(monthOtherRatio);
                } else {
                    personalRef.child("dayOtherRatio").setValue(0);
                    personalRef.child("weekOtherRatio").setValue(0);
                    personalRef.child("monthOtherRatio").setValue(0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getMonthInvestmentBudgetRatios() {
        Query query = budgetRef.orderByChild("item").equalTo("Investment");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    int pTotal = 0;
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        pTotal = Integer.parseInt(String.valueOf(total));
                    }

                    int dayInvestmentRatio = pTotal/30;
                    int weekInvestmentRatio = pTotal/4;
                    int monthInvestmentRatio = pTotal;

                    personalRef.child("dayInvestmentRatio").setValue(dayInvestmentRatio);
                    personalRef.child("weekInvestmentRatio").setValue(weekInvestmentRatio);
                    personalRef.child("monthInvestmentRatio").setValue(monthInvestmentRatio);
                } else {
                    personalRef.child("dayInvestmentRatio").setValue(0);
                    personalRef.child("weekInvestmentRatio").setValue(0);
                    personalRef.child("monthInvestmentRatio").setValue(0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getMonthEconomiesBudgetRatios() {
        Query query = budgetRef.orderByChild("item").equalTo("Economies");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    int pTotal = 0;
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        pTotal = Integer.parseInt(String.valueOf(total));
                    }

                    int dayEconomiesRatio = pTotal/30;
                    int weekEconomiesRatio = pTotal/4;
                    int monthEconomiesRatio = pTotal;

                    personalRef.child("dayEconomiesRatio").setValue(dayEconomiesRatio);
                    personalRef.child("weekEconomiesRatio").setValue(weekEconomiesRatio);
                    personalRef.child("monthEconomiesRatio").setValue(monthEconomiesRatio);
                } else {
                    personalRef.child("dayEconomiesRatio").setValue(0);
                    personalRef.child("weekEconomiesRatio").setValue(0);
                    personalRef.child("monthEconomiesRatio").setValue(0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getMonthPersonalBudgetRatios() {
        Query query = budgetRef.orderByChild("item").equalTo("Personal");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    int pTotal = 0;
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        pTotal = Integer.parseInt(String.valueOf(total));
                    }

                    int dayPersonalRatio = pTotal/30;
                    int weekPersonalRatio = pTotal/4;
                    int monthPersonalRatio = pTotal;

                    personalRef.child("dayPersonalRatio").setValue(dayPersonalRatio);
                    personalRef.child("weekPersonalRatio").setValue(weekPersonalRatio);
                    personalRef.child("monthPersonalRatio").setValue(monthPersonalRatio);
                } else {
                    personalRef.child("dayPersonalsRatio").setValue(0);
                    personalRef.child("weekPersonalRatio").setValue(0);
                    personalRef.child("monthPersonalRatio").setValue(0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getMonthDonationBudgetRatios() {
        Query query = budgetRef.orderByChild("item").equalTo("Donations");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    int pTotal = 0;
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        pTotal = Integer.parseInt(String.valueOf(total));
                    }

                    int dayDonationsRatio = pTotal/30;
                    int weekDonationsRatio = pTotal/4;
                    int monthDonationsRatio = pTotal;

                    personalRef.child("dayDonationsRatio").setValue(dayDonationsRatio);
                    personalRef.child("weekDonationsRatio").setValue(weekDonationsRatio);
                    personalRef.child("monthDonationsRatio").setValue(monthDonationsRatio);
                } else {
                    personalRef.child("dayDonationsRatio").setValue(0);
                    personalRef.child("weekDonationsRatio").setValue(0);
                    personalRef.child("monthDonationsRatio").setValue(0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getMonthEducationBudgetRatios() {
        Query query = budgetRef.orderByChild("item").equalTo("Education");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    int pTotal = 0;
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        pTotal = Integer.parseInt(String.valueOf(total));
                    }

                    int dayEducationRatio = pTotal/30;
                    int weekEducationRatio = pTotal/4;
                    int monthEducationRatio = pTotal;

                    personalRef.child("dayEducationRatio").setValue(dayEducationRatio);
                    personalRef.child("weekEducationRatio").setValue(weekEducationRatio);
                    personalRef.child("monthEducationRatio").setValue(monthEducationRatio);
                } else {
                    personalRef.child("dayEducationRatio").setValue(0);
                    personalRef.child("weekEducationRatio").setValue(0);
                    personalRef.child("monthEducationRatio").setValue(0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getMonthEntertainmentBudgetRatios() {
        Query query = budgetRef.orderByChild("item").equalTo("Entertainment");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    int pTotal = 0;
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        pTotal = Integer.parseInt(String.valueOf(total));
                    }

                    int dayEntertainmentRatio = pTotal/30;
                    int weekEntertainmentRatio = pTotal/4;
                    int monthEntertainmentRatio = pTotal;

                    personalRef.child("dayEntertainmentRatio").setValue(dayEntertainmentRatio);
                    personalRef.child("weekEntertainmentRatio").setValue(weekEntertainmentRatio);
                    personalRef.child("monthEntertainmentRatio").setValue(monthEntertainmentRatio);
                } else {
                    personalRef.child("dayEntertainmentRatio").setValue(0);
                    personalRef.child("weekEntertainmentRatio").setValue(0);
                    personalRef.child("monthEntertainmentRatio").setValue(0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getMonthHouseBudgetRatios() {
        Query query = budgetRef.orderByChild("item").equalTo("House");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    int pTotal = 0;
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        pTotal = Integer.parseInt(String.valueOf(total));
                    }

                    int dayHouseRatio = pTotal/30;
                    int weekHouseRatio = pTotal/4;
                    int monthHouseRatio = pTotal;

                    personalRef.child("dayHouseRatio").setValue(dayHouseRatio);
                    personalRef.child("weekHouseRatio").setValue(weekHouseRatio);
                    personalRef.child("monthHouseRatio").setValue(monthHouseRatio);
                } else {
                    personalRef.child("dayHouseRatio").setValue(0);
                    personalRef.child("weekHouseRatio").setValue(0);
                    personalRef.child("monthHouseRatio").setValue(0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getMonthGroceriesBudgetRatios() {
        Query query = budgetRef.orderByChild("item").equalTo("Groceries");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    int pTotal = 0;
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        pTotal = Integer.parseInt(String.valueOf(total));
                    }

                    int dayGroceriesRatio = pTotal/30;
                    int weekGroceriesRatio = pTotal/4;
                    int monthGroceriesRatio = pTotal;

                    personalRef.child("dayGroceriesRatio").setValue(dayGroceriesRatio);
                    personalRef.child("weekGroceriesRatio").setValue(weekGroceriesRatio);
                    personalRef.child("monthGroceriesRatio").setValue(monthGroceriesRatio);
                } else {
                    personalRef.child("dayGroceriesRatio").setValue(0);
                    personalRef.child("weekGroceriesRatio").setValue(0);
                    personalRef.child("monthGroceriesRatio").setValue(0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getMonthTransportBudgetRatios() {
        Query query = budgetRef.orderByChild("item").equalTo("Transport");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    int pTotal = 0;
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        pTotal = Integer.parseInt(String.valueOf(total));
                    }

                    int dayTransportRatio = pTotal/30;
                    int weekTransportRatio = pTotal/4;
                    int monthTransportRatio = pTotal;

                    personalRef.child("dayTransportRatio").setValue(dayTransportRatio);
                    personalRef.child("weekTransportRatio").setValue(weekTransportRatio);
                    personalRef.child("monthTransportRatio").setValue(monthTransportRatio);
                } else {
                    personalRef.child("dayTransportRatio").setValue(0);
                    personalRef.child("weekTransportRatio").setValue(0);
                    personalRef.child("monthTransportRatio").setValue(0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void additem() {
        AlertDialog.Builder myDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View myView = inflater.inflate(R.layout.input_layout, null);
        myDialog.setView(myView);

        final AlertDialog dialog = myDialog.create();
        dialog.setCancelable(false);

        final Spinner itemSpinner = myView.findViewById(R.id.itemspinner);
        final EditText amount = myView.findViewById(R.id.amount);
        final Button cancel = myView.findViewById(R.id.cancel);
        final Button save = myView.findViewById(R.id.save);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String budgetAmount = amount.getText().toString();
                String budgetItem = itemSpinner.getSelectedItem().toString();

                if (TextUtils.isEmpty(budgetAmount)) {
                    amount.setError("Amount is required");
                    return;
                }

                if (budgetItem.equals("Select item")) {
                    Toast.makeText(BudgetActivity.this, "Select a valid item", Toast.LENGTH_SHORT).show();
                }

                else {
                    String id = budgetRef.push().getKey();
                    DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                    Calendar calendar = Calendar.getInstance();
                    String date = dateFormat.format(calendar.getTime());

                    MutableDateTime epoch = new MutableDateTime();
                    epoch.setDate(0);
                    DateTime now = new DateTime();
                    Weeks weeks = Weeks.weeksBetween(epoch, now);
                    Months months = Months.monthsBetween(epoch, now);

                    String itemDay = budgetItem + date;
                    String itemWeek = budgetItem + weeks.getWeeks();
                    String itemMonth = budgetItem + months.getMonths();

                    Data data = new Data(budgetItem, date, id, itemDay, itemWeek, itemMonth, Integer.parseInt(budgetAmount), months.getMonths(), weeks.getWeeks(), null);
                    budgetRef.child(id).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(BudgetActivity.this, "Budget item added successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(BudgetActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }
                dialog.dismiss();

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        dialog.show();
    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Data> options = new FirebaseRecyclerOptions.Builder<Data>()
                .setQuery(budgetRef, Data.class)
                .build();

        FirebaseRecyclerAdapter<Data, MyViewHolder> adapter = new FirebaseRecyclerAdapter<Data, MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position, @NonNull Data model) {
                holder.setItemAmount("Allocated amount: " + model.getAmount() + " lei");
                holder.setDate("On: " + model.getDate());
                holder.setItemName("Category: " + model.getItem());

                holder.notes.setVisibility(View.GONE);

                switch(model.getItem()) {
                    case "Transport":
                        holder.imageView.setImageResource(R.drawable.ic_transport);
                        break;
                    case "Groceries":
                        holder.imageView.setImageResource(R.drawable.ic_groceries);
                        break;
                    case "House":
                        holder.imageView.setImageResource(R.drawable.ic_house);
                        break;
                    case "Entertainment":
                        holder.imageView.setImageResource(R.drawable.ic_entertainment);
                        break;
                    case "Education":
                        holder.imageView.setImageResource(R.drawable.ic_education);
                        break;
                    case "Economies":
                        holder.imageView.setImageResource(R.drawable.ic_economies);
                        break;
                    case "Donations":
                        holder.imageView.setImageResource(R.drawable.ic_donation);
                        break;
                    case "Investment":
                        holder.imageView.setImageResource(R.drawable.ic_investment);
                        break;
                    case "Personal":
                        holder.imageView.setImageResource(R.drawable.ic_personal);
                        break;
                    case "Other":
                        holder.imageView.setImageResource(R.drawable.ic_other);
                        break;
                }

                holder.myView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        post_key = getRef(position).getKey();
                        item = model.getItem();
                        amount = model.getAmount();
                        updateData();
                    }
                });
            }

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.retrieve_layout, parent, false);
                return new MyViewHolder(view);
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        View myView;
        public ImageView imageView;
        public TextView notes, date;
        public MyViewHolder (View itemView) {
            super(itemView);
            myView = itemView;
            imageView = itemView.findViewById(R.id.imageView);
            notes = itemView.findViewById(R.id.note);
            date = itemView.findViewById(R.id.date);
        }

        public void setItemName (String itemName) {
            TextView item = myView.findViewById(R.id.item);
            item.setText(itemName);
        }

        public void setItemAmount (String itemAmount) {
            TextView amount = myView.findViewById(R.id.amount);
            amount.setText(itemAmount);
        }

        public void setDate (String itemDate) {
            TextView date = myView.findViewById(R.id.date);
            date.setText(itemDate);
        }
    }

    private void updateData() {
        AlertDialog.Builder myDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View myView = inflater.inflate(R.layout.update_layout, null);

        myDialog.setView(myView);
        final AlertDialog dialog = myDialog.create();

        final TextView myItem = myView.findViewById(R.id.itemName);
        final EditText myAmount = myView.findViewById(R.id.amount);
        final EditText myNotes = myView.findViewById(R.id.note);

        myNotes.setVisibility(View.GONE);

        myItem.setText(item);

        myAmount.setText(String.valueOf(amount));
        myAmount.setSelection(String.valueOf(amount).length());

        Button btnDelete = myView.findViewById(R.id.btnDelete);
        Button btnUpdate = myView.findViewById(R.id.btnUpdate);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amount = Integer.parseInt(myAmount.getText().toString());

                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                Calendar calendar = Calendar.getInstance();
                String date = dateFormat.format(calendar.getTime());

                MutableDateTime epoch = new MutableDateTime();
                epoch.setDate(0);
                DateTime now = new DateTime();
                Weeks weeks = Weeks.weeksBetween(epoch, now);
                Months months = Months.monthsBetween(epoch, now);

                String itemDay = item + date;
                String itemWeek = item + weeks.getWeeks();
                String itemMonth = item + months.getMonths();

                Data data = new Data(item, date, post_key, itemDay, itemWeek, itemMonth, amount, months.getMonths(), weeks.getWeeks(), null);

                budgetRef.child(post_key).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(BudgetActivity.this, "Budget item updated successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(BudgetActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });

                dialog.dismiss();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                budgetRef.child(post_key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(BudgetActivity.this, "Budget item deleted successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(BudgetActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });

                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
